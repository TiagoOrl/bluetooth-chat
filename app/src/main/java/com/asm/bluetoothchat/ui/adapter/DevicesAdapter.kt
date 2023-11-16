package com.asm.bluetoothchat.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.asm.bluetoothchat.R
import com.asm.bluetoothchat.bluetooth.Device
import com.asm.bluetoothchat.databinding.CardDeviceItemBinding

class DevicesAdapter(
    private val activity: FragmentActivity,
    private val onClickConnect: (device: Device) -> Unit
) : RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>() {
    private lateinit var devices: ArrayList<Device>


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DeviceViewHolder {
        val binding = CardDeviceItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return DeviceViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return devices.size
    }

    override fun onBindViewHolder(holder: DeviceViewHolder, position: Int) {
        holder.bind(devices[position])
    }

    fun updateList(list: ArrayList<Device>?) {
        if (list == null)
            return

        devices = list
        notifyDataSetChanged()
    }


    inner class DeviceViewHolder(
        private val binding: CardDeviceItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("MissingPermission")
        fun bind(device: Device) {
            binding.tvDeviceName.text = device.bluetoothDevice.name
            binding.tvDeviceMac.text = device.bluetoothDevice.address
            binding.ibDeviceConnect

            if (device.isConnected)
                binding.ibDeviceConnect.setImageResource(R.drawable.baseline_check_box_24)
            else
                binding.ibDeviceConnect.setImageResource(R.drawable.baseline_check_box_outline_blank_24)

            binding.ibDeviceConnect.setOnClickListener {
                if (!device.isConnected) {
                    onClickConnect(device)
                    binding.ibDeviceConnect.setImageResource(R.drawable.baseline_check_box_24)
                    activity.supportFragmentManager.popBackStackImmediate()
                }
            }
        }
    }

}