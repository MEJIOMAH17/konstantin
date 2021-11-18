package org.github.mejiomah17.konstantin.api

import Registry
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.mejiomah17.common.state
import com.github.mejiomah17.common.switch.MultiSwitchButton
import com.github.mejiomah17.common.switch.RGBSwitchButton
import com.github.mejiomah17.common.switch.SwitchButton
import kotlinx.coroutines.GlobalScope
import org.github.mejiomah17.konstantin.client.KonstantinClient

val konstantinClient = KonstantinClient(
    host = "127.0.0.1",
    port = 8080
).also {
    it.start()
}

@Composable
fun App() {
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.White)
    ) {
        BoxWithConstraints() {
            Column {
                RGBSwitchButton(
                    switchState = konstantinClient.state(GlobalScope, Registry.rgbSwitch),
                    name = "RGB SWITCH",
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    switchState = konstantinClient.state(GlobalScope, Registry.superSwitch),
                    name = "My switch1",
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    switchState = konstantinClient.state(GlobalScope, Registry.superSwitch2),
                    name = "My switch2kjljlkjlkjlkjlkjlkj",
                    modifier = Modifier.weight(1f)
                )
                MultiSwitchButton(
                    name = "common",
                    switches = konstantinClient.state(GlobalScope, Registry.superSwitch, Registry.superSwitch2),
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}
