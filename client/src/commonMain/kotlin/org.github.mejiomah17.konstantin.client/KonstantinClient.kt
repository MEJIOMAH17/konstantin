package org.github.mejiomah17.konstantin.client

import io.ktor.client.HttpClient
import io.ktor.client.HttpClientConfig
import io.ktor.client.engine.cio.CIO
import io.ktor.client.engine.cio.CIOEngineConfig
import io.ktor.client.features.websocket.WebSockets
import io.ktor.client.features.websocket.webSocket
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.http.HttpMethod
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing

//TODO rewrite all client api to builder configuration instead of parameter passing
class KonstantinClient(
    private val host: String,
    private val port: Int,
    private val logger: Logger = StdOutLogger,
    private val request: HttpRequestBuilder.() -> Unit = {},
    private val path: String = "/",
    httpClientHook: HttpClientConfig<CIOEngineConfig>.() -> Unit = {},
    private val scope: CoroutineScope = DefaultClientScope
) : io.ktor.utils.io.core.Closeable {
    private val httpClient: HttpClient = HttpClient(CIO) {
        install(WebSockets)
        httpClientHook()
    }
    private val json = Json {
        ignoreUnknownKeys = true
    }
    private val subscribeNotifyChannel = Channel<Unit>(capacity = Channel.CONFLATED)
    private val subsciptions = ConcurrentMap<String, Channel<State>>()

    fun start(): Unit {
        scope.async {
            while (true) {
                runCatching {
                    clientWork()
                }.onFailure {
                    logger.error(it.toString())
                }
                delay(500)
            }

        }
    }

    suspend fun clientWork() {
        httpClient.webSocket(method = HttpMethod.Get, host = host, port = port, request = request, path = path) {
            async {
                subscribeNotifyChannel.send(Unit)
                for (event in subscribeNotifyChannel) {
                    send(Json.encodeToString(Event.Subscribe(subsciptions.keys.toList()) as Event))
                }
            }

            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val stateUpdate = json.decodeFromString<Event.StateUpdate>(receivedText)
                subsciptions[stateUpdate.thingId]?.send(stateUpdate.state)
            }
        }
    }

    fun <S : State> subscribe(
        thing: Thing<S>,
        channelFactory: (String) -> Channel<S> = { Channel(capacity = Channel.CONFLATED) }
    ): Channel<S> {
        val channel = subsciptions.computeIfAbsent(thing.id) {
            channelFactory(it) as Channel<State>
        }
        scope.async {
            subscribeNotifyChannel.send(Unit)
        }
        return channel as Channel<S>
    }

    fun <S : State> subscribe(
        things: List<Thing<S>>,
        channelFactory: (String) -> Channel<S> = { Channel(capacity = Channel.CONFLATED) }
    ): Map<Thing<S>, Channel<S>> {
        val result: Map<Thing<S>, Channel<State>> = things.associateWith { thing ->
            subsciptions.computeIfAbsent(thing.id) {
                channelFactory(it) as Channel<State>
            }
        }
        scope.async {
            subscribeNotifyChannel.send(Unit)
        }
        return result as Map<Thing<S>, Channel<S>>
    }


    fun unsubscribeAll() {
        subsciptions.clear()
        scope.async {
            subscribeNotifyChannel.send(Unit)
        }
    }

    fun unsubscribe(
        thingId: String,
    ) {
        subsciptions.remove(thingId)?.cancel()
        scope.async {
            subscribeNotifyChannel.send(Unit)
        }
    }

    fun unsubscribe(
        thingIds: Iterable<String>,
    ) {
        thingIds.forEach { subsciptions.remove(it)?.cancel() }
        scope.async {
            subscribeNotifyChannel.send(Unit)
        }
    }

    override fun close() {
        httpClient.close()
    }

}