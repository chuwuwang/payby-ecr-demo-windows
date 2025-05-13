package com.payby.pos.ecr.connect.bluetooth

interface ClassicBTListener {

    fun onConnected()

    fun onConnecting()

    fun onDisconnected()

    fun onMessage(bytes: ByteArray)

}
