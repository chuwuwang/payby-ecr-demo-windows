package com.payby.pos.ecr.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
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
import com.payby.pos.ecr.ui.theme.textMainColor
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.ui.widget.CommonUiUtil
import com.payby.pos.ecr.ui.widget.DialogHelper.LoadingDialog
import com.payby.pos.ecr.ui.widget.TextButton

@Composable
fun RefundScreen(modifier: Modifier, refundViewModel: RefundViewModel) {

    val toaster = rememberToasterState {  }
    val isLoading = remember { mutableStateOf(false) }
    val outputText = remember { mutableStateOf("") }
    val onOutputValueChange: (String) -> Unit = {
        outputText.value = it
    }

    val inputAmount = remember { mutableStateOf("") }
    val inputOriginalOrderNo = remember { mutableStateOf("") }
    val inputOriginalMerchantNo = remember { mutableStateOf("") }
    val inputMerchantNo = remember { mutableStateOf("") }
    val receiptTypeState = remember { mutableStateListOf(false, false)}

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
    Column(modifier = modifier.fillMaxSize().padding(top = 16.dp, bottom = 16.dp)) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputAmount.value,
                onValueChange = { text->
                    inputAmount.value = text.filter { it.isDigit() }
                },
                placeholder = {
                    Text(text = "Input Amount (unit cent)", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
                },
                colors = SuccessTextFieldColors
            )

            OutlinedTextField(
                value = inputOriginalOrderNo.value,
                onValueChange = {
                    inputOriginalOrderNo.value = it},
                modifier= Modifier.padding(start = 36.dp),
                placeholder = {
                    Text(text = "Input original order no", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
                },
                colors = SuccessTextFieldColors
            )
        }

        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 32.dp, top = 24.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputOriginalMerchantNo.value,
                onValueChange = {
                    inputOriginalMerchantNo.value = it},
                placeholder = {
                    Text(text = "Input original merchant no", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
                },
                colors = SuccessTextFieldColors
            )
            OutlinedTextField(
                value = inputMerchantNo.value,
                onValueChange = {
                    inputMerchantNo.value = it},
                modifier= Modifier.padding(start = 36.dp),
                placeholder = {
                    Text(text = "Input merchant no (optional)", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
                },
                colors = SuccessTextFieldColors
            )
        }



        Row(modifier = Modifier.padding(start = 32.dp, top = 16.dp), horizontalArrangement = Arrangement.Start) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = receiptTypeState[0], onCheckedChange = {
                    receiptTypeState[0] = it
                })

                Text(text = "Merchant", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)

            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = receiptTypeState[1], onCheckedChange = {
                    receiptTypeState[1] = it
                })

                Text(text = "Customer", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)
            }

        }

        TextButton(
            modifier = Modifier.padding(top = 16.dp),
            text = "Request"
        ) {
            if (ConnectionCore.isConnected) {
                val amountStr = inputAmount.value
                if (amountStr.isNotEmpty() && amountStr.toLong() > 0L) {
                    val nReceiptType =  (if (receiptTypeState[0]) 1 else 0) or
                            (if (receiptTypeState[1]) 2 else 0)

                    refundViewModel.doRefund(amountStr.toLong(),
                        inputOriginalOrderNo.value,
                        inputOriginalMerchantNo.value,
                        inputOriginalOrderNo.value,
                        nReceiptType
                        )
                } else {
                    toaster.show("Please correct amount first")
                }
            } else {
                toaster.show("Please connect to your Device.")
            }
        }

        CommonUiUtil.InputTextField(Modifier.fillMaxSize().padding(start = 32.dp, end = 24.dp, bottom = 24.dp, top = 16.dp), outputText.value, onOutputValueChange)

        Toaster(state = toaster)
        LoadingDialog(visible = isLoading.value, onCloseRequest = {
            isLoading.value = false
        })
    }

}
