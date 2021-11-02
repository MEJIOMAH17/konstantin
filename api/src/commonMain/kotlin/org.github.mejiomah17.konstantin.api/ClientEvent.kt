package org.github.mejiomah17.konstantin.api

import kotlinx.serialization.Serializable

/**
 * This events client sends to server
 */
@Serializable
sealed class ClientEvent {

    @Serializable
    class Subscribe(
        val thingIds: List<String>
    ) : ClientEvent()

    @Serializable
    class StateUpdate(
        val thingId: String,
        val state: State
    ): ClientEvent()
}