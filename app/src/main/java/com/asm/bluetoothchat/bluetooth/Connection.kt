package com.asm.bluetoothchat.bluetooth

import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream


// Defines several constants used when transmitting messages between the
// service and the UI.
const val MESSAGE_READ: Int = 0
const val MESSAGE_WRITE: Int = 1
const val MESSAGE_TOAST: Int = 2

class Connection(private val handler: Handler) {
    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {
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

                // Send the obtained bytes to the UI activity.
                val readMsg = handler.obtainMessage(
                    MESSAGE_READ, numBytes, -1,
                    buffer)
                readMsg.sendToTarget()

            }
        }

        fun write(bytes: ByteArray) {
            try {
                outStream.write(bytes)
            } catch (e: IOException) {
                e.printStackTrace()
                // Send a failure message back to the activity.
                val writeErrorMsg = handler.obtainMessage(MESSAGE_TOAST)
                val bundle = Bundle().apply {
                    putString("toast", "Couldn't send data to the other device")
                }
                writeErrorMsg.data = bundle
                handler.sendMessage(writeErrorMsg)
                return

            }
            // Share the sent message with the UI activity.
            val writtenMsg = handler.obtainMessage(
                MESSAGE_WRITE, -1, -1, buffer)
            writtenMsg.sendToTarget()
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
}