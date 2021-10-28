package org.github.mejiomah17.konstantin.configuration

import org.github.mejiomah17.konstantin.api.Thing
import java.time.Duration

class ConfigurationScope {
    internal val things = ArrayList<ThingAdapter<*>>()

    fun add(thing: ThingAdapter<*>) {
        things.add(thing)
    }

    fun Switch(
        id: String,
        receiveState: suspend () -> Thing.Switch.SwitchState,
        updateState: (Thing.Switch.SwitchState) -> Unit,
        stateCollectTimeout: Duration = DefaultCollectTimeout.value
    ) {
        things.add(
            object : ThingAdapter<Thing.Switch.SwitchState> {
                override val id: String = id
                override val collectTimeout: Duration = stateCollectTimeout

                override suspend fun receiveState(): Thing.Switch.SwitchState {
                    return receiveState()
                }

                override suspend fun updateState(state: Thing.Switch.SwitchState) {
                    return updateState(state)
                }
            }
        )
    }
}