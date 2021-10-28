package com.github.mejiomah17.konstantin.backend

import io.kotest.matchers.shouldBe
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.configuration.Configuration
import org.junit.jupiter.api.Test
import java.time.Duration
import java.util.concurrent.atomic.AtomicReference

class KonstantinServerTest {

    @Test
    fun `server should notify client after subscribe`(): Unit = runBlocking {

        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On

        val config = Configuration {
            Switch(
                id = "superSwitch",
                receiveState = {
                    state
                },
                updateState = {

                },
                stateCollectTimeout = Duration.ofMillis(30)
            )
        }

        KonstantinServer(config).use {
            it.start(wait = false)
            HttpClient {
                install(WebSockets)
            }.use { client ->
                val json = Json {
                    ignoreUnknownKeys = true
                }
                val switchReference = Channel<Thing.Switch.SwitchState>()
                GlobalScope.async {
                    client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/") {
                        send(Json.encodeToString(Event.Subscribe(listOf("superSwitch")) as Event))
                        for (frame in incoming) {
                            frame as? Frame.Text ?: continue
                            val receivedText = frame.readText()
                            val decodeFromString = json.decodeFromString<Thing.Switch.SwitchState>(receivedText)
                            switchReference.send(decodeFromString)
                        }
                    }
                    print("dasf")
                }
                withTimeout(1000) {
                    switchReference.receive() shouldBe Thing.Switch.SwitchState.On
                }
                state = Thing.Switch.SwitchState.Off
                withTimeout(1000) {
                    switchReference.receive() shouldBe Thing.Switch.SwitchState.Off
                }

                state = Thing.Switch.SwitchState.On
                withTimeout(1000) {
                    switchReference.receive() shouldBe Thing.Switch.SwitchState.On
                }
            }
        }
    }
}