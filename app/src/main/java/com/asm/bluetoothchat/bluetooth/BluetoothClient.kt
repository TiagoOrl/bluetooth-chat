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
    bluetoothDevice: BluetoothDevice,
    private val bluetoothAdapter: BluetoothAdapter
) : Thread() {
    private var socket: BluetoothSocket
    init {
        socket = bluetoothDevice.createRfcommSocketToServiceRecord(UUID.fromString(Constants.APP_UUID))
    }
    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter.cancelDiscovery()
        try {
            socket.connect()
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        manageConnectedSocket(socket)
    }

    private fun manageConnectedSocket(bluetoothSocket: BluetoothSocket) {

    }

    fun cancel() {
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }
}