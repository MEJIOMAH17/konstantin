package org.github.mejiomah17.konstantin.example.configuration

import com.github.mejiomah17.konstantin.configuration.Configuration
import com.github.mejiomah17.konstantin.configuration.ConfigurationProvider
import java.time.Duration
import org.github.mejiomah17.konstantin.api.Thing

class TestConfig : ConfigurationProvider {

    override fun createConfiguration(): Configuration = Configuration {
        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On
        Switch(
            id = "superSwitch",
            receiveState = {
                state = when (state) {
                    Thing.Switch.SwitchState.On -> Thing.Switch.SwitchState.Off
                    Thing.Switch.SwitchState.Off -> Thing.Switch.SwitchState.On
                }
                state
            }.toStateChanelFactory(Duration.ofSeconds(10)),
            updateState = {}
        )
        Switch("afdf", { Thing.Switch.SwitchState.On }.toStateChanelFactory(), {})
    }
}