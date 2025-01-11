package com.example.blarduino

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import androidx.recyclerview.widget.RecyclerView
import com.example.blarduino.databinding.DeviceListItemBinding
import com.example.blarduino.databinding.MessageItemBinding
import com.example.blarduino.models.TerminalMessage

class MessageListAdapter(
    context: Context,
    private val messages: List<TerminalMessage>?,
) : RecyclerView.Adapter<MessageListAdapter.ViewHolder>() {

    private lateinit var binding: MessageItemBinding

    class ViewHolder(
        private val binding: MessageItemBinding,
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(element: TerminalMessage) {
            binding.message = element.message

            if(element.sent) {
                binding.container.gravity = Gravity.END
            } else {
                binding.container.gravity = Gravity.START
            }
        }
    }

    override fun getItemCount(): Int = messages?.size ?: 0

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        messages?.elementAt(position)?.let { holder.bind(it) }
    }

    @SuppressLint("MissingPermission")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        binding = MessageItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }
}