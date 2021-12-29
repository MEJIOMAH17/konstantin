package com.github.mejiomah17.konstantin.backend

import com.github.mejiomah17.konstantin.configuration.Configuration
import com.github.mejiomah17.konstantin.configuration.ThingAdapter
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
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.asCoroutineDispatcher
import kotlinx.coroutines.async
import kotlinx.coroutines.cancel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.Json.Default.configuration
import org.github.mejiomah17.konstantin.api.ClientEvent
import org.github.mejiomah17.konstantin.api.ServerEvent
import org.github.mejiomah17.konstantin.api.State
import org.slf4j.LoggerFactory
import java.io.Closeable
import java.time.Duration
import java.util.concurrent.Executors
import kotlin.coroutines.CoroutineContext

class KonstantinServer private constructor(
    private val configuration: Configuration,
    private val serverStopGracePeriod: Duration = Duration.ofSeconds(1),
    private val serverStopTimeoutMillis: Duration = Duration.ofSeconds(2),
    private val backgroundThreadCount: Int = Runtime.getRuntime().availableProcessors(),
    private val port: Int = 8080,
    /**
     * All clients will receive addition actual state each [clientReconciliationTimeout]
     */
    private val clientReconciliationTimeout: Duration = Duration.ofSeconds(10),
    ktorHook: (Application) -> Unit = {},
    private val automation: Automation = Automation { }
) : Closeable {
    companion object {
        operator fun invoke(
            configuration: Configuration,
            block: KonstantinServerConfiguration.() -> Unit = {}
        ): KonstantinServer {
            val config = KonstantinServerConfiguration(configuration)
            config.block()
            return config.run {
                KonstantinServer(
                    configuration = configuration,
                    serverStopGracePeriod = serverStopGracePeriod,
                    serverStopTimeoutMillis = serverStopTimeoutMillis,
                    backgroundThreadCount = backgroundThreadCount,
                    port = port,
                    clientReconciliationTimeout = clientReconciliationTimeout,
                    ktorHook = ktorHook,
                    automation = automation
                )
            }
        }
    }

    private val backendScope: CoroutineScope = object : CoroutineScope {
        override val coroutineContext: CoroutineContext =
            (SupervisorJob() + Executors.newFixedThreadPool(backgroundThreadCount).asCoroutineDispatcher())
    }

    private val stateManager = StateManagerImpl(
        thingIdToStateUpdater = configuration.things.map { thingAdapter ->
            thingAdapter.id to thingAdapter as ThingAdapter<State>
        }.toMap(),
        coroutineScope = backendScope,
        reconciliationTimeout = clientReconciliationTimeout
    )
    private val log = LoggerFactory.getLogger(this::class.java)

    private val server = embeddedServer(Netty, port = port, host = "0.0.0.0") {
        install(WebSockets)
        routing {
            webSocket("/") {
                log.info("connected ${call.request.host()}")
                val subscriptionManager = SubscriptionManager()
                for (frame in incoming) {
                    frame as? Frame.Text ?: continue
                    val content = frame.readText()
                    val clientEvent: ClientEvent = Json.decodeFromString(content)
                    when (clientEvent) {
                        is ClientEvent.Subscribe -> {
                            log.info("subscribed ${call.request.host()} $content")
                            subscriptionManager.subscribe(clientEvent.thingIds, this@embeddedServer) {
                                val channel = stateManager.subscribe(it)
                                for (state in channel) {
                                    val updateContent =
                                        Json.encodeToString(
                                            ServerEvent.StateUpdate(
                                                thingId = it,
                                                state = state
                                            ) as ServerEvent
                                        )
                                    log.trace("respond $updateContent to ${call.request.host()}")
                                    send(updateContent)
                                }
                            }
                        }
                        is ClientEvent.StateUpdate -> {
                            log.info("update state ${call.request.host()} $content")
                            // TODO check that state is correct
                            async {
                                stateManager.updateState(clientEvent.thingId, clientEvent.state)
                            }
                            Unit
                        }
                    }.exhaustive()
                }
            }
        }
        ktorHook(this@embeddedServer)
    }

    fun start(wait: Boolean = false) {
        startCollectors()
        backendScope.async {
            automation.registerAutomations(object : AutomationContext {
                override val stateManager: StateManager = this@KonstantinServer.stateManager
                override val coroutineScope: CoroutineScope = this@async
            })
        }
        server.start(wait)
    }

    private fun startCollectors() {
        configuration.things.forEach { thing ->
            backendScope.async {
                val stateChannel = thing.stateChannel().invoke(backendScope)
                for (state in stateChannel) {
                    runCatching {
                        stateManager.updateState(thing.id, state)
                    }.onFailure {
                        log.error(it.message)
                    }
                }
            }
        }
    }

    override fun close() {
        backendScope.cancel()
        server.stop(
            gracePeriodMillis = serverStopGracePeriod.toMillis(),
            timeoutMillis = serverStopTimeoutMillis.toMillis()
        )
    }

    private fun <T> T.exhaustive(): T {
        return this
    }
}
