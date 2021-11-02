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
        override val state: SwitchState = SwitchState.On,
    ) : Thing<Switch.SwitchState>() {
        @Serializable
        sealed class SwitchState : State() {
            abstract fun invert(): SwitchState

            @Serializable
            object On : SwitchState() {
                override fun invert()  = Off
            }

            @Serializable
            object Off : SwitchState(){
                override fun invert()  = On
            }
        }
    }
}
