package com.sb.android_streaming_app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.connectsdk.device.DevicePicker
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.connectsdk.discovery.provider.SSDPDiscoveryProvider
import com.connectsdk.service.DIALService
import com.sb.android_streaming_app.ui.app.App
import com.sb.android_streaming_app.ui.screens.RootViewModel
import com.sb.android_streaming_app.ui.utils.DevicePickerHelper
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: RootViewModel
    private lateinit var discoveryManager: DiscoveryManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Set the content view using Jetpack Compose
        setContent {
            App()
        }

        // Hide the support action bar
        supportActionBar?.hide()

        // Initialize the root view model using the ViewModelProvider
        viewModel = ViewModelProvider(this)[RootViewModel::class.java]

        // Initialize the discovery manager to find devices on the same Wi-Fi network
        discoveryManager = DiscoveryManager.getInstance().apply {
            // Register the DIALService class with the SSDPDiscoveryProvider class for device discovery
            registerDeviceService(DIALService::class.java, SSDPDiscoveryProvider::class.java)

            // Set the pairing level to ON for device pairing
            pairingLevel = PairingLevel.ON

            // Start device discovery
            start()
        }

        // Initialize device Picker
        val dp = DevicePicker(this)

        // Setup the device picker
        val devicePickerHelper = DevicePickerHelper(viewModel)
        devicePickerHelper.setupPicker(dp)

        // Create a DeviceListener with the necessary parameters.
        viewModel.setDeviceListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dialog.dismiss()
        viewModel.device.disconnect()
    }
}
