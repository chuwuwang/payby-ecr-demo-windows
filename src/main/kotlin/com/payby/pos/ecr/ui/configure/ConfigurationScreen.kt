package com.payby.pos.ecr.ui.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.ui.theme.*

object ConfigurationScreen {

    @Composable
    fun build(modifier: Modifier) {
        var modifierX = modifier.background(backgroundColor)
        Row(modifierX, verticalAlignment = Alignment.CenterVertically) {
            sidebar()
            modifierX = Modifier.weight(3.0f).background(whiteColor)
            ClassicBTView.build(modifierX)
        }
    }

    @Composable
    private fun RowScope.sidebar() {
        val modifier = Modifier.height(IntrinsicSize.Min).weight(1.3f).padding(start = 16.dp, end = 16.dp).clip(radius_8).background(whiteColor)
        Column(modifier) { menuItem("Bluetooth EDR", mainThemeColor) }
    }

    @Composable
    private fun menuItem(text: String, tint: Color) {
        val marginStart = 24.dp
        val paddingStart = 12.dp
        Row(Modifier.fillMaxWidth().height(60.dp).padding(start = marginStart), verticalAlignment = Alignment.CenterVertically) {
            Icon(painter = painterResource("images/ic_unselect.png"), contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
            Text(modifier = Modifier.padding(start = paddingStart), color = textMainColor, textAlign = TextAlign.Start, fontSize = 16.sp, fontFamily = mediumFontFamily, text = text)
        }
    }

}
