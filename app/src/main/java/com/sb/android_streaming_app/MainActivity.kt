package com.sb.android_streaming_app

import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.MenuItem
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.ConnectableDeviceListener
import com.connectsdk.device.DevicePicker
import com.connectsdk.discovery.DiscoveryManager
import com.connectsdk.service.DeviceService
import com.connectsdk.service.DeviceService.PairingType
import com.connectsdk.service.command.ServiceCommandError
import com.sb.android_streaming_app.ui.app.App
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mDiscoveryManager: DiscoveryManager? = null
    private var mDevice: ConnectableDevice? = null
    private var connectItem: MenuItem? = null
    private var dialog: AlertDialog? = null
    private var pairingAlertDialog: AlertDialog? = null
    private var pairingCodeDialog: AlertDialog? = null
    private var dp: DevicePicker? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DiscoveryManager.init(applicationContext)
        setContent {
            App()
        }
        supportActionBar?.hide()
        setupPicker()

        mDiscoveryManager = DiscoveryManager.getInstance()
        mDiscoveryManager!!.registerDefaultDeviceTypes();
        mDiscoveryManager!!.pairingLevel = DiscoveryManager.PairingLevel.ON;

        DiscoveryManager.getInstance().start()
    }

    private val deviceListener: ConnectableDeviceListener = object : ConnectableDeviceListener {

        override fun onDeviceReady(device: ConnectableDevice?) {
            Log.d("2ndScreenAPP", "onPairingSuccess")
            if (pairingAlertDialog!!.isShowing) {
                pairingAlertDialog!!.dismiss()
            }
            if (pairingCodeDialog!!.isShowing) {
                pairingCodeDialog!!.dismiss()
            }
            registerSuccess(mDevice)
        }

        override fun onDeviceDisconnected(device: ConnectableDevice?) {
            Log.d("2ndScreenAPP", "Device Disconnected")
            connectEnded(mDevice)
            Toast.makeText(applicationContext, "Device Disconnected", Toast.LENGTH_SHORT).show()
        }

        override fun onPairingRequired(device: ConnectableDevice?, service: DeviceService?, pairingType: PairingType?) {
            Log.d("2ndScreenAPP", "Connected to " + mDevice!!.ipAddress)

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
            connectFailed(mDevice)
        }
    }


    private fun registerSuccess(device: ConnectableDevice?) {
        Log.d("2ndScreenAPP", "successful register")
    }

    private fun connectFailed(device: ConnectableDevice?) {
        if (device != null) Log.d("2ndScreenAPP", "Failed to connect to " + device.ipAddress)
        if (mDevice != null) {
            mDevice!!.removeListener(deviceListener)
            mDevice!!.disconnect()
            mDevice = null
        }
    }

    fun connectEnded(device: ConnectableDevice?) {
        if (pairingAlertDialog!!.isShowing) {
            pairingAlertDialog!!.dismiss()
        }
        if (pairingCodeDialog!!.isShowing) {
            pairingCodeDialog!!.dismiss()
        }
        if (!mDevice!!.isConnecting) {
            mDevice!!.removeListener(deviceListener)
            mDevice = null
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (dialog != null) {
            dialog!!.dismiss()
        }
        if (mDevice != null) {
            mDevice!!.disconnect()
        }
    }

    fun toggleDevicePicker() {
        dialog?.show()
    }

    private fun setupPicker() {
        dp = DevicePicker(this)
        dialog = dp!!.getPickerDialog("Devices") { arg0, _, arg2, _ ->
            mDevice = arg0.getItemAtPosition(arg2) as ConnectableDevice
            mDevice!!.addListener(deviceListener)
            mDevice!!.setPairingType(null)
            mDevice!!.connect()
            connectItem!!.title = mDevice!!.friendlyName
            dp!!.pickDevice(mDevice)
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
                if (mDevice != null) {
                    val value = input.text.toString().trim { it <= ' ' }
                    mDevice!!.sendPairingKey(value)
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
}
