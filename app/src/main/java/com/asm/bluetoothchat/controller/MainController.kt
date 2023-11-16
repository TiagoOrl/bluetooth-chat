package com.asm.bluetoothchat.controller

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Intent
import android.os.Build
import android.os.Handler
import androidx.fragment.app.FragmentActivity
import com.asm.bluetoothchat.Constants
import com.asm.bluetoothchat.bluetooth.BluetoothClient
import com.asm.bluetoothchat.bluetooth.BluetoothServer
import com.asm.bluetoothchat.bluetooth.Connection
import com.asm.bluetoothchat.bluetooth.Device
import com.asm.bluetoothchat.permission.PermissionsManager
import com.asm.bluetoothchat.ui.fragment.PairedDevicesFragment
import com.asm.bluetoothchat.utils.FragmentUtils
import java.lang.RuntimeException
import java.util.UUID

@SuppressLint("MissingPermission")
class MainController(
    private val activity: FragmentActivity,
    private val permissionsManager: PermissionsManager,
    private val handler: Handler,
    private val onReceiveMsg: (size: Int, buffer: ByteArray) -> Unit,
    private val onGetConnection: (deviceName: String) -> Unit
) {
    private var bluetoothAdapter: BluetoothAdapter? = null
    private val devices = arrayListOf<Device>()
    private lateinit var bluetoothServer: BluetoothServer
    private lateinit var bluetoothClient: BluetoothClient
    private var connection: Connection? = null
    private var pairedDevicesFragment: PairedDevicesFragment
    var hasConnectPermission = false;
    var hasLocationPermission = false;
    var hasScanPermission = false
    var isBtActivated = false;

    init {
        pairedDevicesFragment = PairedDevicesFragment(this) { device ->
            if (haveRequiredPermissions()) {
                bluetoothClient.createClientConnection(device.bluetoothDevice)
                device.isConnected = true
                bluetoothClient.start()
            } else
                requestNeededPermissions()
        }
        requestNeededPermissions()
        initBluetooth()
    }

    /**
     * Initializes the bluetooth adapter, checks if adapter is enabled
     */
    fun initBluetooth() {
        if (!haveRequiredPermissions()) {
            requestNeededPermissions()
            return
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val bluetoothManager: BluetoothManager = activity.getSystemService(BluetoothManager::class.java)
            bluetoothAdapter = bluetoothManager.adapter
        } else
            bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if (bluetoothAdapter == null)
            throw RuntimeException("bluetoothAdapter is not available.")

        if (!bluetoothAdapter!!.isEnabled) {
            val intent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(intent, Constants.REQ_ENABLE_BLUETOOTH)
        } else {
            isBtActivated = true

            val onGetSocket = { socket: BluetoothSocket ->
                handler.post { onGetConnection(socket.remoteDevice.name) }
                connection = Connection(handler, socket, onReceiveMsg)
                connection!!.start()
            }

            initClientAndServer(onGetSocket)
        }
    }

    @SuppressLint("MissingPermission")
    fun getPairedDevices() : ArrayList<Device>? {
        if (devices.size > 0)
            return devices
        if (!haveRequiredPermissions()) {
            requestNeededPermissions()
            return null
        }
        if (bluetoothAdapter == null)
            throw RuntimeException("bluetoothadapter is null")

        val pairedDevices = bluetoothAdapter!!.bondedDevices
        pairedDevices.forEach {
            devices.add(Device(it))
        }

        return devices
    }

    private fun initClientAndServer(onGetSocket: (socket: BluetoothSocket) -> Unit) {
        bluetoothClient = BluetoothClient(
            bluetoothAdapter,
            onGetSocket
        )

        bluetoothServer = BluetoothServer(
            bluetoothAdapter,
            Constants.APP_NAME,
            UUID.fromString(Constants.APP_UUID),
            onGetSocket
        )
        bluetoothServer.start()
    }

    /**
     * checks if has granted locations and bluetooth connect permission, request if its not granted
     */
    private fun requestNeededPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S &&
            !permissionsManager.havePermissionFor(activity, Manifest.permission.BLUETOOTH_SCAN)
            ) {
            val permissions = arrayListOf<String>()
            permissions.add(Manifest.permission.BLUETOOTH_SCAN)
            permissionsManager.requestPermission(
                activity,
                "Bluetooth request",
                "Bluetooth permission SCAN needed",
                Constants.REQ_BLUETOOTH_SCAN,
                permissions.toTypedArray()
            )
        } else
            hasScanPermission = true

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
    }

    fun showPairedDevices() {
        FragmentUtils.show(activity.supportFragmentManager, pairedDevicesFragment)
    }

    fun sendMessage(msg: String) {
        if (connection != null)
            connection!!.write(msg.encodeToByteArray())
        else
            println("MainController: could not send message, connection is null")
    }

    private fun haveRequiredPermissions() : Boolean {
       return (hasLocationPermission && hasConnectPermission && hasScanPermission)
    }

}