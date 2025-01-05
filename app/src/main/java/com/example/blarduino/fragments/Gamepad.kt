package com.example.blarduino.fragments

import android.annotation.SuppressLint
import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.constraintlayout.widget.ConstraintSet.Motion
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.example.blarduino.viewmodels.GamepadViewModel
import com.example.blarduino.R
import com.example.blarduino.databinding.FragmentGamepadBinding
import com.example.blarduino.models.Gamepad
import com.google.android.material.button.MaterialButton

class Gamepad : Fragment() {

    private lateinit var binding: FragmentGamepadBinding

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_gamepad, container, false)

//        val viewModel = GamepadViewModel(container!!.findNavController(), requireContext())
        val viewModel = ViewModelProvider(requireActivity(), GamepadViewModel.Factory(container!!.findNavController()))[GamepadViewModel::class.java]
        binding.viewModel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

//        requireActivity().requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        requireActivity().window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)

        setButtonTouchListener(binding.dpadDown, Gamepad.Button.DPAD_DOWN)
        setButtonTouchListener(binding.dpadLeft, Gamepad.Button.DPAD_LEFT)
        setButtonTouchListener(binding.dpadUp, Gamepad.Button.DPAD_UP)
        setButtonTouchListener(binding.dpadRight, Gamepad.Button.DPAD_RIGHT)

        setButtonTouchListener(binding.triangle, Gamepad.Button.TRIANGLE)
        setButtonTouchListener(binding.circle, Gamepad.Button.CIRCLE)
        setButtonTouchListener(binding.x, Gamepad.Button.X)
        setButtonTouchListener(binding.square, Gamepad.Button.SQUARE)

        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setButtonTouchListener(view: MaterialButton, button: Gamepad.Button) {
        view.setOnTouchListener { _, event ->
            when(event.action) {
                MotionEvent.ACTION_DOWN -> binding.viewModel?.gamepadButtonPress(button)
                MotionEvent.ACTION_UP -> binding.viewModel?.gamepadButtonRelease(button)
            }
            false
        }
    }
}