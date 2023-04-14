package com.sb.android_streaming_app.ui.utils

import android.app.AlertDialog
import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.service.DIALService
import com.connectsdk.service.DeviceService
import com.connectsdk.service.DeviceService.PairingType
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.sb.android_streaming_app.ui.screens.RootViewModel
import java.net.HttpURLConnection
import java.net.URL

/**
 * Created by Zep S. on 14/03/2023.
 */
class DeviceListener(
    private val viewModel: RootViewModel,
) : ConnectableDeviceListener {

    private lateinit var dialService: DIALService
    private lateinit var runningAppSession: LaunchSession

    // Callback for when the device is ready for connection
    override fun onDeviceReady(device: ConnectableDevice) {
        // Connect the device
        connectDevice(device)
        Log.d("2ndScreenAPP", "successful connection")
    }

    // Callback for when the device is disconnected
    override fun onDeviceDisconnected(device: ConnectableDevice) {
        // Disconnect the device
        endConnection(device)
        Log.d("2ndScreenAPP", "Device is disconnected.")
    }

    // Callback for when pairing is required for the device
    override fun onPairingRequired(device: ConnectableDevice?, service: DeviceService?, pairingType: PairingType?) {
        // This callback is not being used in this implementation
    }

    // Callback for when device capabilities are updated
    override fun onCapabilityUpdated(
        device: ConnectableDevice?,
        added: MutableList<String>?,
        removed: MutableList<String>?
    ) {
        // This callback is not being used in this implementation
    }

    // Callback for when connection to the device fails
    override fun onConnectionFailed(device: ConnectableDevice?, error: ServiceCommandError?) {
        // End connection
        endConnection(device)
        Log.d("2ndScreenAPP", "Connection failed with error: $error")
    }

    private fun connectDevice(device: ConnectableDevice?) {
        device?.let { nonNullDevice ->
            dialService = nonNullDevice.getCapability(DIALService::class.java)
            viewModel.deviceConnected()
            viewModel.lauched.value = 1

            Log.d("2ndScreenAPP", dialService.serviceDescription.applicationURL)
            viewModel.launchApplication(dialService)
        }
    }

    private fun endConnection(device: ConnectableDevice?) {
        device?.apply {
            SocketHandler.closeConnection()
            disconnect()
            removeListener(this@DeviceListener)
        }
    }
}
