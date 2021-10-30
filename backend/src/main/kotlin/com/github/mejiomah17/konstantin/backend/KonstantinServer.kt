package com.github.mejiomah17.konstantin.backend

import io.ktor.application.*
import io.ktor.http.cio.websocket.*
import io.ktor.request.*
import io.ktor.routing.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.websocket.*
import kotlinx.coroutines.*
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import com.github.mejiomah17.konstantin.configuration.Configuration
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.time.Duration
import java.util.concurrent.Executors
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
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val content = frame.readText()
                    val event: Event = Json.decodeFromString(content)
                    when (event) {
                        is Event.Subscribe -> {
                            log.info("subscribed ${call.request.host()} $content")
                            event.thingIds.forEach {
                                val channel = stateManager.subscribe(it)
                                for (thing in channel) {
                                    send(Json.encodeToString(thing))
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