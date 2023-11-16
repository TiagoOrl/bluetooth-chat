package com.asm.bluetoothchat.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.asm.bluetoothchat.R
import com.asm.bluetoothchat.bluetooth.Device
import com.asm.bluetoothchat.controller.MainController
import com.asm.bluetoothchat.databinding.DevicesPairedFragmentBinding
import com.asm.bluetoothchat.ui.adapter.DevicesAdapter

class PairedDevicesFragment(
    private val mainController: MainController,
    private val onClickConnect: (device: Device) -> Unit
) : Fragment(R.layout.devices_paired_fragment) {
    private lateinit var binding: DevicesPairedFragmentBinding
    private val adapter by lazy {
        DevicesAdapter(requireActivity(), onClickConnect)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DevicesPairedFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initViews()
        adapter.updateList(mainController.getPairedDevices())
    }

    private fun initViews() {
        binding.rvPairedListFrag.adapter = adapter
        binding.rvPairedListFrag.layoutManager = LinearLayoutManager(requireContext())
    }
}