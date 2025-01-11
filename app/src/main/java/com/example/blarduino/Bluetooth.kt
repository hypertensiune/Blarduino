package com.example.blarduino

import android.Manifest
import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothManager
import android.bluetooth.BluetoothSocket
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Message
import android.util.Log
import androidx.core.app.ActivityCompat
import java.io.InputStream
import java.io.OutputStream
import java.util.UUID
import kotlin.Exception

class Bluetooth {

    class NotSupportedException : Exception()
    class NotEnabledException : Exception()
    class MissingPermissionException : Exception()

    data class Device(val name: String, val address: String, var connected: Boolean)

    public interface IncomingMessageListener {
        fun onIncomingMessage(message: String)
    }

    public interface ConnectionStateListener {
        fun onConnectionSuccessful(index: Int)
        fun onConnectionFailed()
    }

    private val TAG = "Blarduino"

    private val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var adapter: BluetoothAdapter
    private var pairedDevices: Set<BluetoothDevice>? = null

    private var handler: Handler? = null
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    private val incomingMessageListeners: MutableList<IncomingMessageListener> = mutableListOf()
    private val connectionStateListeners: MutableList<ConnectionStateListener> = mutableListOf()

    /**
     * Thread for socket connection
     * https://developer.android.com/develop/connectivity/bluetooth/connect-bluetooth-devices
     */
    private inner class ConnectThread(private val device: BluetoothDevice) : Thread() {

        @SuppressLint("MissingPermission")
        private val socket = device.createRfcommSocketToServiceRecord(mUUID)

        @SuppressLint("MissingPermission")
        override fun run() {
            //adapter.cancelDiscovery()
            socket?.let {
                try {
                    socket.connect()

                    connectedThread = ConnectedThread(socket)
                    connectedThread!!.start()

                    connectionStateListeners.forEach {
//                        it.onConnectionSuccessful(pairedDevices!!.indexOf(device), false)
                        //it.onConnectionFailed()
                    }


                } catch (e: Exception) {
                    println("AICI")
                    connectionStateListeners.forEach {
                        it.onConnectionSuccessful(pairedDevices!!.indexOf(device))
//                        it.onConnectionFailed()
                    }
                }
            }
        }

        fun cancel() {
            try {
                socket.close()
            } catch (e: Exception) {
                Log.e(TAG, "Could not close the socket", e)
            }
        }
    }

    /**
     * Thread for managing the socket after connection is successful
     * https://developer.android.com/develop/connectivity/bluetooth/transfer-data
     */
    private inner class ConnectedThread(private val socket: BluetoothSocket) : Thread() {

        private val inputStream = socket.inputStream
        private val outputStream = socket.outputStream
        private val buffer: ByteArray = ByteArray(1024)

        override fun run() {
            var numBytes: Int

            while(true) {
                numBytes = try {
                    inputStream.read(buffer)
                } catch (e: Exception) {
                    Log.d(TAG, "Input stream was disconnected", e)
                    break
                }
            }
        }

        fun getRemoteDevice(): BluetoothDevice? {
            return socket.remoteDevice
        }

        fun write(message: String) {

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

    @SuppressLint("MissingPermission")
    fun getPairedDevices(): Set<Device>? {
        return pairedDevices?.map {
            Device(it.name, it.address, getConnectedDevice()?.address == it.address)
        }?.toSet()
    }

    private fun getConnectedDevice(): BluetoothDevice? {
        return if(connectedThread != null) {
            connectedThread!!.getRemoteDevice()
        } else {
            null
        }
    }

    @SuppressLint("MissingPermission")
    fun getConnectedDeviceName(): String {
        val connectedDevice = getConnectedDevice()
        return if(connectedDevice != null) {
            connectedDevice.name
        } else {
            "No connected device"
        }
    }

    fun addIncomingMessageListener(listener: IncomingMessageListener) {
        incomingMessageListeners.add(listener)
    }

    fun addConnectionStateListener(listener: ConnectionStateListener) {
        connectionStateListeners.add(listener)
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
        if(connectedThread != null) {
            connectedThread!!.write(message)
        }
    }
}