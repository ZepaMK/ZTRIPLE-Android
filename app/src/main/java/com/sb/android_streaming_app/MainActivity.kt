package com.sb.android_streaming_app

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.connectsdk.core.AppInfo
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.device.DevicePicker
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.discovery.DiscoveryManager.PairingLevel
import com.connectsdk.discovery.provider.SSDPDiscoveryProvider
import com.connectsdk.service.DIALService
import com.connectsdk.service.DeviceService
import com.connectsdk.service.DeviceService.PairingType
import com.connectsdk.service.capability.Launcher
import com.connectsdk.service.capability.Launcher.AppLaunchListener
import com.connectsdk.service.command.ServiceCommandError
import com.connectsdk.service.sessions.LaunchSession
import com.sb.android_streaming_app.services.DService
import com.sb.android_streaming_app.ui.app.App
import com.sb.android_streaming_app.ui.screens.RootViewModel
import dagger.hilt.android.AndroidEntryPoint
import java.util.Locale

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mDiscoveryManager: DiscoveryManager? = null
    private var dialog: AlertDialog? = null
    private var pairingAlertDialog: AlertDialog? = null
    private var pairingCodeDialog: AlertDialog? = null
    private var dp: DevicePicker? = null
    private var viewModel: RootViewModel? = null
    private var launcher: Launcher? = null
    private var dialService: DIALService? = null
    private var runningAppSession: LaunchSession? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            App()
        }
        supportActionBar?.hide()
        setupPicker()

        viewModel = ViewModelProvider(this)[RootViewModel::class.java]

        mDiscoveryManager = DiscoveryManager.getInstance()
        mDiscoveryManager!!.registerDeviceService(DIALService::class.java, SSDPDiscoveryProvider::class.java)
        mDiscoveryManager!!.pairingLevel = PairingLevel.ON
        mDiscoveryManager!!.start()
    }

    val deviceListener: ConnectableDeviceListener = object : ConnectableDeviceListener {

        override fun onDeviceReady(device: ConnectableDevice?) {
            Log.d("2ndScreenAPP", "onPairingSuccess")
            if (pairingAlertDialog!!.isShowing) {
                pairingAlertDialog!!.dismiss()
            }
            if (pairingCodeDialog!!.isShowing) {
                pairingCodeDialog!!.dismiss()
            }
            registerSuccess(viewModel!!.mDevice)
        }

        override fun onDeviceDisconnected(device: ConnectableDevice?) {
            Log.d("2ndScreenAPP", "Device Disconnected")
            viewModel!!.deviceDisconnected()
            connectEnded(viewModel!!.mDevice)
            Toast.makeText(applicationContext, "Device Disconnected", Toast.LENGTH_SHORT).show()
        }

        override fun onPairingRequired(device: ConnectableDevice?, service: DeviceService?, pairingType: PairingType?) {
            Log.d("2ndScreenAPP", "Connected to " + viewModel!!.mDevice!!.ipAddress)

            when (pairingType) {
                PairingType.FIRST_SCREEN -> {
                    Log.d("2ndScreenAPP", "First Screen")
                    pairingAlertDialog!!.show()
                }
                PairingType.PIN_CODE, PairingType.MIXED -> {
                    Log.d("2ndScreenAPP", "Pin Code")
                    pairingCodeDialog!!.show()
                }
                PairingType.NONE -> {}
                else -> {}
            }
        }

        override fun onCapabilityUpdated(
            device: ConnectableDevice?,
            added: MutableList<String>?,
            removed: MutableList<String>?
        ) {
        }

        override fun onConnectionFailed(device: ConnectableDevice?, error: ServiceCommandError?) {
            Log.d("2ndScreenAPP", "onConnectFailed")
            connectFailed(viewModel!!.mDevice)
        }
    }


    private fun registerSuccess(device: ConnectableDevice?) {
        launcher = device!!.getCapability(Launcher::class.java)
        dialService = device.getCapability(DIALService::class.java)

        viewModel!!.deviceConnected()
        viewModel!!.openDialog()

        launcher!!.launchNetflix("70217913", object : AppLaunchListener {
            override fun onSuccess(session: LaunchSession) {
                setRunningAppInfo(session)
            }

            override fun onError(error: ServiceCommandError) {}
        })

        Log.d("2ndScreenAPP", "successful register")
    }

    private fun connectFailed(device: ConnectableDevice?) {
        if (device != null) Log.d("2ndScreenAPP", "Failed to connect to " + device.ipAddress)
        if (viewModel!!.mDevice != null) {
            viewModel!!.mDevice!!.removeListener(deviceListener)
            viewModel!!.mDevice!!.disconnect()
            viewModel!!.mDevice = null
        }
    }

    fun connectEnded(device: ConnectableDevice?) {
        if (pairingAlertDialog!!.isShowing) {
            pairingAlertDialog!!.dismiss()
        }
        if (pairingCodeDialog!!.isShowing) {
            pairingCodeDialog!!.dismiss()
        }
        if (!viewModel!!.mDevice!!.isConnecting) {
            viewModel!!.mDevice!!.removeListener(deviceListener)
            viewModel!!.mDevice = null
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null) {
            dialog!!.dismiss()
        }
        if (viewModel!!.mDevice != null) {
            viewModel!!.mDevice!!.disconnect()
        }
    }

    fun toggleDevicePicker() {
        dialog?.show()
    }

    private fun setupPicker() {
        dp = DevicePicker(this)
        dialog = dp!!.getPickerDialog("Devices") { arg0, _, arg2, _ ->
            viewModel!!.mDevice = arg0.getItemAtPosition(arg2) as ConnectableDevice
            viewModel!!.mDevice!!.addListener(deviceListener)
            viewModel!!.mDevice!!.connect()
            dp!!.pickDevice(viewModel!!.mDevice)
        }
        pairingAlertDialog = AlertDialog.Builder(this)
            .setTitle("Pairing with TV")
            .setMessage("Please confirm the connection on your TV")
            .setPositiveButton("Okay", null)
            .setNegativeButton("Cancel") { _, _ ->
                dp!!.cancelPicker()
                toggleDevicePicker()
            }
            .create()

        val input = EditText(this)
        input.inputType = InputType.TYPE_CLASS_TEXT
        val imm: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        pairingCodeDialog = AlertDialog.Builder(this)
            .setTitle("Enter Pairing Code on TV")
            .setView(input)
            .setPositiveButton(R.string.ok) { _, _ ->
                if (viewModel!!.mDevice != null) {
                    val value = input.text.toString().trim { it <= ' ' }
                    viewModel!!.mDevice!!.sendPairingKey(value)
                    imm.hideSoftInputFromWindow(input.windowToken, 0)
                }
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                dp!!.cancelPicker()
                toggleDevicePicker()
                imm.hideSoftInputFromWindow(input.windowToken, 0)
            }
            .create()
    }

    fun setRunningAppInfo(session: LaunchSession) {
        runningAppSession = session
    }
}
