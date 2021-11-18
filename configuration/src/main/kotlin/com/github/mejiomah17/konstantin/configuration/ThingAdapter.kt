package com.github.mejiomah17.konstantin.configuration

import org.github.mejiomah17.konstantin.api.State

interface ThingAdapter<S : State> {
    val id: String
    val defaultState: S

    /**
     * This channel should send state of thing
     */
    suspend fun stateChannel(): StateChannelFactory<S>
    suspend fun updateState(state: S)
}