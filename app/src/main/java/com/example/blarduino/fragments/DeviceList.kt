package com.example.blarduino.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.blarduino.Bluetooth
import com.example.blarduino.DeviceListAdapter
import com.example.blarduino.R
import com.example.blarduino.databinding.FragmentDeviceListBinding
import com.example.blarduino.viewmodels.DeviceListViewModel

class DeviceList : Fragment() {

    private lateinit var binding: FragmentDeviceListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_list, container, false)

//        val viewModel = DeviceListViewModel(container!!.findNavController())
        val viewModel = ViewModelProvider(requireActivity(), DeviceListViewModel.Factory(container!!.findNavController()))[DeviceListViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.deviceListRecyclerView.layoutManager = LinearLayoutManager(context)

        val deviceIndex = viewModel.bluetoothAlreadyConnected()
        val adapter = DeviceListAdapter(requireContext(), viewModel.devices, deviceIndex) {
            viewModel.onDeviceItemClick(it)
        }
        binding.deviceListRecyclerView.adapter = adapter

        viewModel.connectionState.observe(viewLifecycleOwner) {
            val index = it.first
            val previousConnection = it.second

            if(index == -1) {
                Toast.makeText(requireContext(), "Connection to device failed!", Toast.LENGTH_LONG).show()
            } else {
                if(!previousConnection) {
                    Toast.makeText(requireContext(), "Connection successful", Toast.LENGTH_SHORT).show()
                }
                val vh = binding.deviceListRecyclerView.findViewHolderForAdapterPosition(index) as DeviceListAdapter.ViewHolder
                vh.highlight()
            }
        }

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return binding.root
    }
}