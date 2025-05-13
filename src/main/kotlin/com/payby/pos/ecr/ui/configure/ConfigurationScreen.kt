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

@Composable
fun ConfigurationScreen(modifier: Modifier, viewModel: ConfigurationViewModel) {
    var modifier = modifier.background(backgroundColor)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        Sidebar()
        modifier = Modifier.weight(3.0f).padding(16.dp).clip(radius_8).background(whiteColor)
        ClassicBTConfig(modifier, viewModel)
    }
}

@Composable
private fun RowScope.Sidebar() {
    val modifier = Modifier.height(IntrinsicSize.Min).weight(1.3f)
        .padding(start = 16.dp).clip(radius_8).background(whiteColor)
    Column(modifier) { ItemView("Bluetooth EDR", mainThemeColor) }
}

@Composable
private fun ItemView(text: String, tint: Color) {
    val marginStart = 24.dp
    val paddingStart = 12.dp
    Row(Modifier.fillMaxWidth().height(60.dp).padding(start = marginStart), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource("images/ic_unselect.png"), contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
        Text(modifier = Modifier.padding(start = paddingStart), color = textMainColor, textAlign = TextAlign.Start, fontSize = 16.sp, fontFamily = mediumFontFamily, text = text)
    }
}
