package com.payby.pos.ecr.connect

import java.util.*

object ConnectionCore {

    private val callbacks = ArrayList<ConnectionListener>()

    var connectType = ConnectType.BLUETOOTH

    fun connectWithClassicBT(device: BluetoothDevice) {
        connectType = ConnectType.BLUETOOTH
        ClassicBTManager.connect(device)
    }

    fun send(bytes: ByteArray) {
        if (connectType == ConnectType.BLUETOOTH) {
            ClassicBTManager.send(bytes)
        }
    }

    val isConnected: Boolean
        get() {
            return if (connectType == ConnectType.BLUETOOTH) {
                ClassicBTManager.isConnected
            } else {
                false
            }
        }

    fun disconnect() {
        if (connectType == ConnectType.BLUETOOTH) {
            ClassicBTManager.disconnect()
        }
    }

    fun addListener(listener: ConnectionListener) {
        callbacks.add(listener)
    }

    fun removeListener(listener: ConnectionListener) {
        callbacks.remove(listener)
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun onConnected() {
        val synchronizedList = Collections.synchronizedList(callbacks)
        synchronizedList.forEach { it.onConnected() }
    }

    fun onDisconnected(message: String) {
        val synchronizedList = Collections.synchronizedList(callbacks)
        synchronizedList.forEach { it.onDisconnected(message) }
    }

    fun onReceived(bytes: ByteArray) {
        val synchronizedList = Collections.synchronizedList(callbacks)
        synchronizedList.forEach { it.onMessage(bytes) }
    }

}
