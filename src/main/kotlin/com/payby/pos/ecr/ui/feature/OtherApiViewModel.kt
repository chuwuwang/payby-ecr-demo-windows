package com.payby.pos.ecr.ui.feature

import com.google.protobuf.Timestamp
import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.uaepay.pos.ecr.Ecr

class OtherApiViewModel {

    fun doPing() {
        val timestamp = getTimestamp()
        val ping = Ecr.Ping.newBuilder().setMessageId(5).setTimestamp(timestamp).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setPing(ping).build()
        val byteArray = envelope.toByteArray()
        ConnectionCore.send(byteArray)
    }

    private fun getTimestamp(): Timestamp {
        return Timestamp.newBuilder().setSeconds(System.currentTimeMillis() / 1000).build()
    }

}
