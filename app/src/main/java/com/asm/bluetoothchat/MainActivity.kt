package com.asm.bluetoothchat

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asm.bluetoothchat.controller.MainController
import com.asm.bluetoothchat.permission.PermissionsManager

class MainActivity : AppCompatActivity() {
    private lateinit var mainController: MainController
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mainController = MainController(this, PermissionsManager())
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == Constants.REQ_LOCATION_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Location permission for location granted.")
                mainController.initBluetoothAdapter()
                mainController.hasLocationPermission = true;
            }
        }

        if (requestCode == Constants.REQ_ALLOW_BLUETOOTH_CONNECT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Location permission for bluetooth connect granted.")
                mainController.hasConnectPermission = true;
            }
        }

        if (requestCode == Constants.REQ_ENABLE_BLUETOOTH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Bluetooth enabled event")
                mainController.isBtActivated = true;
            }
        }
    }

}