package org.github.mejiomah17.konstantin.client

internal expect class ConcurrentMap<K : Any, V>() : Map<K, V> {
    fun computeIfAbsent(key: K, mappingFunction: (K) -> V): V
    fun clear(): Unit
    fun remove(key: K): V?
}