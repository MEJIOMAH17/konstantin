package com.github.mejiomah17.konstantin.backend

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing



class StateManagerImpl : StateManager {
    private val currentState = ConcurrentHashMap<String, State>()
    private val stateChannels = ConcurrentHashMap<String, BroadcastChannel<State>>()

    override fun <S : State> subscribe(thing: Thing<S>): ReceiveChannel<S> {
        return subscribe(thing.id) as ReceiveChannel<S>
    }

    override suspend fun <S : State> updateState(thing: Thing<S>, newState: S) {
        updateState(thing.id, newState)
    }

    override fun <S : State> getCurrentState(thing: Thing<S>): S {
        return getCurrentState(thing.id) as S
    }


    internal suspend fun updateState(thingId: String, state: State) {
        if (currentState[thingId] != state) {
            currentState[thingId] = state
            val stateChannel = stateChannels.computeIfAbsent(thingId) {
                BroadcastChannel(capacity = Channel.CONFLATED)
            }
            stateChannel.send(state)
        }

    }

    internal fun subscribe(thingId: String): ReceiveChannel<State> {
        return stateChannels.computeIfAbsent(thingId) {
            BroadcastChannel(capacity = Channel.CONFLATED)
        }.openSubscription()
    }

    internal fun getCurrentState(id: String): State? {
        return currentState[id]
    }


}