package com.sb.android_streaming_app.ui.utils

import android.util.Log
import com.connectsdk.service.DIALService
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.sb.android_streaming_app.ui.screens.RootViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody

/**
 * Created by Zep S. on 24/04/2023.
 */
class LauncherHelper(private val viewModel: RootViewModel) {

    fun launchSmartTvApplication(service: DIALService) {
        when (service.serviceDescription.manufacturer) {
            LG -> {
                // Using DIAL via the Connect SDK library
                service.launchApp("Netflix", object : Launcher.AppLaunchListener {
                    override fun onError(error: ServiceCommandError?) {
                        Log.d("2ndScreenAPP", "App launch unsuccessful: ${error?.message}")
                        viewModel.deviceDisconnected(true)
                    }

                    override fun onSuccess(`object`: LaunchSession?) {
                        Log.d("2ndScreenAPP", "App launch successful")
                        SocketHandler.setSocket("http://ztriple.martijnvb.nl")
                        SocketHandler.establishConnection()
                        viewModel.lauched.value = 2

                    }
                })
            }
            SAMSUNG -> {
                // Using HTTP POST since DIAL can't be used without Samsung's access and review
                CoroutineScope(Dispatchers.IO).launch {
                    val client = OkHttpClient()
                    val request = Request.Builder()
                        .url(
                            String.format(
                                SAMSUNG_URL_TEMPLATE, service.serviceDescription.ipAddress,
                                SAMSUNG_APP_ID
                            )
                        )
                        .post(ByteArray(0).toRequestBody(null, 0, 0))
                        .build()

                    Log.d("2ndScreenAPP", request.url.toString())
                    val response = client.newCall(request).execute()

                    if (response.isSuccessful) {
                        Log.d("2ndScreenAPP", "App launch successful")
                        SocketHandler.setSocket("http://ztriple.martijnvb.nl")
                        SocketHandler.establishConnection()
                        viewModel.lauched.value = 2
                    } else {
                        Log.d("2ndScreenAPP", "App launch unsuccessful")
                        viewModel.deviceDisconnected(true)
                    }
                }
            }
        }
    }

    companion object {
        private const val APP_ID = "ztriple"
        private const val SAMSUNG = "Samsung Electronics"
        private const val LG = "LG Electronics"

        // For Samsung the DIAL protocol can only be used by allowed applications.
        // Therefore a application should be submitted and reviewed.
        // Since this a POC its not done via DIAL but via direct HTTP POST
        private const val SAMSUNG_URL_TEMPLATE = "http://%s:8001/api/v2/applications/%s"
        private const val SAMSUNG_APP_ID = "azFWqejcXJ.NLZiet"
    }
}
