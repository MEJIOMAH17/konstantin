package com.github.mejiomah17.konstantin.example.server

import com.github.mejiomah17.konstantin.backend.Automation
import com.github.mejiomah17.konstantin.backend.KonstantinServer
import kotlinx.coroutines.async
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.example.configuration.Registry
import org.github.mejiomah17.konstantin.example.configuration.TestConfig

fun main() {
    KonstantinServer(TestConfig().createConfiguration()) {
        automation = Automation {
            it.coroutineScope.async {
                for (switchUpdate in it.stateManager.subscribe(Registry.superSwitch)) {
                    when (switchUpdate) {
                        Thing.Switch.SwitchState.On -> println("On")
                        Thing.Switch.SwitchState.Off -> println("Off")
                    }
                }
            }
        }
    }.start(wait = true)
}