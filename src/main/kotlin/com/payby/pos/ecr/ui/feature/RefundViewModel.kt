package com.payby.pos.ecr.ui.feature

import com.google.protobuf.Any
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.utils.AmountHelper
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.uaepay.pos.ecr.Ecr
import com.uaepay.pos.ecr.acquire.Acquire
import com.uaepay.pos.ecr.acquire.Refund
import com.uaepay.pos.ecr.common.Common

class RefundViewModel {

    fun doRefund(amount: Long, orgOrderNo: String, orgMerchantNo: String, merchantNo: String = "", nReceipt: Int =0 ) {
        val timestamp = Processor.getTimestamp()
        val money = Common.Money.newBuilder()
            .setAmount(AmountHelper.longCent2String(amount))
            .setCurrencyCode("AED")
            .build()

        val listReceipt = ArrayList<Acquire.Receipt>()
        when (nReceipt) {
            1-> {
                listReceipt.add(Acquire.Receipt.CUSTOMER_RECEIPT)
            }
            2-> {
                listReceipt.add(Acquire.Receipt.MERCHANT_RECEIPT)
            }
            3->{
                listReceipt.add(Acquire.Receipt.CUSTOMER_RECEIPT)
                listReceipt.add(Acquire.Receipt.MERCHANT_RECEIPT)
            }
        }

        val refundRequest = Refund.RefundRequest.newBuilder()
            .setRefundAmount(money)
            .setRefundMerchantOrderNo(merchantNo)
            .setAcquireMerchantOrderNo(orgOrderNo)
            .setAcquireMerchantOrderNo(orgMerchantNo)
            .addAllPrintReceipts(listReceipt)
            .build()
        val body = Any.pack(refundRequest)
        val request = Ecr.Request.newBuilder().setMessageId(2).setTimestamp(timestamp).setServiceName(Processor.REFUND_PLACE_ORDER).setBody(body).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setRequest(request).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }

    }
}