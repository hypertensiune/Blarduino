package com.example.blarduino.fragments

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.service.voice.VoiceInteractionSession.ActivityId
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import com.example.blarduino.R
import com.example.blarduino.databinding.FragmentHomeBinding
import com.example.blarduino.viewmodels.HomeViewModel

class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)

        val viewModel = HomeViewModel(container!!.findNavController())

        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        return binding.root
    }

}