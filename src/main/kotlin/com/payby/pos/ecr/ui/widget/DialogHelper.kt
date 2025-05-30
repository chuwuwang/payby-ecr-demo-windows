package com.payby.pos.ecr.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.awt.ComposeWindow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogWindow
import com.payby.pos.ecr.ui.theme.boldFontFamily
import com.payby.pos.ecr.ui.theme.whiteColor
import kotlinx.coroutines.delay
import java.awt.event.WindowEvent
import javax.swing.JOptionPane

object DialogHelper {

    fun showToast(message: String, title: String = "INFO") {
        val window = ComposeWindow()
        JOptionPane.showMessageDialog(window, message, title, JOptionPane.INFORMATION_MESSAGE)
    }

    fun showToastWithError(message: String, title: String = "ERROR") {
        val window = ComposeWindow()
        JOptionPane.showMessageDialog(window, message, title, JOptionPane.ERROR_MESSAGE)
    }

    @Composable
    fun LoadingDialog(message: String = "LOADING...", visible: Boolean, onCloseRequest: () -> Unit = {}) {
        DialogWindow(title = "", onCloseRequest = onCloseRequest, resizable = false, undecorated = true, visible = visible) {
            val color = Color(0x60000000)
            val currentTime = remember { mutableStateOf(System.currentTimeMillis()) }
            Box(modifier = Modifier.fillMaxSize().background(color).pointerInput(Unit) {
                awaitPointerEventScope {
                    while (true) {
                        val event = awaitPointerEvent()
                        val localTime= System.currentTimeMillis()
                        if (localTime - currentTime.value > 3000) {
                            onCloseRequest()
                        }

                    }
                }
            }, contentAlignment = Alignment.Center) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    CircularProgressIndicator(color = whiteColor)
                    val style = TextStyle(color = whiteColor, fontFamily = boldFontFamily, fontSize = 16.sp)
                    Text(message, modifier = Modifier.padding(start = 10.dp), letterSpacing = 1.5.sp, style = style)
                }
            }
        }
    }

}
