package com.payby.pos.ecr.ui.configure

import androidx.compose.foundation.clickable
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
import com.payby.pos.ecr.connect.BluetoothDevice
import com.payby.pos.ecr.connect.ClassicBTManager
import com.payby.pos.ecr.ui.CommonUiUtil
import com.payby.pos.ecr.ui.theme.boldFontFamily
import com.payby.pos.ecr.ui.theme.mainThemeColor
import com.payby.pos.ecr.ui.theme.mediumFontFamily
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.utils.isEmpty
import com.payby.pos.ecr.utils.isValid

@Composable
fun ClassicBTConfig(modifier: Modifier) {
    val bluetoothDevices = remember { mutableStateListOf<BluetoothDevice>() }
    for (i in 0..8) {
        val bluetoothDevice = BluetoothDevice()
        bluetoothDevice.address = "00:11:22:33:44:55"
        bluetoothDevice.name = "Device $i"
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
    // ThreadPoolManager.executeCacheTask { ClassicBTManager.startDiscovery(bluetoothDevices) }
}

@Composable
private fun ItemView(device: BluetoothDevice) {
    val text = if (device.name != null && device.name.isValid) {
        device.name + " - " + device.address
    } else {
        device.address
    }
    val modifier = Modifier.height(60.dp).clickable {
        device.status.value = BluetoothDevice.STATUS_CONNECTING
        ClassicBTManager.connect(device)
    }.padding(start = 16.dp, end = 16.dp)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val textStyle = TextStyle(color = textSecondaryColor, fontFamily = mediumFontFamily, fontSize = 16.sp)
        Text(text, style = textStyle)
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.CenterEnd) { BluetoothStatus(device) }
    }
}

@Composable
private fun BluetoothStatus(device: BluetoothDevice) {
    var status = ""
    val value = device.status.value
    if (value == BluetoothDevice.STATUS_PAIRED) {
        status = "Paired"
    } else if (value == BluetoothDevice.STATUS_CONNECTED) {
        status = "Connected"
    } else if (value == BluetoothDevice.STATUS_CONNECTING) {
        status = "Connecting"
    }
    if (status.isEmpty) return
    Row(verticalAlignment = Alignment.CenterVertically) {
        if (value == BluetoothDevice.STATUS_CONNECTED) {
            Icon(painter = painterResource("images/ic_select_right.png"), contentDescription = null, modifier = Modifier.padding(end = 8.dp).size(24.dp), tint = mainThemeColor)
        }
        Text(status, color = mainThemeColor, textAlign = TextAlign.Start, fontSize = 16.sp, fontFamily = boldFontFamily)
    }
}
