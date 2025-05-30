package com.payby.pos.ecr.ui.feature

import com.google.protobuf.Any
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.uaepay.pos.ecr.Ecr
import com.uaepay.pos.ecr.acquire.Settlement


class SettlementViewModel {

    fun  doSettlement(operatorID: String) {
        val timestamp = Processor.getTimestamp()

        val settlementRequest = Settlement.CloseBatchRequest.newBuilder().setOperatorId(operatorID).build()
        val body = Any.pack(settlementRequest)
        val request = Ecr.Request.newBuilder().setMessageId(2).setTimestamp(timestamp).setServiceName(Processor.SETTLEMENT_CLOSE).setBody(body).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setRequest(request).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }

    }
}