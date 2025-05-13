package com.payby.pos.ecr.connect

interface ConnectionListener {

    fun onConnected()

    fun onDisconnected(message: String)

    fun onMessage(bytes: ByteArray)

}
