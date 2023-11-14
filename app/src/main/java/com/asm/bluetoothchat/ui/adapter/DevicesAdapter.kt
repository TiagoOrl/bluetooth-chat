package com.asm.bluetoothchat.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.asm.bluetoothchat.R
import com.asm.bluetoothchat.bluetooth.Device
import com.asm.bluetoothchat.databinding.CardDeviceItemBinding

class DevicesAdapter : RecyclerView.Adapter<DevicesAdapter.DeviceViewHolder>() {
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
        fun bind(device: Device) {
            binding.tvDeviceName.text = device.name
            binding.tvDeviceMac.text = device.macAddress
            binding.ibDeviceConnect

            if (device.isConnected)
                binding.ibDeviceConnect.setBackgroundResource(R.drawable.baseline_check_box_24)
            else
                binding.ibDeviceConnect.setBackgroundResource(R.drawable.baseline_check_box_outline_blank_24)
        }
    }

}