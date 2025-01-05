package com.example.blarduino.viewmodels

import android.content.Context
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.blarduino.App
import com.example.blarduino.Bluetooth
import com.example.blarduino.R
import com.example.blarduino.models.Gamepad

class GamepadViewModel(
    private val navController: NavController,
    private val bluetooth: Bluetooth,
    context: Context
) : ViewModel() {

    private val model: Gamepad = Gamepad(context)

    class Factory(private val navController: NavController) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App

            return GamepadViewModel(navController, app.bluetooth, app.applicationContext) as T
        }
    }

    fun gamepadButtonPress(button: Gamepad.Button) {
        val msg = model.getButtonPressMapping(button)
        bluetooth.writeMessage(msg ?: "")
    }

    fun gamepadButtonRelease(button: Gamepad.Button) {
        val msg = model.getButtonReleaseMapping(button)
        bluetooth.writeMessage(msg ?: "")
    }

    fun onEnableEdditingClick(view: View) {
        navController.navigate(R.id.gamepad_to_settings)
    }

    fun onBackButtonClick(view: View) {
        navController.popBackStack()
    }
}