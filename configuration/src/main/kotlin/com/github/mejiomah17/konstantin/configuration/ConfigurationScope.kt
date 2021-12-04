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

    fun <S : State> registerThing(
        id: String,
        stateChannelFactory: StateChannelFactory<S>,
        updateState: suspend (S) -> Unit,
        defaultState: S,
    ) {
        things.add(
            ThingAdapterImpl(
                id = id,
                stateChannelFactory = stateChannelFactory,
                updateState = updateState,
                defaultState = defaultState
            )
        )

    }

    fun Switch(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.Switch.SwitchState>,
        updateState: suspend (Thing.Switch.SwitchState) -> Unit,
        defaultState: Thing.Switch.SwitchState = Thing.Switch.SwitchState.Off
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun RGBSwitch(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.RGBSwitch.RGBSwitchState>,
        updateState: suspend (Thing.RGBSwitch.RGBSwitchState) -> Unit,
        defaultState: Thing.RGBSwitch.RGBSwitchState = Thing.RGBSwitch.RGBSwitchState(0, 0, 0)
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun BooleanSensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.BooleanSensor.BooleanSensorState>,
        updateState: suspend (Thing.BooleanSensor.BooleanSensorState) -> Unit = {},
        defaultState: Thing.BooleanSensor.BooleanSensorState = Thing.BooleanSensor.BooleanSensorState.True
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun NumericSensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.NumericSensor.NumericState>,
        updateState: suspend (Thing.NumericSensor.NumericState) -> Unit = {},
        defaultState: Thing.NumericSensor.NumericState = Thing.NumericSensor.NumericState(0)
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }


    fun <S : State> (() -> S).toStateChanelFactory(
        stateCollectTimeout: Duration = DefaultCollectTimeout.value
    ): StateChannelFactory<S> {
        return suspend {
            this()
        }.toStateChanelFactory(stateCollectTimeout = stateCollectTimeout)
    }

    fun <S : State> (suspend () -> S).toStateChanelFactory(
        stateCollectTimeout: Duration = DefaultCollectTimeout.value
    ): StateChannelFactory<S> {
        val function = this
        return object : StateChannelFactory<S> {
            override fun invoke(scope: CoroutineScope): ReceiveChannel<S> {
                val channel = Channel<S>(capacity = Channel.CONFLATED)
                scope.launch {
                    while (!channel.isClosedForSend) {
                        runCatching {
                            channel.send(function.invoke())
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