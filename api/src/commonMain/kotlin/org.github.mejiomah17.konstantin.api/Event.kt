package org.github.mejiomah17.konstantin.api

import kotlinx.serialization.Serializable

@Serializable
sealed class Event {

    @Serializable
    class Subscribe(
        val thingIds: List<String>
    ):Event()
}