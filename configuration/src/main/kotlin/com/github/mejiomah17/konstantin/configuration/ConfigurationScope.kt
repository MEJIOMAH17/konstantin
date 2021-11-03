package com.github.mejiomah17.konstantin.configuration

import java.time.Duration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import mu.KotlinLogging.logger
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing

class ConfigurationScope {
    internal val things = ArrayList<ThingAdapter<*>>()
    private val log = logger {}
    fun add(thing: ThingAdapter<*>) {
        things.add(thing)
    }

    fun Switch(
        id: String,
        receiveState: StateChannelFactory<Thing.Switch.SwitchState>,
        updateState: suspend (Thing.Switch.SwitchState) -> Unit,
    ) {
        things.add(
            object : ThingAdapter<Thing.Switch.SwitchState> {
                override val id: String = id

                override suspend fun updateState(state: Thing.Switch.SwitchState) {
                    return updateState(state)
                }

                override suspend fun stateChannel(): StateChannelFactory<Thing.Switch.SwitchState> {
                    return receiveState
                }
            }
        )
    }

    fun <S : State> (() -> S).toStateChanelFactory(
        stateCollectTimeout: Duration = DefaultCollectTimeout.value
    ): StateChannelFactory<S> {
        val function = this
        return object : StateChannelFactory<S> {
            override fun invoke(scope: CoroutineScope): ReceiveChannel<S> {
                val channel = Channel<S>(capacity = Channel.CONFLATED)
                scope.launch {
                    while (!channel.isClosedForSend) {
                        runCatching {
                           channel.send( function.invoke())
                        }.onFailure {
                            log.error(it) {
                                "Exception while receiving state"
                            }
                        }
                        delay(stateCollectTimeout.toMillis())
                    }
                    log.info {
                        "channel was closed"
                    }
                }
                return channel
            }
        }
    }
}