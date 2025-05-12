package com.payby.pos.ecr.connect

import androidx.compose.runtime.snapshots.SnapshotStateList
import com.payby.pos.ecr.bluetooth.BluetoothDevice
import com.payby.pos.ecr.bluetooth.DeviceDiscovery
import com.payby.pos.ecr.bluetooth.ServiceDiscovery
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

    fun connect(device: BluetoothDevice) {
        val remoteDevice = mapLock[device.address]
        serviceDiscovery.setOnServiceDiscoveryListener { list ->
            connectionHandler(list)
        }
        serviceDiscovery.startDiscoveryService(remoteDevice)
    }

    private fun connectionHandler(list: Vector<String>) {
        stopDiscovery()
        serviceDiscovery.stopDiscoveryService()
        if (list.size == 0) return
        try {
            val url = list.elementAt(0)
            streamConnection = (Connector.open(url) as StreamConnection).apply {
                inputStream = openInputStream()
                outputStream = openOutputStream()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
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

}
