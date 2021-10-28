package org.github.mejiomah17.konstantin.configuration

import org.github.mejiomah17.konstantin.api.State
import java.time.Duration

interface ThingAdapter<S : State> {
    val id: String
    val collectTimeout: Duration
        get() = DefaultCollectTimeout.value

    suspend fun receiveState(): S
    suspend fun updateState(state: S)
}