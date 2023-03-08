package com.sb.android_streaming_app.app

import android.app.Application
import com.connectsdk.discovery.DiscoveryManager
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
class CoreApplication: Application() {
    override fun onCreate() {
        DiscoveryManager.init(applicationContext)

        super.onCreate()
    }
}
