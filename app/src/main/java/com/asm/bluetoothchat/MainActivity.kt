package com.asm.bluetoothchat

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.asm.bluetoothchat.controller.MainController
import com.asm.bluetoothchat.databinding.ActivityMainBinding
import com.asm.bluetoothchat.permission.PermissionsManager

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainController: MainController
    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainController = MainController(this, PermissionsManager())
        initViews()
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
                mainController.hasLocationPermission = true;
                mainController.initBluetooth()
            }
        }

        if (requestCode == Constants.REQ_ALLOW_BLUETOOTH_CONNECT) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Location permission for bluetooth connect granted.")
                mainController.hasConnectPermission = true;
                mainController.initBluetooth()
            }
        }

        if (requestCode == Constants.REQ_ENABLE_BLUETOOTH) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Bluetooth enabled event")
                mainController.isBtActivated = true;
                mainController.initBluetooth()
            }
        }
    }

    private fun initViews() {
        binding.btnGetPaired.setOnClickListener {
            mainController.getPairedDevices()
        }
    }

}