package com.asm.bluetoothchat.utils

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.asm.bluetoothchat.R

object FragmentUtils {

    fun show(fragmentManager: FragmentManager, fragment: Fragment) {
        if (fragmentManager.fragments.size > 0)
            return
        fragmentManager.beginTransaction()
            .addToBackStack(null)
            .setReorderingAllowed(true)
            .add(R.id.fcv_main, fragment).commit()
    }
}