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

    fun MotionSensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.MotionSensor.MotionSensorState>,
        updateState: suspend (Thing.MotionSensor.MotionSensorState) -> Unit,
        defaultState: Thing.MotionSensor.MotionSensorState = Thing.MotionSensor.MotionSensorState.MotionIsNotDetected
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun TemperatureSensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.TemperatureSensor.TemperatureState>,
        updateState: suspend (Thing.TemperatureSensor.TemperatureState) -> Unit,
        defaultState: Thing.TemperatureSensor.TemperatureState = Thing.TemperatureSensor.TemperatureState(0)
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun HumiditySensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.HumiditySensor.HumidityState>,
        updateState: suspend (Thing.HumiditySensor.HumidityState) -> Unit,
        defaultState: Thing.HumiditySensor.HumidityState = Thing.HumiditySensor.HumidityState(0)
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun LightLevelSensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.LightLevelSensor.LightLevelState>,
        updateState: suspend (Thing.LightLevelSensor.LightLevelState) -> Unit,
        defaultState: Thing.LightLevelSensor.LightLevelState = Thing.LightLevelSensor.LightLevelState(0)
    ) {
        registerThing(
            id = id,
            stateChannelFactory = stateChannelFactory,
            updateState = updateState,
            defaultState = defaultState
        )
    }

    fun CO2Sensor(
        id: String,
        stateChannelFactory: StateChannelFactory<Thing.CO2Sensor.CO2State>,
        updateState: suspend (Thing.CO2Sensor.CO2State) -> Unit,
        defaultState: Thing.CO2Sensor.CO2State = Thing.CO2Sensor.CO2State(0)
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