package com.payby.pos.ecr.connect

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf

data class BluetoothDevice(
    var name: String = "",
    var address: String = "",

    // 0: disconnected, 1: paired, 2: connected, 3: connecting
    var status: MutableState<Int> = mutableStateOf(STATUS_DISCONNECTED)
) {


    companion object {

        const val STATUS_DISCONNECTED: Int = 0
        const val STATUS_PAIRED: Int = 1
        const val STATUS_CONNECTED: Int = 2
        const val STATUS_CONNECTING: Int = 3

    }

}