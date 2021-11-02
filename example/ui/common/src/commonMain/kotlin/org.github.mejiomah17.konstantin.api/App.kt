package org.github.mejiomah17.konstantin.api

import Registry
import androidx.compose.runtime.Composable
import com.github.mejiomah17.common.SwitchButton
import kotlinx.coroutines.GlobalScope
import org.github.mejiomah17.konstantin.client.KonstantinClient

val client = KonstantinClient(
    host = "127.0.0.1",
    port = 8080
).also {
    it.start()
}

@Composable
fun App() {
    SwitchButton(
        client = client,
        Registry.superSwitch,
        scope = GlobalScope
    )
}