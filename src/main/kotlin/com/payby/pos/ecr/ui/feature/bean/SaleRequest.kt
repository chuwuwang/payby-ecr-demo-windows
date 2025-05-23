package com.payby.pos.ecr.ui.feature.bean


data class SaleRequest(
    val amount: Long = 0L,
    val merchantOrderNo: String = "",
    val subject: String = "",
    val paymentMethod: Int = 0,
    val nInvokeType: Int = 0,
    val nNotificationType: Int = 0,
    val nReceiptType: Int = 0,
    val isDisplay: Boolean = false,
)
