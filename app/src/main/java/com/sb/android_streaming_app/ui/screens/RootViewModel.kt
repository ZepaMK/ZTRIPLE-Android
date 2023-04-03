package com.sb.android_streaming_app.ui.screens

import android.app.AlertDialog
import android.util.Log
import android.widget.Toast
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import coil.network.HttpException
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.DIALService
import com.connectsdk.service.capability.Launcher.AppLaunchListener
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.sb.android_streaming_app.ui.utils.DeviceListener
import dagger.hilt.android.lifecycle.HiltViewModel
import khttp.post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.net.HttpURLConnection
import java.net.URL
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
    var lauched = mutableStateOf(0)


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
        lauched.value = 4
    }

    fun openConnectedDialog() {
        dialogOpen.value = true
    }

    fun openDevicePicker() {
        dialog.show()
    }

    fun launchApplication(service: DIALService) {
        CoroutineScope(Dispatchers.IO).launch {
            val client = OkHttpClient()

            val request = Request.Builder()
                .url(generateURL(service))
                .post(ByteArray(0).toRequestBody(null, 0, 0))
                .build()

            val response = client.newCall(request).execute()

            if (response.code == 200) {
                Log.d("2ndScreenAPP", "App launch successful")
                lauched.value = 1
            } else {
                Log.d("2ndScreenAPP", "App launch  unsuccessful")
                lauched.value = 2
                deviceDisconnected()
            }
        }
    }

    private fun generateURL(service: DIALService): String {
        val url: String = when (service.serviceDescription.manufacturer) {
            SAMSUNG -> String.format(SAMSUNG_URL_TEMPLATE, service.serviceDescription.ipAddress, ID)
            LG -> String.format("LG")
            else -> ""
        }
        return url
    }

    companion object {
        private const val SAMSUNG = "Samsung Electronics"
        private const val SAMSUNG_URL_TEMPLATE = "http://%s:8001/api/v2/applications/%s"
        private const val LG = "LG Electronics"
        private const val ID = "azFWqejcXJ.NLZie"
    }
}

