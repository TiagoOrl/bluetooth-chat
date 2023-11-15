package com.asm.bluetoothchat.bluetooth

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Handler
import android.os.Looper
import java.io.IOException
import java.lang.RuntimeException
import java.util.UUID

@SuppressLint("MissingPermission")
class BluetoothServer(
    bluetoothAdapter: BluetoothAdapter?,
    name: String,
    uuid: UUID,
    private val onGetSocket: (socket: BluetoothSocket) -> Unit
) : Thread() {
    private var serverSocket: BluetoothServerSocket
    init {
        if (bluetoothAdapter == null)
            throw RuntimeException("BluetoothServer: bluetoothAdapter is null")

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
                onGetSocket(socket)
                shouldLoop = false
                serverSocket.close()
            }
        }
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