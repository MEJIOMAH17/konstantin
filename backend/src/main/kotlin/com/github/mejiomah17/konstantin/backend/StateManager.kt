package com.github.mejiomah17.konstantin.backend

import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing
import java.util.concurrent.ConcurrentHashMap

class StateManager {
    private val currentState = ConcurrentHashMap<String, State>()
    private val stateChannels = ConcurrentHashMap<String, BroadcastChannel<State>>()

    suspend fun updateState(thingId: String, state: State) {
        if (currentState[thingId] != state) {
            currentState[thingId] = state
            val stateChannel = stateChannels.computeIfAbsent(thingId) {
                BroadcastChannel(capacity = Channel.CONFLATED)
            }
            stateChannel.send(state)
        }

    }

    fun subscribe(thingId: String): ReceiveChannel<State> {
        return stateChannels.computeIfAbsent(thingId) {
            BroadcastChannel(capacity = Channel.CONFLATED)
        }.openSubscription()
    }

    fun getState(id: String): State? {
        return currentState[id]
    }


}