package com.asm.bluetoothchat.bluetooth

import java.util.UUID

class Device(
    val uuid: UUID,
    val name: String,
    val macAddress: String
) {
    var isConnected = false
}