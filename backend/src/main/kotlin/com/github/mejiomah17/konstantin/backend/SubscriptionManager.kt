package com.github.mejiomah17.konstantin.backend

import java.util.concurrent.ConcurrentHashMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async

class SubscriptionManager {
    private val lock = Any()
    private val subsriptions = ConcurrentHashMap<String, Deferred<*>>()
    fun subscribe(thingIds: List<String>, coroutineScope: CoroutineScope, actionForThing: suspend (String) -> Unit) {
        synchronized(lock) {
            cancelNotActiveSubscriptions(thingIds.toSet())
            thingIds.forEach { thingId ->
                subsriptions.computeIfAbsent(thingId) {
                    coroutineScope.async {
                        actionForThing(thingId)
                    }
                }
            }
        }
    }

    private fun cancelNotActiveSubscriptions(thingIds: Set<String>) {
        subsriptions.forEach { key, value ->
            if (key !in thingIds) {
                value.cancel()
            }
        }
    }
}