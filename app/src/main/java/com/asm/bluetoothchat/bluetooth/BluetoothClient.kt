package com.asm.bluetoothchat.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import com.asm.bluetoothchat.Constants
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothClient(
    private val bluetoothAdapter: BluetoothAdapter?,
    bluetoothDevice: BluetoothDevice,
    private val onGetSocket: (socket: BluetoothSocket?) -> Unit,
    private val onClientConnError: (msg: String) -> Unit
) : Thread() {
    private var socket: BluetoothSocket? = null

    init {
        socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(Constants.APP_UUID))
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter!!.cancelDiscovery()
        try {
            socket?.connect()
        } catch (e: IOException) {
            e.printStackTrace()
            onClientConnError(e.toString())
        }
        onGetSocket(socket)
    }

}