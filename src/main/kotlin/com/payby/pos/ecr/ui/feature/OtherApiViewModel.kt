package com.payby.pos.ecr.ui.feature

import com.payby.pos.ecr.api.Processor
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.uaepay.pos.ecr.Ecr

class OtherApiViewModel {

    fun ping() {
        val timestamp = Processor.getTimestamp()
        val ping = Ecr.Ping.newBuilder().setMessageId(1).setTimestamp(timestamp).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setPing(ping).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }
    }

    fun getDeviceInfo() {
        val timestamp = Processor.getTimestamp()
        val request = Ecr.Request.newBuilder().setMessageId(2).setTimestamp(timestamp).setServiceName(Processor.DEVICE_GET_THIS).build()
        val envelope = Ecr.EcrEnvelope.newBuilder().setVersion(Processor.VERSION).setRequest(request).build()
        Processor.printRequest(envelope)
        val byteArray = envelope.toByteArray()
        ThreadPoolManager.executeCacheTask { ConnectionCore.send(byteArray) }
    }

}
