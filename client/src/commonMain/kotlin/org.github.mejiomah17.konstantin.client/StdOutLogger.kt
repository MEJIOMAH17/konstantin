package org.github.mejiomah17.konstantin.client

internal object StdOutLogger : Logger {
    override fun error(message: String) {
        println(message)
    }
}