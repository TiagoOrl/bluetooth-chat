package com.asm.bluetoothchat

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.asm.bluetoothchat.controller.MainController
import com.asm.bluetoothchat.databinding.ActivityMainBinding
import com.asm.bluetoothchat.permission.PermissionsManager
import com.asm.bluetoothchat.utils.HelperUI
import java.nio.charset.Charset
import java.text.MessageFormat

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var mainController: MainController

    @SuppressLint("MissingPermission")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        mainController = MainController(this, PermissionsManager(), Handler(Looper.getMainLooper()), {
            size, buffer ->
                val msg = String(buffer,0, size, Charset.defaultCharset())
                binding.tvChatMain.text = MessageFormat.format(
                    "{0} \nPeer: {1}",
                    binding.tvChatMain.text,
                    msg
                )
        }, {
            binding.tvConnectionStatus.text = it
        })

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

        if (requestCode == Constants.REQ_BLUETOOTH_SCAN) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                println("Bluetooth SCAN enabled event")
                mainController.hasScanPermission = true
                mainController.initBluetooth()
            }

        }
    }

    private fun initViews() {
        binding.btnGetPaired.setOnClickListener {
            mainController.showPairedDevices()
        }

        binding.btnSendMsg.setOnClickListener {
            HelperUI.closeKeyboard(this)
            if (binding.etChat.text!!.isNotEmpty()) {
                mainController.sendMessage(binding.etChat.text.toString())

                binding.tvChatMain.text = MessageFormat.format(
                    "{0} \nYou: {1}",
                    binding.tvChatMain.text,
                    binding.etChat.text.toString()
                )
            }
        }
    }

}