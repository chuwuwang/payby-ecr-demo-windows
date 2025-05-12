import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.WindowState
import androidx.compose.ui.window.application
import com.payby.pos.ecr.ui.configure.ConfigurationScreen
import com.payby.pos.ecr.ui.main.Sidebar
import com.payby.pos.ecr.ui.theme.whiteColor

@Composable
@Preview
fun app() {
    val current = remember { mutableStateOf(0) }
    MaterialTheme { Content(current) }
}

@Composable
private fun Content(current: MutableState<Int>) {
    Row {
        val sidebarBackground = Color(0xFF2E2E2E)
        val onSidebarClick: (Int) -> Unit = { current.value = it }
        val modifierSidebar = Modifier.weight(1f).fillMaxHeight().background(sidebarBackground)
        Sidebar(modifierSidebar, current.value, onSidebarClick)

        val modifier = Modifier.weight(3f).fillMaxHeight().background(whiteColor)
        BoxWithConstraints(modifier = modifier) { SwitchPage(current, modifier) }
    }
}

@Composable
private fun SwitchPage(index: MutableState<Int>, modifier: Modifier) {
    if (index.value == Sidebar.MENU_FEAT_CONFIGURE) {
        ConfigurationScreen.build(modifier)
    }
}

fun main() = application {
    val position = WindowPosition.Aligned(Alignment.Center)
    val windowState = WindowState(size = DpSize(1200.dp, 800.dp), position = position)
    Window(title = "ECR Demo", state = windowState, onCloseRequest = ::exitApplication) { app() }
}
