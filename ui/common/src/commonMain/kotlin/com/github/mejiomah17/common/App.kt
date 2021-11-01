package com.github.mejiomah17.common

import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.*
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import org.github.mejiomah17.konstantin.api.Thing
import org.github.mejiomah17.konstantin.client.KonstantinClient

val client = KonstantinClient(
    host = "127.0.0.1",
    port = 8080,
)

suspend fun listen(thingState: MutableState<Thing.Switch>) {
    client.use {
        client.start()
        val updateChannel = client.subscribe(thingState.value)
        for (update in updateChannel) {
            thingState.value = thingState.value.copy(state = update)
        }
    }
}


@Composable
fun App() {
    val switchState = mutableStateOf(Registry.superSwitch)

    val switch by remember {
        switchState
    }
    Button(onClick = {
    }) {
        Text("$switch")
    }

    GlobalScope.launch {
        kotlin.runCatching {
            listen(switchState)
        }.onFailure {
            println("scope" + it)
        }
    }
}
