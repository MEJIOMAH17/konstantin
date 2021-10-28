package org.github.mejiomah17.konstantin.example.configuration

import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.configuration.Configuration
import org.github.mejiomah17.konstantin.configuration.ConfigurationProvider

class TestConfig : ConfigurationProvider {

    override fun createConfiguration(): Configuration = Configuration {
        Switch("adf", { Thing.Switch.SwitchState.On }, {})
    }
}