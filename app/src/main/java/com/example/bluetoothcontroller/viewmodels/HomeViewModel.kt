package com.example.bluetoothcontroller.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.bluetoothcontroller.R

class HomeViewModel(private val navController: NavController) : ViewModel() {

    fun onDevicesButtonClick(view: View) {
        navController.navigate(R.id.home_to_devices)
    }
}