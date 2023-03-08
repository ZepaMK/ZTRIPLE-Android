package com.sb.android_streaming_app.ui.screens

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connectsdk.device.ConnectableDevice
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
) : ViewModel() {

    var mDevice: ConnectableDevice? = null
    var connected = mutableStateOf(false)
    var dialogOpen = mutableStateOf(false)

    fun deviceConnected() {
        connected.value = true
    }

    fun deviceDisconnected() {
        connected.value = false
    }

    fun openDialog() {
        dialogOpen.value = true
    }
}
