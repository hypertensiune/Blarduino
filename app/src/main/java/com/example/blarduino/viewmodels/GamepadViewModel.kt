package com.example.blarduino.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.blarduino.R
import com.example.blarduino.models.Gamepad

class GamepadViewModel(private val navController: NavController, context: Context) : ViewModel() {

    private val model: Gamepad = Gamepad(context)

    fun gamepadButtonPress(button: Gamepad.Button) {
        val msg = model.getButtonPressMapping(button)
    }

    fun gamepadButtonRelease(button: Gamepad.Button) {
        val msg = model.getButtonReleaseMapping(button)
    }

    fun onEnableEdditingClick(view: View) {
        navController.navigate(R.id.gamepad_to_settings)
    }

    fun onBackButtonClick(view: View) {
        navController.popBackStack()
    }
}