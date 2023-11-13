package com.asm.bluetoothchat.controller

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.os.Build
import com.asm.bluetoothchat.Constants
import com.asm.bluetoothchat.bluetooth.Device
import com.asm.bluetoothchat.permission.PermissionsManager
import java.lang.RuntimeException
import java.util.UUID

class MainController(
    private val activity: Activity,
    private val permissionsManager: PermissionsManager
) {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val devices = arrayListOf<Device>()
    var hasConnectPermission = false;
    var hasLocationPermission = false;
    var isBtActivated = false;

    init {
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !permissionsManager.havePermissionFor(activity, Manifest.permission.BLUETOOTH_CONNECT)
        ) {
            val permissions = arrayListOf<String>()
            permissions.add(Manifest.permission.BLUETOOTH_CONNECT)
            permissionsManager.requestPermission(
                activity,
                "Bluetooth request",
                "Bluetooth permission request needed",
                Constants.REQ_ALLOW_BLUETOOTH_CONNECT,
                permissions.toTypedArray()
            )
        } else
            hasConnectPermission = true;

        if (!permissionsManager.havePermissionFor(activity, Manifest.permission.ACCESS_FINE_LOCATION)) {
            val permissions = arrayListOf<String>()
            permissions.add(Manifest.permission.ACCESS_FINE_LOCATION)

            permissionsManager.requestPermission(
                activity,
                "Location Permission Needed",
                "Location Permission is needed to use bluetooth services",
                Constants.REQ_LOCATION_PERMISSION,
                permissions.toTypedArray()
            )
        } else
            hasLocationPermission = true;

        initBluetoothAdapter()
    }

    /**
     * Initializes the bluetooth adapter, checks if adapter is enabled
     */
    fun initBluetoothAdapter() {
        if (hasLocationPermission && hasConnectPermission) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
                bluetoothAdapter = bluetoothManager.adapter
            } else
                bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

            checkBTEnabled()
        }
    }

@SuppressLint("MissingPermission")
    private fun checkBTEnabled() {
        if (!bluetoothAdapter!!.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, Constants.REQ_ENABLE_BLUETOOTH)
        } else
            isBtActivated = true;
    }

    @SuppressLint("MissingPermission")
    private fun getPairedDevices() {
        if (bluetoothAdapter == null) return

        if(
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !permissionsManager.havePermissionFor(activity, Manifest.permission.BLUETOOTH_CONNECT)
        )
            throw RuntimeException("BLUETOOTH_CONNECT permission missing")

        val pairedDevices = bluetoothAdapter!!.bondedDevices
        pairedDevices.forEach {
            devices.add(Device(UUID.randomUUID(), it.name, it.address))
        }
    }

}