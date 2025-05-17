package com.payby.pos.ecr.ui.feature

import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.uaepay.pos.ecr.Ecr

class OtherApiViewModel {

    fun ping() {
        val timestamp = Processor.getTimestamp()
        val ping = Ecr.Ping.newBuilder().setMessageId(5).setTimestamp(timestamp).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setPing(ping).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ConnectionCore.send(byteArray)
    }

}
