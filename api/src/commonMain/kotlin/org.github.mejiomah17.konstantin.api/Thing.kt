package org.github.mejiomah17.konstantin.api

import kotlinx.serialization.Serializable

@Serializable
sealed class State
// TODO add generic thing with string state
@Serializable
sealed class Thing<T : State> {
    // TODO description?
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
    data class BooleanSensor(
        override val id: String,
        override val state: BooleanSensorState
    ) : Thing<BooleanSensor.BooleanSensorState>() {

        @Serializable
        sealed class BooleanSensorState : State() {
            @Serializable
            object False : BooleanSensorState()

            @Serializable
            object True : BooleanSensorState()
        }
    }

    @Serializable
    data class NumericSensor(
        override val id: String,
        override val state: NumericState
    ) : Thing<NumericSensor.NumericState>() {
        @Serializable
        data class NumericState(val value: Double) : State() {
            constructor(value: Int) : this(value.toDouble())
        }
    }
}
