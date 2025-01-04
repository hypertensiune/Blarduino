package com.example.blarduino.models

import android.content.Context
import androidx.preference.PreferenceManager
import java.util.EnumMap

class Gamepad(context: Context) {

    enum class Button {
        DPAD_UP,
        DPAD_LEFT,
        DPAD_RIGHT,
        DPAD_DOWN,
        CIRCLE,
        TRIANGLE,
        SQUARE,
        X
    }

    data class Mapping(var press: String, var release: String)

    private var mappings: MutableMap<Button, Mapping> = EnumMap(Button::class.java)

    init {
//        val preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)
        val preferences = PreferenceManager.getDefaultSharedPreferences(context)

        for(button in Button.entries) {
            val press = preferences.getString("${button}_PRESS", "")
            val release = preferences.getString("${button}_RELEASE", "")

            println("$button $press $release")
            mappings[button] = Mapping(press!!, release!!)
        }
    }

    fun getButtonPressMapping(button: Button ) = mappings[button]?.press
    fun getButtonReleaseMapping(button: Button ) = mappings[button]?.release
}
