package com.payby.pos.ecr.api

import com.uaepay.pos.ecr.Ecr

object Processor {

    const val VERSION = 1

    fun parserResponse(byteArray: ByteArray): String {
        try {
            val envelope = Ecr.EcrEnvelope.parseFrom(byteArray)
            val contentCase = envelope.contentCase
            if (contentCase == Ecr.EcrEnvelope.ContentCase.PING) {

            } else if (contentCase == Ecr.EcrEnvelope.ContentCase.PONG) {
                val pong = envelope.pong
            } else if (contentCase == Ecr.EcrEnvelope.ContentCase.RESPONSE) {

            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return ""
    }

}