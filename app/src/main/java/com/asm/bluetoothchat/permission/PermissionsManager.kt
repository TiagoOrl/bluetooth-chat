package com.asm.bluetoothchat.permission

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class PermissionsManager {


    fun havePermissionFor(context: Context, permission: String): Boolean{
        return ContextCompat.checkSelfPermission(
            context,
            permission
        ) == PackageManager.PERMISSION_GRANTED
    }


    fun requestPermission(activity: Activity, title: String, msg: String, code: Int, permissions: Array<String>) {
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permissions[0]
            )
        ) {
            AlertDialog.Builder(activity)
                .setTitle(title)
                .setMessage(msg)
                .setPositiveButton(
                    "Accept"
                ) { dialog, which ->
                    ActivityCompat.requestPermissions(
                        activity,
                        permissions,
                        code
                    ) // non blocking method
                }
                .setNegativeButton(
                    "Deny"
                ) { dialog, which -> dialog.dismiss() }
                .create().show()
        } else {
            ActivityCompat.requestPermissions(
                activity,
                permissions,
                code
            ) // non blocking method
        }
    }
}