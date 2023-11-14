package com.asm.bluetoothchat.bluetooth

import android.bluetooth.BluetoothDevice
import java.util.UUID

class Device(
    val bluetoothDevice: BluetoothDevice
) {
    var isConnected = false
}