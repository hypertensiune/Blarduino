package com.example.blarduino.fragments

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.blarduino.MessageListAdapter
import com.example.blarduino.R
import com.example.blarduino.databinding.FragmentTerminalBinding
import com.example.blarduino.viewmodels.GamepadViewModel
import com.example.blarduino.viewmodels.TerminalViewModel

class Terminal : Fragment() {

    private lateinit var binding: FragmentTerminalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_terminal, container, false)

        val viewModel = ViewModelProvider(requireActivity(), TerminalViewModel.Factory(container!!.findNavController()))[TerminalViewModel::class.java]

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner
        binding.messageRecyclerView.layoutManager = LinearLayoutManager(context).apply {
            stackFromEnd = true
            reverseLayout = false
        }

        binding.textBar.setEndIconOnClickListener {
            viewModel.sendMessage()
        }

        viewModel.messages.observe(viewLifecycleOwner) {
            val adapter = MessageListAdapter(requireContext(), it)
            binding.messageRecyclerView.adapter = adapter
            binding.messageRecyclerView.scrollToPosition(it.size - 1)
        }

        return binding.root
    }
}