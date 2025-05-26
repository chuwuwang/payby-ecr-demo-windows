package com.payby.pos.ecr.ui.feature

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Checkbox
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
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
import com.payby.pos.ecr.ui.feature.bean.SaleRequest
import com.payby.pos.ecr.ui.theme.SuccessTextFieldColors
import com.payby.pos.ecr.ui.theme.mediumFontFamily
import com.payby.pos.ecr.ui.theme.textMainColor
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.ui.widget.CommonUiUtil
import com.payby.pos.ecr.ui.widget.TextButton

@Composable
fun PurchaseScreen(modifier: Modifier, purchaseViewModel: PurchaseViewModel) {
    val toaster = rememberToasterState()
    val inputAmount = remember { mutableStateOf("") }
    val inputMerchantNo = remember { mutableStateOf("") }
    val inputSubject = remember { mutableStateOf("") }
    val selectorNotification = remember { mutableStateOf("") }
    val selectorResultNotification = remember { mutableStateOf("") }

    val isDisplayResult = remember { mutableStateOf(false) }
    val outputText = remember { mutableStateOf("") }
    val onOutputValueChange: (String) -> Unit = {
        outputText.value = it
    }
    val paymentTypeState = remember { mutableStateListOf(false, false, false) }
    val receiptTypeState = remember { mutableStateListOf(false, false)}
    val isLoading = remember { mutableStateOf(false) }

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

    Column(modifier = modifier.fillMaxSize().verticalScroll(rememberScrollState())) {
        Row(
            modifier = Modifier.fillMaxWidth().padding(start = 24.dp, top = 24.dp),
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
                value = inputMerchantNo.value,
                onValueChange = {
                    inputMerchantNo.value = it
                },
                modifier = Modifier.padding(start = 36.dp),
                placeholder = {
                    Text(text = "Input merchant order no [A-Za-z0-9]", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
                },
                colors = SuccessTextFieldColors
            )
        }

        OutlinedTextField(
            value = inputSubject.value,
            onValueChange = {
                inputSubject.value = it
            },
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            placeholder = {
                Text(text = "Input Subject (Optional)", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textSecondaryColor)
            },
            colors = SuccessTextFieldColors
        )

        Text(text = "Select Payment Type (Optional)",
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            fontSize = 24.sp, fontFamily = mediumFontFamily, color = textMainColor
        )

        Row(modifier = Modifier.padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,) {

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = paymentTypeState[0], onCheckedChange = {
                    paymentTypeState[0] = it
                })

                Text(text = "Bank Card", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = paymentTypeState[1], onCheckedChange = {
                   paymentTypeState[1] = it
                })

                Text(text = "Customer Present Code", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)
            }

            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Checkbox(checked = paymentTypeState[2], onCheckedChange = {
                    paymentTypeState[2] = it
                })

                Text(text = "POS Present Code", fontSize = 16.sp, fontFamily = mediumFontFamily, color = textMainColor)
            }

        }

        Text(text = "Invoke Type",
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            fontSize = 24.sp, fontFamily = mediumFontFamily, color = textMainColor)

        Row(
            modifier = Modifier.padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CommonUiUtil.RadioButton(text = "Sync Notification",
                selected = selectorNotification.value == "SYNC",
                onClick = {
                   selectorNotification.value = "SYNC"
                }
            )
            CommonUiUtil.RadioButton(text =  "Async Notification",
                selected = selectorNotification.value == "ASYNC",
                onClick = {
                    selectorNotification.value = "ASYNC"
                }
            )
        }


        Text(text = "Payment Result Notification (Optional)",
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            fontSize = 24.sp, fontFamily = mediumFontFamily, color = textMainColor)

        Row(
            modifier = Modifier.padding(start = 24.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            CommonUiUtil.RadioButton(text = "REQUEST",
                selected = selectorResultNotification.value == "REQUEST",
                onClick = {
                    selectorResultNotification.value = "REQUEST"
                }
            )
            CommonUiUtil.RadioButton(text =  "EVENT",
                selected = selectorResultNotification.value == "EVENT",
                onClick = {
                    selectorResultNotification.value = "EVENT"
                }
            )
        }

        Text(text = "Receipt (Optional)", modifier = Modifier.padding(start = 24.dp, top = 16.dp), fontSize = 24.sp, fontFamily = mediumFontFamily, color = textMainColor)

        Row(modifier = Modifier.padding(start = 24.dp, top = 16.dp), horizontalArrangement = Arrangement.Start) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Checkbox(checked = receiptTypeState[0], onCheckedChange = {
                    receiptTypeState[0]= it
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

        Row(
            modifier = Modifier.padding(start = 24.dp, top = 16.dp),
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(text = "Display Result Page", fontSize = 24.sp, fontFamily = mediumFontFamily, color = textMainColor)
            Checkbox(checked = isDisplayResult.value, onCheckedChange = {
                isDisplayResult.value = it
            })

        }

        Row(
            horizontalArrangement = Arrangement.Start,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            TextButton(modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                text =  "Request",
                onClick = {
                    if (ConnectionCore.isConnected) {
                        val paymentType =
                            (if (paymentTypeState[0]) 1 else 0) or
                                    (if (paymentTypeState[1]) 2 else 0) or
                                    (if (paymentTypeState[2]) 4 else 0)
                        val invokeType = when(selectorNotification.value) {
                            "SYNC" -> 1
                            "ASYNC" -> 2
                            else -> 0
                        }

                        val notificationType = when (selectorResultNotification.value) {
                            "REQUEST" -> 1
                            "EVENT" -> 2
                            else -> 0
                        }

                        val nReceiptType =  (if (paymentTypeState[0]) 1 else 0) or
                                (if (paymentTypeState[1]) 2 else 0)

                        val amountStr = inputAmount.value
                        if (amountStr.isNotEmpty() && amountStr.toLong()> 0) {
                            val request = SaleRequest(
                                amount = amountStr.toLong(),
                                subject = inputSubject.value,
                                paymentMethod = paymentType,
                                nInvokeType = invokeType,
                                nNotificationType = notificationType,
                                nReceiptType = nReceiptType,
                                isDisplay = isDisplayResult.value,
                            )
                            purchaseViewModel.doTransaction(request)
                        } else {
                            toaster.show("Please correct amount first")
                        }
                    } else {
                        toaster.show("Please connect to the device first")
                    }

                })

            TextButton(modifier = Modifier.padding(start = 24.dp, top = 16.dp),
                text =  "Cancel",
                onClick = {

                })
        }

        val modifierScroll = Modifier.fillMaxSize().padding(start = 24.dp, end = 24.dp, bottom = 24.dp, top = 16.dp)
        CommonUiUtil.InputTextField(modifierScroll, outputText.value, onOutputValueChange)


        Toaster(state = toaster)

    }

}
