package com.github.mejiomah17.konstantin.backend

import com.github.mejiomah17.konstantin.configuration.Configuration
import io.ktor.application.Application
import java.time.Duration

class KonstantinServerConfiguration(
    val configuration: Configuration
) {
    var serverStopGracePeriod: Duration = Duration.ofSeconds(1)
    var serverStopTimeoutMillis: Duration = Duration.ofSeconds(2)
    var backgroundThreadCount: Int = Runtime.getRuntime().availableProcessors()
    var port: Int = 8080

    /**
     * All clients will receive addition actual state each [clientReconciliationTimeout]
     */
    var clientReconciliationTimeout: Duration = Duration.ofSeconds(10)
    var ktorHook: (Application) -> Unit = {}
    var automation: Automation = Automation { }
}
