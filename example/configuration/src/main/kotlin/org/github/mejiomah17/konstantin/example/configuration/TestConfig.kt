package org.github.mejiomah17.konstantin.example.configuration

import com.github.mejiomah17.konstantin.configuration.Configuration
import com.github.mejiomah17.konstantin.configuration.ConfigurationProvider
import java.time.Duration
import org.github.mejiomah17.konstantin.api.Thing

class TestConfig : ConfigurationProvider {

    override fun createConfiguration(): Configuration = Configuration {
        var state: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On
        var state2: Thing.Switch.SwitchState = Thing.Switch.SwitchState.On
        Switch(
            id = "superSwitch",
            receiveState = {
                state = state.invert()
                state
            }.toStateChanelFactory(Duration.ofSeconds(1000)),
            updateState = {}
        )
        Switch(
            id = "superSwitch2",
            receiveState = {
                state = state.invert()
                state
            }.toStateChanelFactory(Duration.ofSeconds(1000)),
            updateState = {}
        )
        RGBSwitch(
            id = "rgbSwitch",
            receiveState = {
                Thing.RGBSwitch.RGBSwitchState(255, 200, 255)
            }.toStateChanelFactory(),
            updateState = {

            }
        )
    }
}