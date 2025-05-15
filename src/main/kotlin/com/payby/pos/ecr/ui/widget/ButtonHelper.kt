package com.payby.pos.ecr.ui.widget

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.ui.theme.boldFontFamily
import com.payby.pos.ecr.ui.theme.mainThemeColor
import com.payby.pos.ecr.ui.theme.whiteColor

object ButtonHelper {

    val buttonWidth = 96.dp
    val buttonHeight = 72.dp

}

@Composable
fun TextButton(modifier: Modifier, text: String, onClick: () -> Unit) {
    Button(modifier = modifier.padding(start = 32.dp, end = 32.dp).height(ButtonHelper.buttonHeight), colors = ButtonDefaults.buttonColors(mainThemeColor), onClick = onClick) {
        Text(text, color = whiteColor, fontSize = 18.sp, fontFamily = boldFontFamily, letterSpacing = 2.sp)
    }
}
