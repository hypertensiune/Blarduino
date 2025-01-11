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

    companion object {
        const val CONNECTION_SUCCESSFUL = 0
        const val CONNECTION_FAILED = 1
        const val CONNECTED = 2
    }

    class NotSupportedException : Exception()
    class NotEnabledException : Exception()
    class MissingPermissionException : Exception()

    public interface IncomingMessageListener {
        fun onIncomingMessage(message: String)
    }

    private val TAG = "Blarduino"

    private val mUUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

    private lateinit var bluetoothManager: BluetoothManager
    private lateinit var adapter: BluetoothAdapter
    private var pairedDevices: Set<BluetoothDevice>? = null

    private var handler: Handler? = null
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    private val listeners: MutableList<IncomingMessageListener> = mutableListOf()

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
                val msg = Message.obtain()
                msg.arg1 = pairedDevices!!.indexOf(device)

                try {
                    socket.connect()

                    connectedThread = ConnectedThread(socket)
                    connectedThread!!.start()

                    msg.what = CONNECTION_SUCCESSFUL

                } catch (e: Exception) {
                    msg.what = CONNECTION_FAILED
                }

                handler?.sendMessage(msg)
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

    fun addIncomingMessageListener(listener: IncomingMessageListener) {
        listeners.add(listener)
    }

    fun removeIncomingMessageListener(listener: IncomingMessageListener) {
        listeners.remove(listener)
    }

    fun setHandler(handler: Handler) {
        this.handler = handler
    }

    fun connectedDeviceIndex(): Int {
        val device = connectedThread?.getRemoteDevice()
        return pairedDevices?.indexOf(device) ?: -1
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