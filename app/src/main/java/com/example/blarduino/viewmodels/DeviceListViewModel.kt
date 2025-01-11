package com.example.blarduino.viewmodels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.os.Handler
import android.view.View
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.blarduino.App
import com.example.blarduino.Bluetooth
import com.example.blarduino.fragments.DeviceList

@SuppressLint("MissingPermission")
class DeviceListViewModel(
    private val navController: NavController,
    private val bluetooth: Bluetooth
) : ViewModel(), Bluetooth.ConnectionStateListener {

    companion object {
        const val CONNECTION_SUCCESSFUL = 0
        const val CONNECTION_FAILED = 1
    }

    var devices: Set<Bluetooth.Device>? = null
    val connectionState: MutableLiveData<Int> = MutableLiveData()

    class Factory(private val navController: NavController) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App

            return DeviceListViewModel(navController, app.bluetooth) as T
        }
    }

    init {
        devices = bluetooth.getPairedDevices()
        bluetooth.addConnectionStateListener(this)
    }

    override fun onConnectionSuccessful(index: Int) {
        devices?.forEach { it.connected = false }
        devices?.elementAtOrNull(index)?.connected = true
        connectionState.postValue(CONNECTION_SUCCESSFUL)
    }

    override fun onConnectionFailed() {
        connectionState.postValue(CONNECTION_FAILED)
    }

    fun onDeviceItemClick(deviceAddress: String) {
        bluetooth.connect(deviceAddress)
    }

    fun onBackClick(view: View) {
        navController.popBackStack()
    }
}