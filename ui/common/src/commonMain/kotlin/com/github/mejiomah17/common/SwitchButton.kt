package com.github.mejiomah17.common

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient

@Composable
fun SwitchButton(
    client: KonstantinClient,
    switch: Thing.Switch,
    scope: CoroutineScope,
) {
    val updateChannel = client.subscribe(switch)
    val switchState = mutableStateOf(switch)
    val uiSwitch by remember { switchState }
    Button(onClick = {
        client.updateState(uiSwitch.copy(state = uiSwitch.state.invert()))
    }) {
        Text("${uiSwitch.state}")
    }
    scope.async {
        for (update in updateChannel) {
            switchState.value = uiSwitch.copy(state = update)
        }
    }
}
