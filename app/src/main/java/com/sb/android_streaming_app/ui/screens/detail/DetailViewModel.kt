package com.sb.android_streaming_app.ui.screens.detail

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.data.use_cases.GetMovie
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val getMovie: GetMovie,
    val player: Player,
): ViewModel() {
    var moviesResponse by mutableStateOf<Response<MovieItem>>(Response.Loading)
        private set

    fun fetchMovie(id: Int) = viewModelScope.launch {
        moviesResponse = getMovie(id)
    }

    init {
        player.prepare()
    }

    fun playVideo(uri: String) {
        player.setMediaItem(MediaItem.fromUri(uri))
        player.play()
    }

    override fun onCleared() {
        super.onCleared()
        player.release()
    }
}
