package com.payby.pos.ecr.ui.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.ui.theme.boldFontFamily
import com.payby.pos.ecr.ui.theme.mainThemeColor
import com.payby.pos.ecr.ui.theme.titleFontSize
import com.payby.pos.ecr.ui.theme.whiteColor

object NavigationBar {

    const val MENU_FEAT_SALE = 0
    const val MENU_FEAT_VOID = 1
    const val MENU_FEAT_CONFIGURE = 10

}

@Composable
fun showNavigationBar(modifier: Modifier, index: Int, onClick: (Int) -> Unit) {
    Column(modifier) {
        itemView(Modifier.clickable { onClick(NavigationBar.MENU_FEAT_SALE) }, "Sale", resourcePath = "images/ic_purchase_black.png", if (index == NavigationBar.MENU_FEAT_SALE) mainThemeColor else whiteColor)
        itemView(Modifier.clickable { onClick(NavigationBar.MENU_FEAT_VOID) }, "Void", resourcePath = "images/ic_void_black.png", if (index == NavigationBar.MENU_FEAT_VOID) mainThemeColor else whiteColor)
        itemView(Modifier.clickable { onClick(NavigationBar.MENU_FEAT_CONFIGURE) }, "Configuration", resourcePath = "images/ic_configure_black.png", if (index == NavigationBar.MENU_FEAT_CONFIGURE) mainThemeColor else whiteColor)
        val boxModifier = Modifier.fillMaxHeight()
        Box(modifier = boxModifier) {
            Text(modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 24.dp), fontSize = 14.sp, text = "© PayBy Team 2024", color = whiteColor)
        }
    }
}

@Composable
private fun itemView(modifier: Modifier, text: String, resourcePath: String, tint: Color) {
    val marginStart = 24.dp
    val paddingStart = 12.dp
    Row(modifier.fillMaxWidth().height(64.dp).padding(start = marginStart), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(resourcePath), contentDescription = null, modifier = Modifier.size(28.dp), tint = tint)
        Text(modifier = Modifier.padding(start = paddingStart), color = tint, textAlign = TextAlign.Start, fontSize = titleFontSize, fontFamily = boldFontFamily, text = text)
    }
}