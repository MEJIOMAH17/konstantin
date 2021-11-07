import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import org.github.mejiomah17.konstantin.api.App

fun main() = application {
    Window(onCloseRequest = ::exitApplication) {
        App()
    }
}