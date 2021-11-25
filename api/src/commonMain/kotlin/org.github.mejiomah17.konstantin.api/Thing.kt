package org.github.mejiomah17.konstantin.api

import kotlinx.serialization.Serializable

@Serializable
sealed class State

@Serializable
sealed class Thing<T : State> {
    //TODO description?
    abstract val id: String
    abstract val state: T

    @Serializable
    data class Switch(
        override val id: String,
        override val state: SwitchState = SwitchState.Off,
    ) : Thing<Switch.SwitchState>() {

        @Serializable
        sealed class SwitchState : State() {
            abstract fun invert(): SwitchState

            @Serializable
            object On : SwitchState() {
                override fun invert() = Off
            }

            @Serializable
            object Off : SwitchState() {
                override fun invert() = On
            }
        }
    }

    @Serializable
    data class RGBSwitch(
        override val id: String,
        override val state: RGBSwitchState = RGBSwitchState(0, 0, 0),
    ) : Thing<RGBSwitch.RGBSwitchState>() {

        @Serializable
        data class RGBSwitchState(
            val red: Int,
            val green: Int,
            val blue: Int
        ) : State()
    }

    @Serializable
    data class MotionSensor(
        override val id: String,
        override val state: MotionSensorState
    ) : Thing<MotionSensor.MotionSensorState>() {

        @Serializable
        sealed class MotionSensorState : State() {
            @Serializable
            object MotionDetected : MotionSensorState()

            @Serializable
            object MotionIsNotDetected : MotionSensorState()
        }
    }

    @Serializable
    data class TemperatureSensor(
        override val id: String,
        override val state: TemperatureState
    ) : Thing<TemperatureSensor.TemperatureState>() {
        @Serializable
        data class TemperatureState(val value: Double) : State() {
            constructor(value: Int) : this(value.toDouble())
        }
    }

    @Serializable
    data class HumiditySensor(
        override val id: String,
        override val state: HumidityState
    ) : Thing<HumiditySensor.HumidityState>() {
        @Serializable
        data class HumidityState(
            /**
             * percent value from 0 to 100
             */
            val value: Double
        ) : State() {
            constructor(value: Int) : this(value.toDouble())
        }
    }

    @Serializable
    data class LightLevelSensor(
        override val id: String,
        override val state: LightLevelState
    ) : Thing<LightLevelSensor.LightLevelState>() {
        @Serializable
        data class LightLevelState(
            val value: Double
        ) : State() {
            constructor(value: Int) : this(value.toDouble())
        }
    }

    @Serializable
    data class CO2Sensor(
        override val id: String,
        override val state: CO2State
    ) : Thing<CO2Sensor.CO2State>() {
        @Serializable
        data class CO2State(
            val value: Double
        ) : State() {
            constructor(value: Int) : this(value.toDouble())
        }
    }

}
