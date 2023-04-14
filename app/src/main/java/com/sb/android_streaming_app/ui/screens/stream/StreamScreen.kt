package com.sb.android_streaming_app.ui.screens.stream

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Slider
import androidx.compose.material.SliderDefaults
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.ui.components.ProgressBar
import com.sb.android_streaming_app.ui.screens.detail.DetailViewModel
import com.sb.android_streaming_app.ui.utils.SocketHandler
import io.socket.client.Ack
/**
 * Created by Zep S. on 14/04/2023.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun StreamScreen(
    id: Int,
    device: String,
    detailViewModel: DetailViewModel = hiltViewModel(),
) {
    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        content = {
            Detail(
                viewModel = detailViewModel,
                detailContent = { detail ->
                    DetailContent(
                        detail = detail,
                        viewModel = detailViewModel,
                        device = device,
                    )
                },
                id = id
            )
        }
    )
}

@Composable
fun Detail(
    viewModel: DetailViewModel,
    detailContent: @Composable (detail: MovieItem) -> Unit,
    id: Int
) {
    viewModel.fetchMovie(id)
    when (val detailResponse = viewModel.moviesResponse) {
        is Response.Loading -> ProgressBar()
        is Response.Success -> detailContent(detailResponse.data)
        is Response.Failure -> print(detailResponse.e)
    }
}

@Composable
fun DetailContent(detail: MovieItem, viewModel: DetailViewModel, device: String) {
    var pause by remember { mutableStateOf(false) }
    var seekPosition by remember { mutableStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Column() {
            Text(text = detail.title, textAlign = TextAlign.Center, fontSize = 30.sp, modifier = Modifier.fillMaxWidth())
            Text(text = device, textAlign = TextAlign.Center, fontSize = 14.sp, modifier = Modifier.fillMaxWidth().padding(top = 4.dp))
        }

        AsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(detail.imageUrl)
                .crossfade(true)
                .build(),
            contentDescription = null,
            modifier = Modifier
                .background(MaterialTheme.colors.primary)
                .size(400.dp)
        )
        Row(modifier = Modifier.fillMaxWidth()) {
            Slider(
                value = seekPosition,
                onValueChange = { seekPosition = it },
                valueRange = 0f..100f,
                onValueChangeFinished = {
                    //TODO
                },
                steps = 0,
                colors = SliderDefaults.colors(
                    thumbColor = Color.White,
                    activeTrackColor = Color.White,
                    inactiveTickColor = Color.DarkGray
                )
            )
        }
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            IconButton(onClick = {
                SocketHandler.mSocket?.emit("rewind")
            }) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.FastRewind,
                    contentDescription = "rewind",
                    tint = Color.White,
                )
            }
            IconButton(onClick = {
                if (pause) {
                    SocketHandler.mSocket?.emit("play", object : Ack {
                        override fun call(vararg args: Any?) {
                            pause = false
                        }
                    })
                } else {
                    SocketHandler.mSocket?.emit("pause", object : Ack {
                        override fun call(vararg args: Any?) {
                            pause = true
                        }
                    })
                }
            }) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = if (pause) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = "pause / play",
                    tint = Color.White
                )
            }

            IconButton(onClick = {
                SocketHandler.mSocket?.emit("fast-forward")
            }) {
                Icon(
                    modifier = Modifier.size(60.dp),
                    imageVector = Icons.Filled.FastForward,
                    contentDescription = "fast-forward",
                    tint = Color.White
                )
            }
        }
    }
}
