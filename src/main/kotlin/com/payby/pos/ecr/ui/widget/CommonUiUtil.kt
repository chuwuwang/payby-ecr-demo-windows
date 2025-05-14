package com.payby.pos.ecr.ui.widget

import androidx.compose.material.Divider
import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.dp
import com.payby.pos.ecr.ui.theme.separateColor

object CommonUiUtil {

    @Composable
    fun horizontalDivider() {
        Divider(thickness = 1.dp, color = separateColor)
    }

}