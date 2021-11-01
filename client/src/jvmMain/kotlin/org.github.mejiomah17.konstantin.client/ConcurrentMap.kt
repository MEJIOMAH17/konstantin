package org.github.mejiomah17.konstantin.client

import java.util.concurrent.ConcurrentHashMap

internal actual class ConcurrentMap<K : Any, V> : Map<K, V> {
    private val delegate = ConcurrentHashMap<K, V>()
    override val entries: Set<Map.Entry<K, V>> = delegate.entries
    override val keys: Set<K> = delegate.keys
    override val size: Int = delegate.size
    override val values: Collection<V> = delegate.values

    override fun containsKey(key: K): Boolean {
        return delegate.containsKey(key)
    }

    override fun containsValue(value: V): Boolean {
        return delegate.containsValue(value)
    }

    override fun get(key: K): V? {
        return delegate.get(key)
    }

    override fun isEmpty(): Boolean {
        return delegate.isEmpty()
    }

    actual fun computeIfAbsent(key: K, mappingFunction: (K) -> V): V {
        return delegate.computeIfAbsent(key, mappingFunction)
    }

    actual fun clear() {
        return delegate.clear()
    }

    actual fun remove(key: K): V? {
        return delegate.remove(key)
    }

}