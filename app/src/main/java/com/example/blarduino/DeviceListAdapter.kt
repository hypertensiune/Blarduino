package com.example.blarduino

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.blarduino.databinding.DeviceListItemBinding

class DeviceListAdapter(
    private val context: Context,
    private val devices: Set<Bluetooth.Device>?,
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<DeviceListAdapter.ViewHolder>() {

    private lateinit var binding: DeviceListItemBinding

    class ViewHolder(
        private val context: Context,
        private val binding: DeviceListItemBinding,
        private val _bind: (Bluetooth.Device, DeviceListItemBinding) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: Bluetooth.Device) = _bind(element, binding)

        fun highlight() {
            binding.root.backgroundTintList = ColorStateList.valueOf(context.resources.getColor(R.color.selected))
        }
    }

    override fun getItemCount(): Int = devices?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        devices?.elementAt(position)?.let {
            holder.bind(it)
            if(it.connected) { holder.highlight() }
        }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = DeviceListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(context, binding) { element, binding ->
            binding.name = element.name + if(element.connecting) " (Connecting...)" else ""
            binding.mac = element.address
            binding.root.setOnClickListener {
                onItemClick(element.address)
            }
        }
    }
}