package com.github.mejiomah17.konstantin.backend

import com.github.mejiomah17.konstantin.configuration.ThingAdapter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.BroadcastChannel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.ReceiveChannel
import kotlinx.coroutines.delay
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing
import org.slf4j.LoggerFactory
import java.time.Duration
import java.util.concurrent.ConcurrentHashMap

internal class StateManagerImpl(
    private val thingIdToStateUpdater: Map<String, ThingAdapter<State>>,
    private val coroutineScope: CoroutineScope,
    private val reconciliationTimeout: Duration
) : StateManager {
    private val currentState = ConcurrentHashMap<String, State>()
    private val stateChannels = ConcurrentHashMap<String, BroadcastChannel<State>>()
    private val log = LoggerFactory.getLogger(this::class.java)

    init {
        coroutineScope.async {
            while (true) {
                for ((id, state) in currentState) {
                    stateChannels[id]?.send(state)
                }
                delay(reconciliationTimeout.toMillis())
            }
        }
    }

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
            coroutineScope.async {
                runCatching {
                    thingIdToStateUpdater[thingId]?.updateState(state)
                }.onFailure {
                    log.error(it.toString())
                }
            }
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
