package com.github.mejiomah17.konstantin.example.server

import com.github.mejiomah17.konstantin.backend.KonstantinServer
import org.github.mejiomah17.konstantin.example.configuration.TestConfig

fun main() {
    KonstantinServer(
        configuration = TestConfig().createConfiguration()
    ).start(wait = true)
}