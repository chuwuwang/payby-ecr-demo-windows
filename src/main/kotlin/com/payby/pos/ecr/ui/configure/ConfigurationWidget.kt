package com.payby.pos.ecr.ui.configure

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.payby.pos.ecr.bluetooth.BluetoothDevice
import com.payby.pos.ecr.ui.CommonUiUtil
import com.payby.pos.ecr.ui.theme.*

@Composable
fun showConfiguration(modifier: Modifier) {
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
    Row(modifier.background(backgroundColor), verticalAlignment = Alignment.CenterVertically) {
        sidebar()
        content(bluetoothDevices)
    }
}

@Composable
private fun RowScope.content(list: SnapshotStateList<BluetoothDevice>) {
    val modifier = Modifier.weight(3.0f).background(whiteColor)
    val bindItem: @Composable (BluetoothDevice, Int) -> Unit = { item, _ ->
        Column {
            Row(Modifier.height(50.dp).padding(start = 16.dp), verticalAlignment = Alignment.CenterVertically) {
                val text = if (item.name != null && item.name.length > 0) {
                    item.name + " - " + item.address
                } else {
                    item.address
                }
                val textStyle = TextStyle(color = textSecondaryColor, fontFamily = mediumFontFamily, fontSize = 16.sp)
                Text(text, style = textStyle)
                if (item.isSelected) {
                    Icon(painter = painterResource("images/ic_select_right.png"), contentDescription = null, modifier = Modifier.size(24.dp)., tint = mainThemeColor)
                }
            }
            CommonUiUtil.horizontalDivider()
        }
    }
    Column(modifier) {
        val state = rememberLazyListState()
        LazyColumn(state = state) {
            val items = list.toList()
            itemsIndexed(items) { index, data -> bindItem(data, index) }
        }
    }
}

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

@Composable
private fun RowScope.sidebar() {
    val modifier = Modifier.height(IntrinsicSize.Min).weight(1.3f).padding(start = 16.dp, end = 16.dp).clip(radius_8).background(whiteColor)
    Column(modifier) {
        menuItem("Bluetooth EDR", "images/ic_unselect.png", mainThemeColor)
    }
}

@Composable
private fun menuItem(text: String, resourcePath: String, tint: Color) {
    val marginStart = 24.dp
    val paddingStart = 12.dp
    Row(Modifier.fillMaxWidth().height(60.dp).padding(start = marginStart), verticalAlignment = Alignment.CenterVertically) {
        Icon(painter = painterResource(resourcePath), contentDescription = null, modifier = Modifier.size(24.dp), tint = tint)
        Text(modifier = Modifier.padding(start = paddingStart), color = textMainColor, textAlign = TextAlign.Start, fontSize = 16.sp, fontFamily = mediumFontFamily, text = text)
    }
}
