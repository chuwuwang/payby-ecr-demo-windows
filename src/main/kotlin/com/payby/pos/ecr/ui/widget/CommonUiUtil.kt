package com.payby.pos.ecr.ui.widget

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.ui.theme.*

object CommonUiUtil {

    @Composable
    fun RadioButton(text: String, selected: Boolean, onClick: () -> Unit) {
        val buttonColors = RadioButtonDefaults.colors(successColor)
        Row(modifier = Modifier.height(56.dp), verticalAlignment = Alignment.CenterVertically) {
            RadioButton(onClick = onClick, selected = selected, colors = buttonColors)
            Text(text = text, fontSize = 18.sp, fontFamily = boldFontFamily, letterSpacing = 1.sp, color = textMainColor)
        }
    }

    @Composable
    fun InputTextField(modifier: Modifier, value: String, onValueChange: (String) -> Unit) {
        val textStyle = TextStyle(color = textMainColor, fontSize = 18.sp, fontFamily = mediumFontFamily, lineHeight = 30.sp, letterSpacing = 2.sp)
        OutlinedTextField(modifier = modifier, value = value, onValueChange = onValueChange, textStyle = textStyle, colors = SuccessTextFieldColors)
    }

    @Composable
    fun HorizontalDivider() {
        Divider(thickness = 1.dp, color = separateColor)
    }

}
