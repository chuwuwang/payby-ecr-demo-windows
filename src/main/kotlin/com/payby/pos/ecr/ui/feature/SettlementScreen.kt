package com.payby.pos.ecr.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sonner.Toaster
import com.dokar.sonner.rememberToasterState
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.connect.ConnectionListener
import com.payby.pos.ecr.ui.theme.SuccessTextFieldColors
import com.payby.pos.ecr.ui.theme.mediumFontFamily
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.ui.widget.CommonUiUtil
import com.payby.pos.ecr.ui.widget.DialogHelper.LoadingDialog
import com.payby.pos.ecr.ui.widget.TextButton

@Composable
fun SettlementScreen(modifier: Modifier, settlementViewModel: SettlementViewModel) {

    val toaster = rememberToasterState {  }
    val isLoading = remember { mutableStateOf(false) }
    val outputText = remember { mutableStateOf("") }
    val onOutputValueChange: (String) -> Unit = {
        outputText.value = it
    }
    val inputOperatorID = remember { mutableStateOf("") }


    DisposableEffect(Unit) {
        val listener = object : ConnectionListener {
            override fun onConnected() {

            }

            override fun onDisconnected(message: String) {
                isLoading.value = false
                toaster.show(message)
            }

            override fun onMessage(bytes: ByteArray) {
                isLoading.value = false
                val string = Processor.parserResponse(bytes)
                outputText.value = string
            }

        }
        ConnectionCore.addListener(listener = listener)
        onDispose { ConnectionCore.removeListener(listener = listener) }
    }

    Column(modifier = modifier.padding(top = 24.dp)) {
        OutlinedTextField(
            value = inputOperatorID.value,
            onValueChange = { text->
                inputOperatorID.value = text.filter { it.isDigit() }
            },
            modifier = Modifier.padding(start = 32.dp, top = 24.dp),
            placeholder = {
                Text(text = "Input operator id", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
            },
            colors = SuccessTextFieldColors
        )

        TextButton(modifier= Modifier.padding(top = 16.dp), text = "Request") {
            if (!ConnectionCore.isConnected) {
                if (inputOperatorID.value.isNotBlank()) {
                    settlementViewModel.doSettlement(inputOperatorID.value)
                } else {
                    toaster.show("Please input a valid operator ID.")
                }
            } else {
                toaster.show("Please connect to your Device.")
            }
        }

        CommonUiUtil.InputTextField(modifier = Modifier.fillMaxSize().padding(start = 32.dp, end = 24.dp, bottom = 24.dp, top = 16.dp), outputText.value, onOutputValueChange)

        Toaster(state = toaster)
        LoadingDialog(visible = isLoading.value, onCloseRequest = {
            isLoading.value = false
        })
    }
}
