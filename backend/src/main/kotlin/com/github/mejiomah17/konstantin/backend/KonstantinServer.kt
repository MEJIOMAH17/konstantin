package com.github.mejiomah17.konstantin.backend

import com.github.mejiomah17.konstantin.configuration.Configuration
import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.application.log
import io.ktor.http.cio.websocket.Frame
import io.ktor.http.cio.websocket.readText
import io.ktor.http.cio.websocket.send
import io.ktor.request.host
import io.ktor.routing.routing
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty
import io.ktor.websocket.WebSockets
import io.ktor.websocket.webSocket
import java.io.Closeable
import java.time.Duration
import java.util.concurrent.Executors
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import org.github.mejiomah17.konstantin.api.Thing
import org.slf4j.LoggerFactory
import kotlin.coroutines.CoroutineContext

class KonstantinServer(
    private val configuration: Configuration,
    private val serverStopGracePeriod: Duration = Duration.ofSeconds(1),
    private val serverStopTimeoutMillis: Duration = Duration.ofSeconds(2),
    private val backgroundThreadCount: Int = Runtime.getRuntime().availableProcessors(),
    ktorHook: (Application) -> Unit = {}
) : Closeable {
    private val backendScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            (SupervisorJob() + Executors.newFixedThreadPool(backgroundThreadCount).asCoroutineDispatcher())
    }
    val stateManager = StateManager()
    val log = LoggerFactory.getLogger(this::class.java)

    private val server = embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        install(WebSockets)
        routing {
            webSocket("/") {
                log.info("connected ${call.request.host()}")
                val subscriptionManager = SubscriptionManager()
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val content = frame.readText()
                    val event: Event = Json.decodeFromString(content)
                    when (event) {
                        is Event.Subscribe -> {
                            log.info("subscribed ${call.request.host()} $content")
                            subscriptionManager.subscribe(event.thingIds, this@embeddedServer) {
                                val channel = stateManager.subscribe(it)
                                for (state in channel) {
                                    val updateContent =
                                        Json.encodeToString(Event.StateUpdate(thingId = it, state = state))
                                    log.trace("respond $updateContent to ${call.request.host()}")
                                    send(updateContent)
                                }
                            }

                        }
                    }
                }
            }
        }
        ktorHook(this@embeddedServer)
    }

    fun start(wait: Boolean = false) {
        startCollectors()
        server.start(wait)
    }


    private fun startCollectors() {
        configuration.things.forEach { thing ->
            backendScope.async {
                while (true) {
                    runCatching {
                        stateManager.updateState(thing.id, thing.receiveState())
                    }.onFailure {
                        log.error(it.message)
                    }
                    delay(thing.collectTimeout.toMillis())
                }
            }
        }
    }

    override fun close() {
        server.stop(
            gracePeriodMillis = serverStopGracePeriod.toMillis(),
            timeoutMillis = serverStopTimeoutMillis.toMillis()
        )
    }
}