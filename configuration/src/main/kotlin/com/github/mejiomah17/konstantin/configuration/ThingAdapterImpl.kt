package com.github.mejiomah17.konstantin.configuration

import org.github.mejiomah17.konstantin.api.State

internal class ThingAdapterImpl<S : State>(
    override val id: String,
    override val defaultState: S,
    private val stateChannelFactory: StateChannelFactory<S>,
    private val updateState: suspend (S) -> Unit,
) : ThingAdapter<S> {

    override suspend fun stateChannel(): StateChannelFactory<S> {
        return stateChannelFactory
    }

    override suspend fun updateState(state: S) {
        return updateState.invoke(state)
    }
}
