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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.mejiomah17.common.KonstantinColors
import com.github.mejiomah17.konstantin.icons.KonstantinIcons
import com.github.mejiomah17.konstantin.icons.konstantinicons.Lightbulb
import org.github.mejiomah17.konstantin.api.Thing

@Composable
fun SwitchButton(
    name: String,
    onColor: Color = KonstantinColors.on,
    offColor: Color = KonstantinColors.off,
    switchState: MutableState<Thing.Switch.SwitchState>,
    modifier: Modifier = Modifier
) {

    SwitchButton(
        name = name,
        onColor = onColor,
        offColor = offColor,
        switchState = switchState.value,
        onClick = { switchState.value = switchState.value.invert() },
        modifier = modifier
    )
}

@Composable
internal fun SwitchButton(
    name: String,
    onColor: Color,
    offColor: Color,
    switchState: Thing.Switch.SwitchState,
    onClick: () -> Unit,
    modifier: Modifier
) {
    LightButton(
        name = name,
        iconColor = when (switchState) {
            Thing.Switch.SwitchState.Off -> offColor
            Thing.Switch.SwitchState.On -> onColor
        },
        textColor = offColor,
        onClick = onClick,
        modifier = modifier,
    )
}

@Composable
internal fun LightButton(
    name: String,
    iconColor: Color,
    textColor: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
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
                    colorFilter = ColorFilter.tint(iconColor),
                    modifier = Modifier.fillMaxSize(imageFraction).align(Alignment.CenterHorizontally)
                )
                val textSize = (boxWithConstraintsScope.maxHeight * ((1 - imageFraction) / 2.5f)).value
                Text(
                    name,
                    color = textColor,
                    fontSize = textSize.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally).padding(1.dp)
                )
            }
        }
    }
}
