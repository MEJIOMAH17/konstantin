package com.github.mejiomah17.konstantin.backend

import kotlinx.coroutines.channels.ReceiveChannel
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing

interface StateManager {
    fun <S : State> subscribe(thing: Thing<S>): ReceiveChannel<S>

    suspend fun <S : State> updateState(thing: Thing<S>, newState: S)
    fun <S : State> getCurrentState(thing: Thing<S>): S
}
