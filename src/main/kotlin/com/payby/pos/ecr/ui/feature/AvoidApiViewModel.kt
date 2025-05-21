package com.payby.pos.ecr.ui.feature

import com.google.protobuf.Any
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.uaepay.pos.ecr.Ecr
import com.uaepay.pos.ecr.acquire.Acquire
import com.uaepay.pos.ecr.acquire.Void

class AvoidApiViewModel {
    fun doAvoid(orderNo: String, merchantOrderNo:String? ="", nType: Int = 0) {
        val timestamp = Processor.getTimestamp()
        val listReceipt = ArrayList<Acquire.Receipt>()
        when (nType) {
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
        val voidRequest = Void.VoidRequest.newBuilder().setAcquireOrderNo(orderNo).setVoidMerchantOrderNo(merchantOrderNo).addAllPrintReceipts(listReceipt).build()
        val body = Any.pack(voidRequest)
        val request = Ecr.Request.newBuilder().setMessageId(2).setTimestamp(timestamp).setServiceName(Processor.VOID_PLACE_ORDER).setBody(body).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setRequest(request).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }

    }
}