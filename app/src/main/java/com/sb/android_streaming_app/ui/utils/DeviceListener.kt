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
        Log.d(TAG, "Device is ready for connection.")
        // Register the device as successfully connected
        registerSuccess(device)
    }

    // Callback for when the device is disconnected
    override fun onDeviceDisconnected(device: ConnectableDevice) {
        Log.d("2ndScreenAPP", "Device is disconnected.")
        connectionEnded(device)
    }

    // Callback for when pairing is required for the device
    override fun onPairingRequired(device: ConnectableDevice?, service: DeviceService?, pairingType: PairingType?) {
        Log.d(TAG, "Pairing is required for device: ${device?.ipAddress}")
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
        Log.d(TAG, "Connection failed with error: $error")
        // Notify the view model of the failed connection
        connectionFailed(mutableStateOf(device))
    }

    /**
     * Handles the case where a connection to a device is successful.
     * Initializes the launcher and DIAL service capabilities of the device.
     * Notifies the view model that the device is connected and opens a dialog.
     * Launches Netflix on the device and sets up a listener for the launch session.
     * Logs a message indicating the success.
     *
     * @param device The device that was successfully connected to.
     */
    private fun registerSuccess(device: ConnectableDevice?) {
        Log.d("2ndScreenAPP", "successful connection")
        device?.let { nonNullDevice ->
            dialService = nonNullDevice.getCapability(DIALService::class.java)
            viewModel.deviceConnected()
            viewModel.lauched.value = 1
            viewModel.launchApplication(dialService)
        }
    }

    /**
     * Handles the end of a connection attempt to a device.
     * Dismisses any pairing dialogs that are showing and removes the device listener if the device
     * is no longer connecting.
     *
     * @param device The device that was being connected to.
     */
    private fun connectionEnded(device: ConnectableDevice) {
        device.apply {
            disconnect()
            removeListener(this@DeviceListener)
        }
    }

    /**
     * Handles the case where a connection to a device fails.
     * If the device is not null, log a message indicating the failure.
     * If the view model's device is not null, remove the listener and disconnect the device.
     * Finally, set the view model's device to null.
     */
    private fun connectionFailed(device: MutableState<ConnectableDevice?>) {
        device.let {
            Log.d("2ndScreenAPP", "Failed to connect to ${it.value?.ipAddress}")
        }
        device.apply {
            value?.removeListener(this@DeviceListener)
            value?.disconnect()
            value = null
        }
    }

    fun setRunningAppInfo(session: LaunchSession) {
        runningAppSession = session
    }

    companion object {
        private const val TAG = "DeviceListener"
    }
}