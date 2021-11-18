package com.github.mejiomah17.common

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.github.konstantin.concurrentmap.ConcurrentMap
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient

internal object MutableStateHolder {
    val map: ConcurrentMap<String, MutableState<out State>> = ConcurrentMap()
}

fun <S : State> KonstantinClient.state(scope: CoroutineScope, thing: Thing<S>): MutableState<S> {
    val client = this
    val state = MutableStateHolder.map.computeIfAbsent(thing.id) {
        val updateChannel = client.subscribe(thing)
        val result = mutableStateOf(thing.state)
        scope.async {
            for (update in updateChannel) {
                result.value = update
            }
        }
        result
    } as MutableState<S>

    return object : MutableState<S> {
        override var value: S
            get() = state.value
            set(value) {
                client.updateState(thing.id, value)
                state.value = value
            }

        override fun component1(): S {
            return state.component1()
        }

        override fun component2(): (S) -> Unit {
            return state.component2()
        }

    }
}


fun <S : State> KonstantinClient.state(scope: CoroutineScope, vararg things: Thing<S>): List<MutableState<S>> {
    return things.map { state(scope, it) }
}