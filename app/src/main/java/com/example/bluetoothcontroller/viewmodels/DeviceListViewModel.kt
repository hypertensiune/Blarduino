package com.example.bluetoothcontroller.viewmodels

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import androidx.core.content.ContextCompat.getSystemService
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController

@SuppressLint("MissingPermission")
class DeviceListViewModel(private val navController: NavController) : ViewModel() {

    val devices: MutableLiveData<Set<BluetoothDevice>> = MutableLiveData<Set<BluetoothDevice>>()

    init {
        val bluetoothManager = getSystemService(navController.context, BluetoothManager::class.java)
        val adapter: BluetoothAdapter? = bluetoothManager?.adapter

        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
        devices.value = pairedDevices!!
    }
}