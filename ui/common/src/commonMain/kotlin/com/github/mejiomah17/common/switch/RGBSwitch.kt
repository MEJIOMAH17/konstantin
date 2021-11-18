package com.github.mejiomah17.common.switch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.mejiomah17.common.KonstantinColors
import org.github.mejiomah17.konstantin.api.Thing

@Composable
fun RGBSwitchButton(
    switchState: MutableState<Thing.RGBSwitch.RGBSwitchState>,
    name: String,
    textColor: Color = KonstantinColors.off,
    modifier: Modifier = Modifier
) {
    val uiSwitchState by remember { switchState }
    val iconColor = Color(red = uiSwitchState.red, green = uiSwitchState.green, blue = uiSwitchState.blue)
    LightButton(
        name = name,
        iconColor = iconColor,
        textColor = textColor,
        onClick = {},
        modifier = modifier
    )
}

