package com.payby.pos.ecr.ui.theme

import androidx.compose.material.TextFieldColors
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable

val SuccessTextFieldColors: TextFieldColors
    @Composable
    get() = TextFieldDefaults.outlinedTextFieldColors(textColor = blackColor, cursorColor = successColor, unfocusedBorderColor = separateColor, focusedBorderColor = successColor)