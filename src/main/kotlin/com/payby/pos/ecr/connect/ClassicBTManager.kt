package com.payby.pos.ecr.connect

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.payby.pos.ecr.bluetooth.BluetoothDevice
import com.payby.pos.ecr.bluetooth.DeviceDiscovery
import com.payby.pos.ecr.bluetooth.ServiceDiscovery
import com.payby.pos.ecr.ui.widget.DialogHelper
import com.payby.pos.ecr.utils.IOHelper
import com.payby.pos.ecr.utils.ThreadPoolManager
import java.io.InputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.ConcurrentHashMap
import javax.bluetooth.RemoteDevice
import javax.microedition.io.Connector
import javax.microedition.io.StreamConnection

object ClassicBTManager {

    private val deviceDiscovery by lazy { DeviceDiscovery() }
    private val serviceDiscovery by lazy { ServiceDiscovery() }
    private val mapLock = ConcurrentHashMap<String, RemoteDevice>()

    private var inputStream: InputStream ? = null
    private var outputStream: OutputStream ? = null
    private var streamConnection: StreamConnection ? = null

    private var running = false

    fun startDiscovery(list: SnapshotStateList<BluetoothDevice>) {
        val deviceDiscoveryListener = object : DeviceDiscovery.OnDeviceDiscoveryListener {

            override fun onDeviceDiscovered(remoteDevice: RemoteDevice) {
                addBluetoothDevice(remoteDevice, list)
            }

            override fun onDeviceDiscovered(map: ConcurrentHashMap<String, RemoteDevice>) {

            }

        }
        deviceDiscovery.setOnDeviceDiscoveryListener(deviceDiscoveryListener)
        deviceDiscovery.startDiscoveryDevice()
    }

    fun stopDiscovery() {
        deviceDiscovery.stopDiscoveryDevice()
    }

    private fun addBluetoothDevice(remoteDevice: RemoteDevice, list: SnapshotStateList<BluetoothDevice>) {
        var name = ""
        val address = remoteDevice.bluetoothAddress
        try {
            name = remoteDevice.getFriendlyName(true) ?: ""
        } catch (e: Exception) {
            e.printStackTrace()
        }
        val bool = mapLock.containsKey(address)
        if (bool) return
        val device = BluetoothDevice()
        device.name = name
        device.address = address
        device.isPaired = remoteDevice.isAuthenticated
        list.add(device)
        mapLock[address] = remoteDevice
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    fun connect(device: BluetoothDevice) {
        val remoteDevice = mapLock[device.address]
        serviceDiscovery.setOnServiceDiscoveryListener { list ->
            connectionHandler(list)
        }
        serviceDiscovery.startDiscoveryService(remoteDevice)
    }

    private fun stopDiscoveryService() {
        serviceDiscovery.stopDiscoveryService()
    }

    val isConnected: Boolean
        get() = streamConnection != null && inputStream != null && outputStream != null

    fun send(bytes: ByteArray) {
        val string = getString(bytes)
        println("<--- Windows send: $string")
        val outStream = outputStream
        if (outStream == null) {
            DialogHelper.showToast("Bluetooth connection is not established")
            return
        }
        try {
            outStream.write(bytes)
            outStream.flush()
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    fun disconnect() {
        stopDiscoveryService()
        stopDiscovery()
        close()
    }

    private fun connectionHandler(list: Vector<String>) {
        stopDiscovery()
        stopDiscoveryService()
        if (list.size == 0) return
        try {
            val url = list.elementAt(0)
            streamConnection = (Connector.open(url) as StreamConnection).apply {
                inputStream = openInputStream()
                outputStream = openOutputStream()
            }
            ThreadPoolManager.executeCacheTask { readLooper() }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
    }

    private fun readLooper() {
        var len: Int
        var string: String ?
        var bytes: ByteArray
        var buffer: ByteArray
        running = true
        while (running && streamConnection != null && inputStream != null) {
            val inStream = inputStream ?: return
            buffer = ByteArray(4 * 1024)
            try {
                len = inStream.read(buffer)
                while (len != -1) {
                    bytes = ByteArray(len)
                    System.arraycopy(buffer, 0, bytes, 0, len)
                    string = getString(bytes)
                    println("---> Windows received: $string")
                    len = inStream.read(buffer)
                }
            } catch (e: Throwable) {
                e.printStackTrace()
            }
        }
    }

    private fun close() {
        IOHelper.close(inputStream)
        IOHelper.close(outputStream)
        try {
            val connection = streamConnection
            if (connection != null) {
                connection.close()
            }
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        running = false
        inputStream = null
        outputStream = null
        streamConnection = null
    }

    private fun getString(bytes: ByteArray): String ? {
        try {
            return String(bytes)
        } catch (e: Throwable) {
            e.printStackTrace()
        }
        return null
    }

}
