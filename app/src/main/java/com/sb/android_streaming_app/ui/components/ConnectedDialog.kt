package com.sb.android_streaming_app.ui.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MusicNote
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import com.connectsdk.device.ConnectableDevice
import com.connectsdk.service.capability.VolumeControl

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ConnectedDialog(
    connected: MutableState<Boolean>,
    dialogOpen: MutableState<Boolean>,
    device: ConnectableDevice,
    onDisconnect: () -> Unit,
) {
    val volumeControl = device.getCapability(VolumeControl::class.java)
    var sliderPosition by remember { mutableStateOf(0f) }

    if (dialogOpen.value && connected.value) {
        AlertDialog(
            onDismissRequest = {
                dialogOpen.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        onDisconnect()
                    }
                ) {
                    Text(text = "Disconnect".uppercase(), color = Color.Blue)
                }
            },
            dismissButton = {
                TextButton(
                    onClick = {
                        dialogOpen.value = false
                    }
                ) {
                    Text(text = "Close".uppercase(), color = Color.Blue)
                }
            },
            title = {
                Text(text = device.friendlyName, fontSize = 18.sp)
            },
            text = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.MusicNote,
                        contentDescription = "VolumeDown",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Slider(
                        value = sliderPosition,
                        onValueChange = { sliderPosition = it },
                        valueRange = 0f..100f,
                        enabled = device.hasCapabilities(VolumeControl.Volume_Set),
                        onValueChangeFinished = {
                            volumeControl.setVolume(sliderPosition / 100, null)
                        },
                        steps = 0,
                        colors = SliderDefaults.colors(

                            thumbColor = Color.White,
                            activeTrackColor = Color.White,
                            inactiveTickColor = Color.DarkGray
                        )
                    )
                }
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = false
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = MaterialTheme.shapes.large,
            backgroundColor = MaterialTheme.colors.primary,
        )
    }
}
