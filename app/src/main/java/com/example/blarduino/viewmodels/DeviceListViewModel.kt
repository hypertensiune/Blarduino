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

    var devices: Set<BluetoothDevice>? = null
    val connectionState: MutableLiveData<Pair<Int, Boolean>> = MutableLiveData<Pair<Int, Boolean>>()

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
        val bluetoothManager = getSystemService(navController.context, BluetoothManager::class.java)
        val adapter: BluetoothAdapter? = bluetoothManager?.adapter

        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
        devices = pairedDevices!!

        bluetooth.addConnectionStateListener(this)
    }

    override fun onConnectionSuccessful(index: Int, previousConnection: Boolean) {
        connectionState.postValue(Pair(index, previousConnection))
    }

    override fun onConnectionFailed() {
        connectionState.postValue(Pair(-1, false))
    }

    fun bluetoothAlreadyConnected(): Int {
        return bluetooth.connectedDeviceIndex()
    }

    fun onDeviceItemClick(deviceAddress: String) {
        bluetooth.connect(deviceAddress)
    }

    fun onBackClick(view: View) {
        navController.popBackStack()
    }
}