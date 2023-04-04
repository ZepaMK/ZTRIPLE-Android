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
class DevicePickerHelper(private val viewModel: RootViewModel) {

    // This function sets up the picker dialog and is called when a device picker is created.
    // It takes a DevicePicker instance as an argument.
    fun setupPicker(dp: DevicePicker) {

        // This line creates a dialog using the given picker instance and sets it to the dialog variable of the viewModel.
        viewModel.dialog = dp.getPickerDialog("devices") { adapter, _, position, _ ->

            // When a device is selected, the launched value of the viewModel is set to 0.
            viewModel.lauched.value = 0

            // The selected device object from the adapter is set to the device variable of the viewModel.
            viewModel.device = adapter.getItemAtPosition(position) as ConnectableDevice

            // The DeviceListener variable of the viewModel is added as a listener to the selected device.
            viewModel.device.addListener(viewModel.deviceListener)

            // The selected device is connected.
            viewModel.device.connect()

            // Finally, the selected device is passed to the picker instance using the pickDevice() method.
            dp.pickDevice(viewModel.device)
        }
    }
}
