package com.asm.bluetoothchat.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import java.io.IOException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothServer(
    bluetoothAdapter: BluetoothAdapter,
    name: String,
    uuid: UUID
) : Thread() {
    private var serverSocket: BluetoothServerSocket
    init {
        try {
            serverSocket = bluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(name, uuid)
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
    }

    override fun run() {
        var shouldLoop = true
        while(shouldLoop) {
            var socket: BluetoothSocket?
            try {
                socket = serverSocket.accept()
            } catch (e: IOException) {
                e.printStackTrace()
                throw e
            }
            socket.also {
                manageConnectedSocket(socket)
                shouldLoop = false
                serverSocket.close()
            }
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {

    }

    fun cancel() {
        try {
            serverSocket.close()
        } catch (e: IOException) {
            e.printStackTrace()
            throw e
        }
        join()
    }
}