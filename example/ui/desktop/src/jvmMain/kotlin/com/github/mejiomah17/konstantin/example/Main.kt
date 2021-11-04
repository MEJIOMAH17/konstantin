import androidx.compose.desktop.DesktopMaterialTheme
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.github.mejiomah17.konstantin.api.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        DesktopMaterialTheme(
            colors = MaterialTheme.colors.copy(primary = Color(44,44,44))
        ) {
            App()
        }
    }
}