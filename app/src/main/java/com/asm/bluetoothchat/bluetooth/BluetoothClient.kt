package com.asm.bluetoothchat.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothSocket
import android.os.Handler
import com.asm.bluetoothchat.Constants
import java.io.IOException
import java.lang.RuntimeException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothClient(
    private val bluetoothAdapter: BluetoothAdapter?,
    private val onGetSocket: (socket: BluetoothSocket) -> Unit
) : Thread() {
    private lateinit var socket: BluetoothSocket

    fun createClientConnection(device: BluetoothDevice) {
        socket = device.createRfcommSocketToServiceRecord(UUID.fromString(Constants.APP_UUID))
    }

    override fun run() {
        // Cancel discovery because it otherwise slows down the connection.
        bluetoothAdapter!!.cancelDiscovery()
        try {
            socket.connect()
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        onGetSocket(socket)
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