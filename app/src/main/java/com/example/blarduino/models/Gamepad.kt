package com.example.blarduino.models

import android.content.Context
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

    data class Mapping(var press: Int, var release: Int)

    private var mappings: MutableMap<Button, Mapping> = EnumMap(Button::class.java)

    init {
        val preferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE)

        for(button in Button.entries) {
            val press = preferences.getInt("${button}_PRESS", -1)
            val release = preferences.getInt("${button}_RELEASE", -1)

            mappings[button] = Mapping(press, release)
        }
    }

    fun getButtonPressMapping(button: Button ) = mappings[button]?.press
    fun getButtonReleaseMapping(button: Button ) = mappings[button]?.release
}
