package org.github.mejiomah17.konstantin.api

import Registry
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.github.mejiomah17.common.SwitchButton
import kotlinx.coroutines.GlobalScope
import org.github.mejiomah17.konstantin.client.KonstantinClient

val konstantinClient = KonstantinClient(
    host = "127.0.0.1",
    port = 8080
).also {
    it.start()
}

@Composable
fun App() {
    SwitchButton(
        client = konstantinClient,
        Registry.superSwitch,
        scope = GlobalScope,
        modifier = Modifier.fillMaxSize(0.5f)
    )
}