package com.payby.pos.ecr.ui.widget

import androidx.compose.ui.awt.ComposeWindow
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

}