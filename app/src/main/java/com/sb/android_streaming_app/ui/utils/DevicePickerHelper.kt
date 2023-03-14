package com.sb.android_streaming_app.ui.utils

import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.device.DevicePicker
import com.sb.android_streaming_app.R
import com.sb.android_streaming_app.ui.screens.RootViewModel

/**
 * Created by Zep S. on 14/03/2023.
 */
class DevicePickerHelper(
    private val context: Context,
    private val viewModel: RootViewModel,
) {
    private val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

    fun setupPicker(dp: DevicePicker) {
        viewModel.dialog = dp.getPickerDialog("devices") { adapter, _, position, _ ->
            viewModel.device = adapter.getItemAtPosition(position) as ConnectableDevice
            viewModel.device.addListener(viewModel.deviceListener)
            viewModel.device.connect()
            dp.pickDevice(viewModel.device)
        }

        viewModel.pairingAlertDialog = AlertDialog.Builder(context)
            .setTitle("Pairing with TV")
            .setMessage("Please confirm the connection on your TV")
            .setPositiveButton("Okay", null)
            .setNegativeButton("Cancel") { _, _ ->
                dp.cancelPicker()
                viewModel.openDevicePicker()
            }
            .create()

        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
        }
        viewModel.pairingCodeDialog = AlertDialog.Builder(context)
            .setTitle("Enter Pairing Code on TV")
            .setView(input)
            .setPositiveButton(R.string.ok) { _, _ ->
                val value = input.text.toString().trim()
                viewModel.device.sendPairingKey(value)
                imm.hideSoftInputFromWindow(input.windowToken, 0)
            }
            .setNegativeButton(R.string.cancel) { _, _ ->
                dp.cancelPicker()
                viewModel.openDevicePicker()
                imm.hideSoftInputFromWindow(input.windowToken, 0)
            }
            .create()
    }
}
