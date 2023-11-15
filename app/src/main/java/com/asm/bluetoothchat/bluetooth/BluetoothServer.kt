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
    private val handler: Handler,
    bluetoothAdapter: BluetoothAdapter?,
    name: String,
    uuid: UUID,
    private val receiveCallback: (size: Int, buffer: ByteArray) -> Unit
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
                manageConnectedSocket(socket)
                shouldLoop = false
                serverSocket.close()
            }
        }
    }

    private fun manageConnectedSocket(socket: BluetoothSocket) {
        val connection = Connection(handler, socket, receiveCallback)
        connection.start()
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