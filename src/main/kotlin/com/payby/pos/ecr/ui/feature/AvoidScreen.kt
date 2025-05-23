package com.payby.pos.ecr.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
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
import com.payby.pos.ecr.ui.widget.TextButton
import com.payby.pos.ecr.utils.isEmpty

@Composable
fun AvoidScreen(modifier: Modifier, avoidApiViewModel: AvoidApiViewModel) {

    val inputAvoidOrderNo = remember { mutableStateOf("") }
    val inputAvoidMerchantOrderNoOriginal = remember { mutableStateOf("") }
    val inputAvoidMerchantOrderNo = remember { mutableStateOf("") }
    val isMerchantReceipt = remember { mutableStateOf(false) }
    val isCustomerReceipt = remember { mutableStateOf(false) }
    val isLoading = remember { mutableStateOf(false) }
    val toaster = rememberToasterState {  }

    DisposableEffect(key1 = Unit) {
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

            }

        }
        ConnectionCore.addListener(listener = listener)
        onDispose { ConnectionCore.removeListener(listener = listener) }
    }


    Column(modifier = modifier.padding(top =16.dp, bottom = 16.dp)) {
        OutlinedTextField(
            value = inputAvoidOrderNo.value,
            onValueChange = {
                inputAvoidOrderNo.value = it},
            modifier = Modifier.padding(start = 32.dp),
            placeholder = {
                Text(text = "Input original order no", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
            },
            colors = SuccessTextFieldColors
        )
        OutlinedTextField(
            value = inputAvoidMerchantOrderNoOriginal.value,
            onValueChange = {
                inputAvoidMerchantOrderNoOriginal.value = it},
            modifier = Modifier.padding(start = 32.dp, top = 16.dp),
            placeholder = {
                Text(text = "Input original merchant order no", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
            },
            colors = SuccessTextFieldColors
        )
        OutlinedTextField(
            value = inputAvoidMerchantOrderNo.value,
            onValueChange = {
                inputAvoidMerchantOrderNo.value = it},
            modifier = Modifier.padding(start = 32.dp, top = 16.dp),
            placeholder = {
                Text(text = "Input merchantOrderNo(Optional)", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
            },
            colors = SuccessTextFieldColors
        )
        Text(text = "Receipt (Optional)", modifier = Modifier.padding(start = 32.dp, top = 16.dp), fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)

        Row(modifier = Modifier.padding(start = 32.dp, top = 16.dp), horizontalArrangement = Arrangement.Start) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = isMerchantReceipt.value, onCheckedChange = {
                    isMerchantReceipt.value = it
                })

                Text(text = "Merchant", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)

            }

            Row(horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically) {
                Checkbox(checked = isCustomerReceipt.value, onCheckedChange = {
                    isCustomerReceipt.value = it
                })

                Text(text = "Customer", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)
            }

        }
        TextButton(
            modifier = Modifier.padding(vertical = 16.dp),
            text = "Request"
        ) {
            if (inputAvoidOrderNo.value.isEmpty) {
                toaster.show("Please enter a valid order no.")
                return@TextButton
            }
            val nType = if (isMerchantReceipt.value && isCustomerReceipt.value) {
                3
            } else if (isMerchantReceipt.value && !isCustomerReceipt.value) {
                2
            } else if (!isMerchantReceipt.value && isCustomerReceipt.value) {
                1
            } else {
                0
            }

            avoidApiViewModel.doAvoid(inputAvoidOrderNo.value, inputAvoidMerchantOrderNoOriginal.value, inputAvoidMerchantOrderNo.value, nType)
            
        }

        Toaster(state = toaster)
    }
}
