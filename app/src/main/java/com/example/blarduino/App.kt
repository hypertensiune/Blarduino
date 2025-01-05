package com.example.blarduino

import android.app.Application

class App : Application() {

    lateinit var bluetooth: Bluetooth
        private set

    init {
        bluetooth = Bluetooth()
    }
}