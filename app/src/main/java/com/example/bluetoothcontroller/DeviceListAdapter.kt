package com.example.bluetoothcontroller

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import com.example.bluetoothcontroller.databinding.DeviceListItemBinding

class DeviceListAdapter(
    context: Context,
    private val devices: Set<BluetoothDevice>?,
    private val onItemClick: (Any) -> Unit
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    private lateinit var binding: DeviceListItemBinding

    class ViewHolder(
        private val binding: DeviceListItemBinding,
        private val _bind: (BluetoothDevice, DeviceListItemBinding) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: BluetoothDevice) = _bind(element, binding)
    }

    override fun getItemCount(): Int = devices?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        devices?.elementAt(position)?.let { holder.bind(it) }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DeviceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding) { element, binding ->
            binding.name = element.name
            binding.mac = element.address
        }
    }
}