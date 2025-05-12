package com.payby.pos.ecr.ui.configure

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.bluetooth.BluetoothDevice
import com.payby.pos.ecr.bluetooth.DeviceDiscovery
import com.payby.pos.ecr.ui.CommonUiUtil
import com.payby.pos.ecr.ui.theme.mainThemeColor
import com.payby.pos.ecr.ui.theme.mediumFontFamily
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.utils.isEmpty
import com.payby.pos.ecr.utils.isValid

@Composable
fun ClassicBTConfig(modifier: Modifier) {
    val bluetoothDevices = remember { mutableStateListOf<BluetoothDevice>() }
    for (i in 0..50) {
        val bluetoothDevice = BluetoothDevice()
        bluetoothDevice.address = "00:11:22:33:44:55"
        bluetoothDevice.name = "Device $i"
        if (i == 23) {
            bluetoothDevice.isSelected = true
        }
        bluetoothDevices.add(bluetoothDevice)
    }
    val bindItem: @Composable (BluetoothDevice, Int) -> Unit = { item, _ ->
        Column {
            ItemView(item)
            CommonUiUtil.horizontalDivider()
        }
    }
    val state = rememberLazyListState()
    LazyColumn(modifier, state = state) {
        val items = bluetoothDevices.toList()
        itemsIndexed(items) { index, data -> bindItem(data, index) }
    }
}

@Composable
private fun ItemView(device: BluetoothDevice) {
    val text = if (device.name != null && device.name.isValid) {
        device.name + " - " + device.address
    } else {
        device.address
    }
    val modifier = Modifier.height(60.dp).padding(start = 16.dp, end = 16.dp)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val textStyle = TextStyle(color = textSecondaryColor, fontFamily = mediumFontFamily, fontSize = 16.sp)
        Text(text, style = textStyle)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) { BluetoothStatus(device) }
    }
}

@Composable
private fun BluetoothStatus(device: BluetoothDevice) {
    var status = ""
    if (device.isSelected) {
        status = "Connecting"
    } else if (device.isPaired) {
        status = "Connected"
    }
    if (status.isEmpty) return
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource("images/ic_select_right.png"), contentDescription = null, modifier = Modifier.size(24.dp), tint = mainThemeColor)
        Text(status, modifier = Modifier.padding(start = 8.dp), color = textSecondaryColor, textAlign = TextAlign.Start, fontSize = 16.sp, fontFamily = mediumFontFamily)
    }
}

object ClassicBTConfig {

    private fun xx() {
        val deviceDiscovery = DeviceDiscovery()
        deviceDiscovery.setOnDeviceDiscoveryListener {

        }
        deviceDiscovery.startDiscoveryDevice()
    }





}
