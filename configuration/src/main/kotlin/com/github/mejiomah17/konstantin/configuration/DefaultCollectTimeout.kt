package com.github.mejiomah17.konstantin.configuration

import java.time.Duration

object DefaultCollectTimeout {
    val value: Duration = Duration.ofMillis(500)
}