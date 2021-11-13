package com.github.mejiomah17.common.switch

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mejiomah17.common.KonstantinColors
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
    val switchState: MutableState<Thing.Switch.SwitchState> = mutableStateOf(switch.state)

    SwitchButton(
        name = name,
        onColor = onColor,
        offColor = offColor,
        switchState = switchState,
        onClick = { client.updateState(switch.copy(state = switchState.value.invert())) },
        modifier = modifier
    )
    scope.async {
        for (update in updateChannel) {
            switchState.value = update
        }
    }
}

@Composable
internal fun SwitchButton(
    name: String,
    onColor: Color,
    offColor: Color,
    switchState: MutableState<Thing.Switch.SwitchState>,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val uiSwitch: Thing.Switch.SwitchState by remember { switchState }
    Button(
        onClick = onClick,
        modifier = modifier
    ) {
        BoxWithConstraints {
            val boxWithConstraintsScope = this
            Column(modifier = Modifier.fillMaxSize()) {
                val imageFraction = 0.80f
                Image(
                    imageVector = KonstantinIcons.Lightbulb,
                    "",
                    colorFilter = when (uiSwitch) {
                        Thing.Switch.SwitchState.Off -> ColorFilter.tint(color = offColor)
                        Thing.Switch.SwitchState.On -> ColorFilter.tint(color = onColor)
                    },
                    modifier = Modifier.fillMaxSize(imageFraction).align(Alignment.CenterHorizontally)
                )
                val textSize = (boxWithConstraintsScope.maxHeight * ((1 - imageFraction) / 2.5f)).value
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
