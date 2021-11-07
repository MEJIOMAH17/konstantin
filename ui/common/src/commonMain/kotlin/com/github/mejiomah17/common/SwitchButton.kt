package com.github.mejiomah17.common

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mejiomah17.konstantin.icons.KonstantinIcons
import com.github.mejiomah17.konstantin.icons.konstantinicons.Lightbulb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient

@Composable
fun SwitchButton(
    client: KonstantinClient,
    switch: Thing.Switch,
    name: String,
    scope: CoroutineScope,
    onColor: Color = KonstantinColors.on,
    offColor: Color = KonstantinColors.off,
    modifier: Modifier = Modifier
) {
    val updateChannel = client.subscribe(switch)
    val switchState = mutableStateOf(switch)
    val uiSwitch: Thing.Switch by remember { switchState }
    Button(
        onClick = {
            client.updateState(uiSwitch.copy(state = uiSwitch.state.invert()))
        },
        modifier = modifier
    ) {
        BoxWithConstraints() {
            val boxWithConstraintsScope = this
            Column(modifier = Modifier.fillMaxSize()) {
                Image(
                    imageVector = KonstantinIcons.Lightbulb,
                    "",
                    colorFilter = when (uiSwitch.state) {
                        Thing.Switch.SwitchState.Off -> ColorFilter.tint(color = onColor)
                        Thing.Switch.SwitchState.On -> ColorFilter.tint(color = offColor)
                    },
                    modifier = Modifier.fillMaxSize(0.80f).align(Alignment.CenterHorizontally)
                )
                val textSize = 20
                if (
                    boxWithConstraintsScope.maxHeight.value * 0.2 > textSize &&
                    boxWithConstraintsScope.maxWidth.value / name.length > textSize
                ) {
                    Text(
                        name,
                        color = offColor,
                        fontSize = textSize.sp,
                        modifier = Modifier.align(Alignment.CenterHorizontally).padding(1.dp)
                    )
                }
            }
        }
    }
    scope.async {
        for (update in updateChannel) {
            switchState.value = uiSwitch.copy(state = update)
        }
    }
}
