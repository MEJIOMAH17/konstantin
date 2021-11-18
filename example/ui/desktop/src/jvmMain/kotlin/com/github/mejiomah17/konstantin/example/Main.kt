import androidx.compose.ui.graphics.ExperimentalGraphicsApi
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.github.mejiomah17.konstantin.api.App
@ExperimentalGraphicsApi
fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}