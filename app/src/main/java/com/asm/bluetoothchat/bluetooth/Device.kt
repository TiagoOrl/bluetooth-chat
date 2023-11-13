package com.asm.bluetoothchat.bluetooth

import java.util.UUID

data class Device(
    private val uuid: UUID,
    private val name: String,
    private val macAddress: String
)