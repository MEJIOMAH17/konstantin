package org.github.mejiomah17.konstantin.configuration


class Configuration(
    val things: List<ThingAdapter<*>>
) {
    constructor(block: ConfigurationScope.() -> Unit) : this(
        things = ConfigurationScope().let {
            it.block()
            it.things
        }
    )
}



