package com.example.bluetoothcontroller.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bluetoothcontroller.DeviceListAdapter
import com.example.bluetoothcontroller.R
import com.example.bluetoothcontroller.databinding.FragmentDeviceListBinding
import com.example.bluetoothcontroller.viewmodels.DeviceListViewModel

class DeviceList : Fragment() {

    private lateinit var binding: FragmentDeviceListBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_device_list, container, false)

        val viewModel = DeviceListViewModel(container!!.findNavController())

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.deviceListRecyclerView.layoutManager = LinearLayoutManager(context)

        viewModel.devices.observe(viewLifecycleOwner) {
            val adapter = DeviceListAdapter(requireContext(), viewModel.devices.value) {

            }
            binding.deviceListRecyclerView.adapter = adapter
        }

        return binding.root
    }

}