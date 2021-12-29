package com.github.mejiomah17.common.switch

import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.graphics.colorspace.ColorSpaces
import com.github.mejiomah17.common.KonstantinColors
import com.godaddy.android.colorpicker.ClassicColorPicker
import org.github.mejiomah17.konstantin.api.Thing

@Composable
@ExperimentalGraphicsApi
fun RGBSwitchButton(
    switchState: MutableState<Thing.RGBSwitch.RGBSwitchState>,
    name: String,
    textColor: Color = KonstantinColors.off,
    modifier: Modifier = Modifier
) {
    val uiSwitchState by remember { switchState }
    val iconColor = Color(red = uiSwitchState.red, green = uiSwitchState.green, blue = uiSwitchState.blue)
    Row(modifier) {
        LightButton(
            name = name,
            iconColor = iconColor,
            textColor = textColor,
            onClick = {},
            modifier = Modifier.weight(1f)
        )
        ClassicColorPicker(modifier = Modifier.weight(1f)) {
            val newColor: Color = it.toColor().convert(ColorSpaces.Srgb)
            switchState.value = Thing.RGBSwitch.RGBSwitchState(
                red = (newColor.red * 255).toInt(),
                green = (newColor.green * 255).toInt(),
                blue = (newColor.blue * 255).toInt()
            )
        }
    }
}
