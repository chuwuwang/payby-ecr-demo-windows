package com.payby.pos.ecr.api

import com.google.protobuf.Timestamp
import com.google.protobuf.util.JsonFormat
import com.uaepay.pos.ecr.Ecr
import com.uaepay.pos.ecr.acquire.Acquire.AcquireOrder
import com.uaepay.pos.ecr.acquire.Acquire.AcquireOrderPage
import com.uaepay.pos.ecr.acquire.Device.DeviceInfo
import com.uaepay.pos.ecr.acquire.Refund.RefundOrder
import com.uaepay.pos.ecr.acquire.Settlement.TransactionReport

object Processor {

    const val VERSION = 1

    const val ACQUIRE_PLACE_ORDER = "/acquire/place"
    const val ACQUIRE_GET_ORDER = "/acquire/get"
    const val ACQUIRE_GET_ORDER_LIST = "/acquire/queryPage"
    const val ACQUIRE_PRINT_RECEIPTS = "/acquire/receipts/print"
    const val ACQUIRE_NOTIFICATION = "/acquire/notification"

    const val VOID_PLACE_ORDER = "/acquire/void"

    const val REFUND_PLACE_ORDER = "/acquire/refund/place"
    const val REFUND_GET_ORDER = "/acquire/refund/get"
    const val REFUND_PRINT_RECEIPTS = "/acquire/refund/receipts/print"

    const val SETTLEMENT_CLOSE = "/settlement/closeBatch"
    const val DEVICE_GET_THIS = "/device/getThis"

    const val CLOSE_CASHIER = "/cashier/close"

    fun getTimestamp(): Timestamp {
        return Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build()
    }

    fun parserResponse(byteArray: ByteArray): String {
        try {
            val sb = StringBuilder()
            val envelope = Ecr.EcrEnvelope.parseFrom(byteArray)
            val contentCase = envelope.contentCase
            if (contentCase == Ecr.EcrEnvelope.ContentCase.PING) {
                val ping = envelope.ping
                sb.append(ping)
            } else if (contentCase == Ecr.EcrEnvelope.ContentCase.PONG) {
                val pong = envelope.pong
                sb.append(pong)
            } else if (contentCase == Ecr.EcrEnvelope.ContentCase.RESPONSE) {
                val response = envelope.response
                val responseString = parserResponse(response)
                sb.append(responseString)
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return "Response message parser error"
    }

    private fun parserResponse(response: Ecr.Response): String {
        val body = response.body
        val messageId = response.messageId
        val timestamp = response.timestamp.seconds
        val serviceName = response.getServiceName()
        val responseCode = response.getResponseCode()
        val errorMessage = response.getErrorMessage()
        val subResponseCode = response.getSubResponseCode()
        var bodyString = ""
        val builder = StringBuilder()
        builder.append("RESPONSE").append("\n")
        builder.append("messageId: ").append(messageId).append("\n")
        builder.append("timestamp: ").append(timestamp).append("\n")
        builder.append("serviceName: ").append(serviceName).append("\n")
        builder.append("responseCode: ").append(responseCode).append("\n")
        builder.append("subResponseCode: ").append(subResponseCode).append("\n")
        builder.append("errorMessage: ").append(errorMessage).append("\n")
        // Acquire
        if (serviceName == ACQUIRE_PLACE_ORDER) {
            val message = body.unpack(AcquireOrder::class.java)
            bodyString = JsonFormat.printer().print(message)
        } else if (serviceName == ACQUIRE_GET_ORDER) {
            val message = body.unpack(AcquireOrder::class.java)
            bodyString = JsonFormat.printer().print(message)
        } else if (serviceName == ACQUIRE_GET_ORDER_LIST) {
            val message = body.unpack(AcquireOrderPage::class.java)
            bodyString = JsonFormat.printer().print(message)
        }
        // Refund
        else if (serviceName == REFUND_PLACE_ORDER) {
            val message = body.unpack(RefundOrder::class.java)
            bodyString = JsonFormat.printer().print(message)
        } else if (serviceName == REFUND_GET_ORDER) {
            val message = body.unpack(RefundOrder::class.java)
            bodyString = JsonFormat.printer().print(message)
        }
        // Settlement
        else if (serviceName == SETTLEMENT_CLOSE) {
            val message = body.unpack(TransactionReport::class.java)
            bodyString = JsonFormat.printer().print(message)
        }
        // Device
        else if (serviceName == DEVICE_GET_THIS) {
            val message = body.unpack(DeviceInfo::class.java)
            bodyString = JsonFormat.printer().print(message)
        }
        builder.append("body: ").append(bodyString).append("\n")
        return builder.toString()
    }

}
