package com.example.blarduino.viewmodels

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.blarduino.R

class HomeViewModel(private val navController: NavController) : ViewModel() {

    fun onDevicesButtonClick(view: View) {
        navController.navigate(R.id.home_to_devices)
    }
}