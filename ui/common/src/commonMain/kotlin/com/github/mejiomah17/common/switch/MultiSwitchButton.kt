package com.github.mejiomah17.common.switch

import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.github.mejiomah17.common.KonstantinColors
import org.github.mejiomah17.konstantin.api.Thing

/**
 * Shows On when any of switches is On. Shows Off when all switches off.
 */
@Composable
fun MultiSwitchButton(
    switches: List<MutableState<Thing.Switch.SwitchState>>,
    name: String,
    onColor: Color = KonstantinColors.on,
    offColor: Color = KonstantinColors.off,
    modifier: Modifier = Modifier
) {
    SwitchButton(
        name = name,
        onColor = onColor,
        offColor = offColor,
        switchState = switches.map { it.value }.computeState(),
        onClick = {
            val newState = switches.map { it.value }.computeState().invert()
            switches.forEach {
                it.value = newState
            }
        },
        modifier = modifier
    )
}
//
//
// @Composable
// fun MultiSwitchButton(
//    client: KonstantinClient,
//    switches: List<Thing.Switch>,
//    name: String,
//    scope: CoroutineScope,
//    onColor: Color = KonstantinColors.on,
//    offColor: Color = KonstantinColors.off,
//    modifier: Modifier = Modifier
// ) {
//    val switchState = MultiSwitchState(
//        switches = switches,
//        client = client,
//        scope = scope
//    )
//
//    val uiSwitchState: Thing.Switch.SwitchState by remember { switchState.uiState }
//
//    SwitchButton(
//        name = name,
//        onColor = onColor,
//        offColor = offColor,
//        switchState = switchState.uiState,
//        onClick = {
//            val newState = uiSwitchState.invert()
//            switches.forEach {
//                client.updateState(it.copy(state = newState))
//            }
//        },
//        modifier = modifier
//    )
//    switchState.run()
// }

// private class MultiSwitchState(
//    private val switches: List<Thing.Switch>,
//    private val client: KonstantinClient,
//    private val scope: CoroutineScope,
// ) {
//    val uiState = mutableStateOf(switches.map { it.state }.computeState())
//    private val updateChannel = Channel<Pair<String, Thing.Switch.SwitchState>>(capacity = Channel.BUFFERED)
//    private val updateChannels = client.subscribe(switches)
//
//    fun run() {
//        updateChannels.forEach { entry ->
//            val thing = entry.key
//            val stateChannel = entry.value
//            scope.async {
//                for (state in stateChannel) {
//                    updateChannel.send(thing.id to state)
//                }
//            }
//        }
//        scope.async {
//            val currentState = HashMap<String, Thing.Switch.SwitchState>()
//            for ((id, state) in updateChannel) {
//                currentState[id] = state
//                uiState.value = currentState.values.computeState()
//            }
//        }
//    }
//
//
// }

private fun Collection<Thing.Switch.SwitchState>.computeState(): Thing.Switch.SwitchState {
    return if (any { it == Thing.Switch.SwitchState.On }) {
        Thing.Switch.SwitchState.On
    } else {
        Thing.Switch.SwitchState.Off
    }
}
