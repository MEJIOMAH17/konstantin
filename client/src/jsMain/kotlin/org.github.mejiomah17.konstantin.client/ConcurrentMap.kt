package org.github.mejiomah17.konstantin.client

//TODO I do not understand js concurrency! Rewrite this code is MUST before release!
internal actual class ConcurrentMap<K : Any, V> : Map<K, V> {
    private val delegate = HashMap<K, V>()
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
        val valueFromMap = delegate[key]
        return if (valueFromMap == null) {
            val newValue = mappingFunction(key)
            delegate[key] = newValue
            newValue
        } else {
            valueFromMap
        }
    }

    actual fun clear() {
        return delegate.clear()
    }

    actual fun remove(key: K): V? {
        return delegate.remove(key)
    }

}