package com.example.blarduino

import android.annotation.SuppressLint
import android.bluetooth.BluetoothDevice
import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.NavHostFragment
import com.example.blarduino.models.Gamepad
import kotlinx.coroutines.launch
import java.util.UUID


class MainActivity : AppCompatActivity() {

    private val ID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    private val TAG = "BluetoothController"

    private val MAC = "00:21:06:08:1C:A3"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val decorView = window.decorView
            var uiVisibility = decorView.systemUiVisibility

            // Check for the current theme and adjust the status bar icons accordingly
            uiVisibility = if (isDarkTheme()) {
                // Dark theme: Use dark icons
                uiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            } else {
                // Light theme: Use light icons
                uiVisibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }
            decorView.systemUiVisibility = uiVisibility
        }

        setContentView(R.layout.activity_main)

        lifecycleScope.launch {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            val inflater = navHostFragment.navController.navInflater
            val graph = inflater.inflate(R.navigation.navigation)

            graph.setStartDestination(R.id.home)
            navHostFragment.navController.setGraph(graph, bundleOf())
        }

//        val bluetoothManager = getSystemService(BluetoothManager::class.java)
//        val adapter: BluetoothAdapter? = bluetoothManager.adapter
//
//        if(adapter == null) {
//            // BT not supported
//        }
//
//        if(adapter?.isEnabled == false) {
//            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
//            startActivityForResult(enableBluetoothIntent, 1001)
//        }
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.BLUETOOTH_CONNECT) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.BLUETOOTH_CONNECT, Manifest.permission.BLUETOOTH_SCAN), 1)
//            Log.d(TAG, "No blueotooth permissions")
//            return
//        }
//
//
//        val pairedDevices: Set<BluetoothDevice>? = adapter?.bondedDevices
//        pairedDevices?.forEach {
//            Log.d(TAG, "${it.name} ${it.address}")
//        }
//
//        val device = adapter?.getRemoteDevice(MAC)
//
//        val thread = ConnectThread(device!!)
//        thread.start()
    }

    private fun isDarkTheme(): Boolean {
        val nightModeFlags = resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK
        return nightModeFlags == Configuration.UI_MODE_NIGHT_YES
    }

    private inner class ConnectThread(device: BluetoothDevice) : Thread() {

        @SuppressLint("MissingPermission")
        private val socket = device.createRfcommSocketToServiceRecord(ID)

        @SuppressLint("MissingPermission")
        public override fun run() {
            socket?.let {

                socket.connect()
                Log.d(TAG, "Socket connected: $socket.isConnected")

                val outputStream = socket.outputStream

                var b = 65
                while(true) {
                    outputStream.write(b)
                    b++
                    sleep(1000)
                }
            }
        }

    }
}