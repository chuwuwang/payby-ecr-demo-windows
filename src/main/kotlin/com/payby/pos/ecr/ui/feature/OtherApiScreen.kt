package com.payby.pos.ecr.ui.feature

import androidx.compose.foundation.layout.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
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

private const val API_PING = "api_ping"
private const val API_GET_DEVICE_INFO = "api_get_device_info"
private const val API_QUERY_ACQUIRE_ORDER = "api_query_acquire_order"
private const val API_QUERY_REFUND_ORDER = "api_query_refund_order"

@Composable
fun OtherApiScreen(modifier: Modifier, viewModel: OtherApiViewModel) {
    val toaster = rememberToasterState()
    val isLoading = remember { mutableStateOf(false) }
    val selector = remember { mutableStateOf("") }
    val outputText = remember { mutableStateOf("") }
    val inputRefundOrder = remember { mutableStateOf("") }
    val inputAcquireOrder = remember { mutableStateOf("") }
    val onOutputValueChange: (String) -> Unit = {
        outputText.value = it
    }

    DisposableEffect(Unit) {
        val listener = object : ConnectionListener {

            override fun onConnected() {}

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
        ConnectionCore.addListener(listener)
        onDispose { ConnectionCore.removeListener(listener) }
    }

    Column(modifier) {
        FeatureList(selector, inputAcquireOrder, inputRefundOrder)
        TextButton(modifier = Modifier.padding(vertical = 24.dp), text = "Request") {
            if (ConnectionCore.isConnected) {
                isLoading.value = true
                if (selector.value == API_PING) {
                    viewModel.ping()
                } else if (selector.value == API_GET_DEVICE_INFO) {

                } else if (selector.value == API_QUERY_ACQUIRE_ORDER) {

                } else if (selector.value == API_QUERY_REFUND_ORDER) {

                }
            } else {
                toaster.show("Please connect to the device first")
            }
        }
        val modifierScroll = Modifier.fillMaxSize().padding(start = 24.dp, end = 24.dp, bottom = 24.dp)
        CommonUiUtil.InputTextField(modifierScroll, outputText.value, onOutputValueChange)
    }
    Toaster(state = toaster)
    LoadingDialog(visible = isLoading.value)
}

private fun placeholderText(): @Composable () -> Unit = {
    Text(text = "Input orderNo. or merchantOrderNo.", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
}

@Composable
private fun FeatureList(selector: MutableState<String>, inputAcquireOrder: MutableState<String>, inputRefundOrder: MutableState<String>) {
    val onInputChangeAcquireOrder: (String) -> Unit = {
        inputAcquireOrder.value = it
    }
    val onInputChangeRefundOrder: (String) -> Unit = {
        inputRefundOrder.value = it
    }
    CommonUiUtil.RadioButton("Ping", selector.value == API_PING) {
        selector.value = API_PING
    }
    CommonUiUtil.RadioButton("Get Device Info", selector.value == API_GET_DEVICE_INFO) {
        selector.value = API_GET_DEVICE_INFO
    }
    Row(modifier = Modifier.padding(end = 24.dp), verticalAlignment = Alignment.CenterVertically) {
        CommonUiUtil.RadioButton("Query Acquire Order", selector.value == API_QUERY_ACQUIRE_ORDER) {
            selector.value = API_QUERY_ACQUIRE_ORDER
        }
        val textStyle = TextStyle(color = textMainColor, fontSize = 18.sp, fontFamily = mediumFontFamily, lineHeight = 30.sp, letterSpacing = 2.sp)
        OutlinedTextField(modifier = Modifier.padding(start = 16.dp).fillMaxWidth(), placeholder = placeholderText(), value = inputAcquireOrder.value,
            onValueChange = onInputChangeAcquireOrder, textStyle = textStyle, colors = SuccessTextFieldColors)
    }
    Row(modifier = Modifier.padding(end = 24.dp, top = 16.dp), verticalAlignment = Alignment.CenterVertically) {
        CommonUiUtil.RadioButton("Query Refund Order", selector.value == API_QUERY_REFUND_ORDER) {
            selector.value = API_QUERY_REFUND_ORDER
        }
        val textStyle = TextStyle(color = textMainColor, fontSize = 18.sp, fontFamily = mediumFontFamily, lineHeight = 30.sp, letterSpacing = 2.sp)
        OutlinedTextField(modifier = Modifier.padding(start = 16.dp).fillMaxWidth(), placeholder = placeholderText(), value = inputRefundOrder.value,
            onValueChange = onInputChangeRefundOrder, textStyle = textStyle, colors = SuccessTextFieldColors)
    }
}
