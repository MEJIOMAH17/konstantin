package org.github.mejiomah17.common

import androidx.compose.material.Text
import androidx.compose.material.Button
import androidx.compose.runtime.*
import io.ktor.client.*
import io.ktor.client.features.websocket.*
import io.ktor.http.*
import io.ktor.http.cio.websocket.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.github.mejiomah17.konstantin.api.Event
import org.github.mejiomah17.konstantin.api.State
import org.github.mejiomah17.konstantin.api.Thing
import java.util.concurrent.atomic.AtomicBoolean

val client = HttpClient {
    install(WebSockets)
}

val flag: AtomicBoolean = AtomicBoolean()

private val json = Json {
    ignoreUnknownKeys = true
}

suspend fun listen(thingState: MutableState<Thing.Switch>) {
    if (!flag.getAndSet(true)) {
        while (true) {
            try {
                client.webSocket(method = HttpMethod.Get, host = "127.0.0.1", port = 8080, path = "/") {
                    send(Json.encodeToString(Event.Subscribe(listOf(Registry.superSwitch.id)) as Event))
                    for (frame in incoming) {
                        frame as? Frame.Text ?: continue
                        val receivedText = frame.readText()
                        val newThing = json.decodeFromString<Thing.Switch.SwitchState>(receivedText)
                        thingState.value = thingState.value.copy(state = newThing)
                    }
                }
            } catch (e: Exception) {
                println("not" + e)
            }
        }
    }else{
        println("dfsafadsf")
    }
}



@Composable
fun App() {
    var text by remember { mutableStateOf("Hello, World!") }
    val switchState = mutableStateOf(Registry.superSwitch)
    GlobalScope.launch {
        kotlin.runCatching {
            listen(switchState)
        }.onFailure {
            println("scope" + it)
        }
    }
    val switch by remember {
        switchState
    }
    Button(onClick = {
        text = "Hello, "
    }) {
        Text("$switch")
    }
}
