package com.github.mejiomah17.konstantin.backend

import com.github.mejiomah17.konstantin.configuration.Configuration
import io.kotest.matchers.shouldBe
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withTimeout
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient
import org.junit.jupiter.api.Test
import java.time.Duration

class KonstantinServerTest {

    @Test
    fun `server should notify client after subscribe`(): Unit = runBlocking {

        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On

        val config = Configuration {
            Switch(
                id = "superSwitch",
                stateChannelFactory = {
                    state
                }.toStateChanelFactory(Duration.ofMillis(30)),
                updateState = {
                },
            )
            Switch(
                id = "superSwitch2",
                stateChannelFactory = {
                    state
                }.toStateChanelFactory(Duration.ofMillis(30)),
                updateState = {
                },
            )
        }

        KonstantinServer(config).use {
            it.start(wait = false)
            KonstantinClient(
                host = "127.0.0.1",
                port = 8080
            ).use {
                it.start()
                val channel = it.subscribe(Thing.Switch("superSwitch"))
                val channel2 = it.subscribe(Thing.Switch("superSwitch2"))
                withTimeout(1000) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.On
                    channel2.receive() shouldBe Thing.Switch.SwitchState.On
                }
                state = Thing.Switch.SwitchState.Off
                withTimeout(100) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.Off
                    channel2.receive() shouldBe Thing.Switch.SwitchState.Off
                }

                state = Thing.Switch.SwitchState.On
                withTimeout(100) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.On
                    channel2.receive() shouldBe Thing.Switch.SwitchState.On
                }
            }
        }
    }

    @Test
    fun `server should not notify client after unsubscribe`(): Unit = runBlocking {

        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On

        val config = Configuration {
            Switch(
                id = "superSwitch",
                stateChannelFactory = {
                    state
                }.toStateChanelFactory(Duration.ofMillis(30)),
                updateState = {
                },
            )
        }

        KonstantinServer(config).use {
            it.start(wait = false)
            KonstantinClient(
                host = "127.0.0.1",
                port = 8080
            ).use {
                it.start()
                val channel = it.subscribe(Thing.Switch("superSwitch"))
                withTimeout(1000) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.On
                }
                state = Thing.Switch.SwitchState.Off
                withTimeout(100) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.Off
                }
                it.unsubscribe("superSwitch")
                state = Thing.Switch.SwitchState.On
                kotlin.runCatching {
                    withTimeout(1000) {
                        while (!channel.isClosedForReceive) {
                            // do nothing
                        }
                    }
                }
            }
        }
    }

    @Test
    fun `server should call update state when client updates state`(): Unit = runBlocking {

        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On

        val config = Configuration {
            Switch(
                id = "superSwitch",
                stateChannelFactory = {
                    state
                }.toStateChanelFactory(Duration.ofMillis(30)),
                updateState = {
                    state = it
                },
            )
        }

        KonstantinServer(config).use {
            it.start(wait = false)
            KonstantinClient(
                host = "127.0.0.1",
                port = 8080
            ).use {
                it.start()
                val channel = it.subscribe(Thing.Switch("superSwitch"))
                withTimeout(1000) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.On
                }
                it.updateState(Thing.Switch("superSwitch", Thing.Switch.SwitchState.Off))
                withTimeout(100) {
                    channel.receive() shouldBe Thing.Switch.SwitchState.Off
                }
            }
        }
    }
}
