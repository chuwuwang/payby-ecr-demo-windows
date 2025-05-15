package com.payby.pos.ecr.ui.configure

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.dokar.sonner.Toaster
import com.dokar.sonner.ToasterState
import com.dokar.sonner.rememberToasterState
import com.payby.pos.ecr.connect.BluetoothDevice
import com.payby.pos.ecr.connect.ClassicBTManager
import com.payby.pos.ecr.connect.ConnectionCore
import com.payby.pos.ecr.connect.ConnectionListener
import com.payby.pos.ecr.ui.theme.boldFontFamily
import com.payby.pos.ecr.ui.theme.mainThemeColor
import com.payby.pos.ecr.ui.theme.mediumFontFamily
import com.payby.pos.ecr.ui.theme.textSecondaryColor
import com.payby.pos.ecr.ui.widget.CommonUiUtil
import com.payby.pos.ecr.ui.widget.DialogHelper.LoadingDialog
import com.payby.pos.ecr.utils.ThreadPoolManager
import com.payby.pos.ecr.utils.isEmpty
import com.payby.pos.ecr.utils.isValid

private var connectingDevice: BluetoothDevice ? = null

@Composable
fun ClassicBTConfigScreen(modifier: Modifier, viewModel: ConfigurationViewModel) {
    val toaster = rememberToasterState()
    val isLoading = remember { mutableStateOf(false) }

    DisposableEffect(Unit) {
        connectingDevice = null
        if (ConnectionCore.isConnected == false) {
            viewModel.resetConnectionStatus()
        }
        val listener = object : ConnectionListener {

            override fun onConnected() {
                isLoading.value = false
                toaster.show("Connection successful")
                changeBluetoothStatus(viewModel.bluetoothDevices, BluetoothDevice.STATUS_CONNECTED)
                connectingDevice = null
            }

            override fun onDisconnected(message: String) {
                isLoading.value = false
                toaster.show(message)
                changeBluetoothStatus(viewModel.bluetoothDevices, BluetoothDevice.STATUS_DISCONNECTED)
                connectingDevice = null
            }

            override fun onMessage(bytes: ByteArray) { }

        }
        ConnectionCore.addListener(listener)
        onDispose { ConnectionCore.removeListener(listener) }
    }

    val bindItem: @Composable (BluetoothDevice, Int) -> Unit = { item, _ ->
        Column {
            ItemView(item, isLoading, toaster)
            CommonUiUtil.horizontalDivider()
        }
    }
    val state = rememberLazyListState()
    LazyColumn(modifier, state = state) {
        val items = viewModel.bluetoothDevices
        itemsIndexed(items) { index, data -> bindItem(data, index) }
    }
    Toaster(state = toaster)
    LoadingDialog("CONNECTING...", isLoading.value)

    if (viewModel.bluetoothDevices.size > 0) return
    ThreadPoolManager.executeCacheTask { ClassicBTManager.startDiscovery(viewModel.bluetoothDevices) }
}

private fun changeBluetoothStatus(bluetoothDevices: SnapshotStateList<BluetoothDevice>, state: Int) {
    val device = connectingDevice ?: return
    val address = device.address
    bluetoothDevices.forEach { bluetoothDevice ->
        if (bluetoothDevice.address == address) bluetoothDevice.status.value = state
    }
}

@Composable
private fun ItemView(device: BluetoothDevice, isLoading: MutableState<Boolean>, toaster: ToasterState) {
    val text = if (device.name != null && device.name.isValid) {
        device.name + " - " + device.address
    } else {
        device.address
    }
    val modifier = Modifier.height(60.dp).clickable {
        if (ConnectionCore.isConnected) {
            toaster.show("Bluetooth is already connected. Multiple Bluetooth devices are not allowed to be connected.")
        } else {
            device.status.value = BluetoothDevice.STATUS_CONNECTING
            connectingDevice = device
            isLoading.value = true
            ThreadPoolManager.executeCacheTask { ConnectionCore.connectWithClassicBT(device) }
        }
    }.padding(start = 16.dp, end = 16.dp)
    Row(modifier, verticalAlignment = Alignment.CenterVertically) {
        val textStyle = TextStyle(color = textSecondaryColor, fontFamily = mediumFontFamily, letterSpacing = 1.sp, fontSize = 16.sp)
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
