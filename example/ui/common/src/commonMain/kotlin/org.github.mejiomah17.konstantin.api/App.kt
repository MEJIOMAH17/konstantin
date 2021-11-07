package org.github.mejiomah17.konstantin.api

import Registry
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.mejiomah17.common.SwitchButton
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
            val boxWithConstraintsScope = this
            println(boxWithConstraintsScope.maxWidth)
            Column {
                SwitchButton(
                    client = konstantinClient,
                    name = "My switch1",
                    switch = Registry.superSwitch,
                    scope = GlobalScope,
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    client = konstantinClient,
                    name = "My switch2kjljlkjlkjlkjlkjlkj",
                    switch = Registry.superSwitch,
                    scope = GlobalScope,
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    client = konstantinClient,
                    name = "My switch3",
                    switch = Registry.superSwitch,
                    scope = GlobalScope,
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    client = konstantinClient,
                    name = "My switch4",
                    switch = Registry.superSwitch,
                    scope = GlobalScope,
                    modifier = Modifier.weight(1f)
                )
                SwitchButton(
                    client = konstantinClient,
                    name = "My switch5",
                    switch = Registry.superSwitch,
                    scope = GlobalScope,
                    modifier = Modifier.weight(1f)
                )
            }
        }
    }
}