import androidx.compose.ui.window.ComposeUIViewController
import custom.KoinInit

fun MainViewController() = ComposeUIViewController(
    configure = {


        KoinInit().initialize()
    }
) {
    App()
}