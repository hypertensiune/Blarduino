package com.example.blarduino

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import java.io.OutputStream
import java.util.UUID
import kotlin.Exception

class Bluetooth() {

    private val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var adapter: BluetoothAdapter

    private var connectThread: ConnectThread? = null

    private var pairedDevices: Set<BluetoothDevice>? = null


    class NotSupportedException : Exception()
    class NotEnabledException : Exception()
    class MissingPermissionException : Exception()

    private inner class ConnectThread(private val device: BluetoothDevice) : Thread() {

        @SuppressLint("MissingPermission")
        private val socket = device.createRfcommSocketToServiceRecord(mUUID)
        private lateinit var outputStream: OutputStream

        @SuppressLint("MissingPermission")
        override fun run() {
            socket?.let {
                socket.connect()
                outputStream = socket.outputStream
            }
        }

        fun write(message: String) {
            println("THREAD WRITE MESSAGE $message")
            outputStream.write(message.toByteArray())
        }
    }

    @Throws(NotSupportedException::class, NotEnabledException::class, MissingPermissionException::class)
    fun initialize(context: Context)  {

        bluetoothManager = context.getSystemService(BluetoothManager::class.java)
        adapter = bluetoothManager.adapter

        if(adapter == null) {
            // Bluetooth not supported
            throw NotSupportedException()
        }

        if(adapter?.isEnabled == false) {
            throw NotEnabledException()
        }

        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
            throw MissingPermissionException()
        }

        pairedDevices = adapter?.bondedDevices
    }

    fun connect(device: BluetoothDevice) {
        connectThread = ConnectThread(device)
        connectThread!!.start()
    }

    fun connect(device: Int) {
        connect(pairedDevices!!.elementAt(device))
    }

    fun connect(mac: String) {
        connect(adapter?.getRemoteDevice(mac)!!)
    }

    fun writeMessage(message: String) {
        if(connectThread != null) {
            connectThread!!.write(message)
        }
    }

}