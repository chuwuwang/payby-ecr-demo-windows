package com.payby.pos.ecr.utils

import java.math.BigDecimal
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.Locale

object AmountHelper {

    fun longCent2String(value: Long): String {
        val decimal = BigDecimal(value)
        val bigDecimal = BigDecimal("100")
        val doubleValue = decimal.divide(bigDecimal).toDouble()
        val symbols = DecimalFormatSymbols.getInstance(Locale.US)
        val decimalFormat = DecimalFormat("#0.00", symbols)
        return decimalFormat.format(doubleValue)
    }
}