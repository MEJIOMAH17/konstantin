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
import mu.KotlinLogging.logger
import org.github.mejiomah17.konstantin.api.ClientEvent
import org.github.mejiomah17.konstantin.api.ServerEvent
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing

//TODO rewrite all client api to builder configuration instead of parameter passing
class KonstantinClient(
    private val host: String,
    private val port: Int,
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
    private val log = logger {}
    private val updateChannel = Channel<Thing<*>>(capacity = Channel.BUFFERED)
    private val subscribeNotifyChannel = Channel<Unit>(capacity = Channel.CONFLATED)
    private val subsciptions = ConcurrentMap<String, Channel<State>>()

    override fun close() {
        httpClient.close()
    }

    fun start(): Unit {
        scope.async {
            while (true) {
                runCatching {
                    clientWork()
                }.onFailure {
                    log.error(it) {
                        "exception while client work"
                    }
                }
                delay(500)
            }

        }
    }

    /**
     * sends async message to server for state updating
     */
    fun <S : State> updateState(thing: Thing<S>) {
        scope.async {
            updateChannel.send(thing)
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


    private suspend fun clientWork() {
        httpClient.webSocket(method = HttpMethod.Get, host = host, port = port, request = request, path = path) {
            async {
                subscribeNotifyChannel.send(Unit)
                for (event in subscribeNotifyChannel) {
                    send(Json.encodeToString(ClientEvent.Subscribe(subsciptions.keys.toList()) as ClientEvent))
                }
            }
            async {
                for (event in updateChannel) {
                    send(Json.encodeToString(ClientEvent.StateUpdate(event.id, event.state) as ClientEvent))
                }
            }
            for (frame in incoming) {
                frame as? Frame.Text ?: continue
                val receivedText = frame.readText()
                val event = json.decodeFromString<ServerEvent>(receivedText)
                when (event) {
                    is ServerEvent.StateUpdate -> subsciptions[event.thingId]?.send(event.state)
                }.exhaustive()
            }
        }
    }

    private fun <T> T.exhaustive(): T {
        return this
    }
}