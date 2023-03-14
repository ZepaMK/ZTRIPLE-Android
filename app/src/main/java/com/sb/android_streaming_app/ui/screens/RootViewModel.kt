package com.sb.android_streaming_app.ui.screens

import android.app.AlertDialog
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connectsdk.device.ConnectableDevice
import com.sb.android_streaming_app.ui.utils.DeviceListener
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(

) : ViewModel() {

    lateinit var device: ConnectableDevice
    lateinit var deviceListener: DeviceListener
    lateinit var dialog: AlertDialog
    lateinit var pairingAlertDialog: AlertDialog
    lateinit var pairingCodeDialog: AlertDialog

    var connected = mutableStateOf(false)
    var dialogOpen = mutableStateOf(false)

    fun setDeviceListener() {
        deviceListener = DeviceListener(
            viewModel = this,
            pairingAlertDialog = pairingAlertDialog,
            pairingCodeDialog = pairingCodeDialog,
        )
    }

    fun deviceConnected() {
        connected.value = true
    }

    fun deviceDisconnected() {
        deviceListener.onDeviceDisconnected(device)
        connected.value = false
    }

    fun openConnectedDialog() {
        dialogOpen.value = true
    }

    fun openDevicePicker() {
        dialog.show()
    }
}
