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

object Sidebar {

    const val MENU_FEAT_PURCHASE = 0
    const val MENU_FEAT_VOID = 1
    const val MENU_FEAT_REFUND = 2

    const val MENU_FEAT_SETTLEMENT = 10

    const val MENU_FEAT_OTHER = 20
    const val MENU_FEAT_CONFIGURE = 21

}

@Composable
fun Sidebar(modifier: Modifier, index: Int, onClick: (Int) -> Unit) {
    Column(modifier) {
        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_PURCHASE) }, "Purchase", resourcePath = "images/ic_purchase_black.png", if (index == Sidebar.MENU_FEAT_PURCHASE) mainThemeColor else whiteColor)
        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_VOID) }, "Void", resourcePath = "images/ic_void_black.png", if (index == Sidebar.MENU_FEAT_VOID) mainThemeColor else whiteColor)
        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_REFUND) }, "Refund", resourcePath = "images/ic_refund_black.png", if (index == Sidebar.MENU_FEAT_REFUND) mainThemeColor else whiteColor)

        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_SETTLEMENT) }, "Settlement", resourcePath = "images/ic_settlement_black.png", if (index == Sidebar.MENU_FEAT_SETTLEMENT) mainThemeColor else whiteColor)

        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_OTHER) }, "Other Api", resourcePath = "images/ic_other_black.png", if (index == Sidebar.MENU_FEAT_OTHER) mainThemeColor else whiteColor)
        ItemView(Modifier.clickable { onClick(Sidebar.MENU_FEAT_CONFIGURE) }, "Configuration", resourcePath = "images/ic_configure_black.png", if (index == Sidebar.MENU_FEAT_CONFIGURE) mainThemeColor else whiteColor)

        val modifierBox = Modifier.fillMaxHeight()
        Box(modifier = modifierBox) {
            Text(modifier = Modifier.align(Alignment.BottomStart).padding(start = 24.dp, bottom = 24.dp), fontSize = 14.sp, text = "© PayBy Team 2025", color = whiteColor)
        }
    }
}

@Composable
private fun ItemView(modifier: Modifier, text: String, resourcePath: String, tint: Color) {
    val marginStart = 24.dp
    val paddingStart = 12.dp
    Row(modifier.fillMaxWidth().height(64.dp).padding(start = marginStart), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(resourcePath), contentDescription = null, modifier = Modifier.size(28.dp), tint = tint)
        Text(modifier = Modifier.padding(start = paddingStart), color = tint, textAlign = TextAlign.Start, fontSize = titleFontSize, letterSpacing = 1.5.sp, fontFamily = boldFontFamily, text = text)
    }
}