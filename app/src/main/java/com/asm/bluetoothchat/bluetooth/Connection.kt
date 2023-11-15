package com.asm.bluetoothchat.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


class Connection(
    private val handler: Handler,
    private val socket: BluetoothSocket,
    private val onReceiveMsg: (size: Int, buffer: ByteArray) -> Unit
) : Thread() {
    private val inStream: InputStream = socket.inputStream
    private val outStream: OutputStream = socket.outputStream
    private val buffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream


    override fun run() {
        var numBytes: Int

        while(true) {
            try {
                numBytes = inStream.read(buffer) // blocking op
            } catch (e: IOException) {
                e.printStackTrace()
                break
            }

            handler.post {
                onReceiveMsg(numBytes, buffer)
            }
        }
    }

    fun write(bytes: ByteArray) {
        try {
            outStream.write(bytes)
        } catch (e: IOException) {
            e.printStackTrace()
            return
        }
    }

    // Call this method from the main activity to shut down the connection.
    fun cancel() {
        try {
            socket.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

}
