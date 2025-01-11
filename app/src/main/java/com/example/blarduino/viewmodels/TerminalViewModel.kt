package com.example.blarduino.viewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.navigation.NavController
import com.example.blarduino.App
import com.example.blarduino.Bluetooth
import com.example.blarduino.models.TerminalMessage

class TerminalViewModel(
    private val navController: NavController,
    private val bluetooth: Bluetooth
) : ViewModel(), Bluetooth.IncomingMessageListener {

    val messages: MutableLiveData<MutableList<TerminalMessage>> = MutableLiveData(mutableListOf())

    class Factory(private val navController: NavController) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(
            modelClass: Class<T>,
            extras: CreationExtras
        ): T {
            val app = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as App

            return TerminalViewModel(navController, app.bluetooth) as T
        }
    }

    val inputText: MutableLiveData<String> = MutableLiveData("")

    fun sendMessage() {
        inputText.value?.let {
            bluetooth.writeMessage(it)

            val v = messages.value
            v?.add(TerminalMessage(it, true))
            messages.value = v
        }
    }

    override fun onIncomingMessage(message: String) {
        val v = messages.value
        v?.add(TerminalMessage(message, false))
        messages.value = v
    }
}