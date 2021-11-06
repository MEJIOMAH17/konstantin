package org.github.mejiomah17.konstantin.api

import Registry
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
    MaterialTheme(
        colors = MaterialTheme.colors.copy(primary = Color.White)
    ) {
        SwitchButton(
            client = konstantinClient,
            Registry.superSwitch,
            scope = GlobalScope,
            modifier = Modifier.fillMaxSize(0.5f)
        )
    }
}