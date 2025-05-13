package com.payby.pos.ecr.connect

import com.payby.pos.ecr.bluetooth.BluetoothDevice

object ConnectionCore {

    fun connectWithClassicBT(device: BluetoothDevice) {
        ClassicBTManager.connect(device)
    }

}