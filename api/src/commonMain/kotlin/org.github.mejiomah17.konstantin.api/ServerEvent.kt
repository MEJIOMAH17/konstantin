package org.github.mejiomah17.konstantin.api

import kotlinx.serialization.Serializable

/**
 * This events server sends to client
 */
@Serializable
sealed class ServerEvent {

    @Serializable
    class StateUpdate(
        val thingId: String,
        val state: State
    ):ServerEvent()
}