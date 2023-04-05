package com.sb.android_streaming_app.ui.screens

import android.app.AlertDialog
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.DIALService
import com.connectsdk.service.capability.Launcher.AppLaunchListener
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.sb.android_streaming_app.ui.utils.DeviceListener
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.net.HttpURLConnection
import java.net.URL
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
//        CoroutineScope(Dispatchers.IO).launch {
//            val client = OkHttpClient()
//
//            val request = Request.Builder()
//                .url(generateURL(service))
//                .post(ByteArray(0).toRequestBody(null, 0, 0))
//                .build()
//
//            Log.d("2ndScreenAPP", request.url.toString())
//
//            val response = client.newCall(request).execute()
//
//            if (response.code == 200) {
//                Log.d("2ndScreenAPP", "App launch successful")
//                lauched.value = 2
//            } else {
//                Log.d("2ndScreenAPP", "App launch unsuccessful")
//                deviceDisconnected(true)
//            }
//        }

        service.launcher.launchApp("ztriple", object : AppLaunchListener {
            override fun onError(error: ServiceCommandError?) {
                Log.d("2ndScreenAPP", "App launch unsuccessful: ${error?.message}")
                deviceDisconnected(true)
            }

            override fun onSuccess(`object`: LaunchSession?) {
                Log.d("2ndScreenAPP", "App launch successful")
                lauched.value = 2
            }

        })
    }

    // Function to generate URL based on DIALService manufacturer
    private fun generateURL(service: DIALService): String {
        val url: String = when (service.serviceDescription.manufacturer) {
            SAMSUNG -> String.format(SAMSUNG_URL_TEMPLATE, service.serviceDescription.ipAddress, SAMSUNG_APP_ID)
            else -> ""
        }
        return url
    }

    companion object {
        private const val SAMSUNG = "Samsung Electronics"
        private const val SAMSUNG_URL_TEMPLATE = "http://%s:8001/api/v2/applications/%s"
        private const val SAMSUNG_APP_ID = "azFWqejcXJ.NLZiet"
    }
}
