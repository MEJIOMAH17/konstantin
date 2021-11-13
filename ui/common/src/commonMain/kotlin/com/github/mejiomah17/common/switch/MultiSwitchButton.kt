package com.github.mejiomah17.common.switch

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
import com.github.mejiomah17.common.KonstantinColors
import com.github.mejiomah17.konstantin.icons.KonstantinIcons
import com.github.mejiomah17.konstantin.icons.konstantinicons.Lightbulb
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient

/**
 * Shows On when any of switches is On. Shows Off when all switches off.
 */
@Composable
fun MultiSwitchButton(
    client: KonstantinClient,
    switches: List<Thing.Switch>,
    name: String,
    scope: CoroutineScope,
    onColor: Color = KonstantinColors.on,
    offColor: Color = KonstantinColors.off,
    modifier: Modifier = Modifier
) {
    val switchState = MultiSwitchState(
        switches = switches,
        client = client,
        scope = scope
    )

    val uiSwitchState: Thing.Switch.SwitchState by remember { switchState.uiState }

    SwitchButton(
        name = name,
        onColor = onColor,
        offColor = offColor,
        switchState = switchState.uiState,
        onClick = {
            val newState = uiSwitchState.invert()
            switches.forEach {
                client.updateState(it.copy(state = newState))
            }
        },
        modifier = modifier
    )
    switchState.run()
}

private class MultiSwitchState(
    private val switches: List<Thing.Switch>,
    private val client: KonstantinClient,
    private val scope: CoroutineScope,
) {
    val uiState = mutableStateOf(switches.map { it.state }.computeState())
    private val updateChannel = Channel<Pair<String, Thing.Switch.SwitchState>>(capacity = Channel.BUFFERED)
    private val updateChannels = client.subscribe(switches)

    fun run() {
        updateChannels.forEach { entry ->
            val thing = entry.key
            val stateChannel = entry.value
            scope.async {
                for (state in stateChannel) {
                    updateChannel.send(thing.id to state)
                }
            }
        }
        scope.async {
            val currentState = HashMap<String, Thing.Switch.SwitchState>()
            for ((id, state) in updateChannel) {
                currentState[id] = state
                uiState.value = currentState.values.computeState()
            }
        }
    }

    private fun Collection<Thing.Switch.SwitchState>.computeState(): Thing.Switch.SwitchState {
        return if (any { it == Thing.Switch.SwitchState.On }) {
            Thing.Switch.SwitchState.On
        } else {
            Thing.Switch.SwitchState.Off
        }
    }
}
