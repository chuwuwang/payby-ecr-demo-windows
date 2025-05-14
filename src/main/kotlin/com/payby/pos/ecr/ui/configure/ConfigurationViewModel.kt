package com.payby.pos.ecr.ui.configure

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import com.payby.pos.ecr.connect.BluetoothDevice

class ConfigurationViewModel {

    val bluetoothDevices by lazy {
        val devices = mutableStateListOf<BluetoothDevice>()
        for (i in 0..3) {
            val bluetoothDevice = BluetoothDevice()
            bluetoothDevice.address = "00:11:22:33:44:55"
            bluetoothDevice.name = "Device $i"
            devices.add(bluetoothDevice)
        }
        devices
    }

    fun resetConnectionStatus() {
        bluetoothDevices.forEach {
            if (it.status.value != BluetoothDevice.STATUS_PAIRED) {
                it.status = mutableStateOf(BluetoothDevice.STATUS_DISCONNECTED)
            }
        }
    }

}
