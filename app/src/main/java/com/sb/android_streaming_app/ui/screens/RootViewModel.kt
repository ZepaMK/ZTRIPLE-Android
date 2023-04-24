package com.sb.android_streaming_app.ui.screens

import android.app.AlertDialog
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.DIALService
import com.sb.android_streaming_app.ui.utils.DeviceListener
import com.sb.android_streaming_app.ui.utils.SocketHandler
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class RootViewModel @Inject constructor(
) : ViewModel() {

    // Lateinit variables for ConnectableDevice, DeviceListener
    lateinit var device: ConnectableDevice
    lateinit var deviceListener: DeviceListener
    lateinit var dialog: AlertDialog

    // Mutable state variables to observe changes in view
    var connected = mutableStateOf(false)
    var dialogOpen = mutableStateOf(false)
    var lauched = mutableStateOf(0)

    // Function to set DeviceListener
    fun setDeviceListener() {
        deviceListener = DeviceListener(this)
    }

    // Function to indicate device is connected
    fun deviceConnected() {
        connected.value = true
    }

    // Function to indicate device is disconnected and update mutable state variables accordingly
    fun deviceDisconnected(launchFailed: Boolean) {
        if (launchFailed) lauched.value = 3 else lauched.value = 0
        deviceListener.onDeviceDisconnected(device)
        connected.value = false
        dialogOpen.value = false
    }

    // Function to open Connected Dialog
    fun openConnectedDialog() {
        dialogOpen.value = true
    }

    // Function to open Device Picker Dialog
    fun openDevicePicker() {
        dialog.show()
    }

    // Function to launch application using DIALService
    fun launchApplication(service: DIALService) {
//        val launcherSmartApp = LauncherHelper(this)
//        launcherSmartApp.launchSmartTvApplication(service)

        // Can be removed when not testing mode
        SocketHandler.setSocket("http://ztriple.martijnvb.nl/")
        SocketHandler.establishConnection()
        lauched.value = 2
    }
}
