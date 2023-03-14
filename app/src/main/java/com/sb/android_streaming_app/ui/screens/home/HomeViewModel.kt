package com.sb.android_streaming_app.ui.screens.home

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.data.use_cases.GetMovies
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by Zep S. on 03/03/2023.
 */
@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getMovies: GetMovies,
): ViewModel() {
    var moviesResponse by mutableStateOf<Response<List<MovieItem>>>(Response.Loading)
        private set

    fun fetchMovies() = viewModelScope.launch {
        getMovies().collect { response ->
            moviesResponse = response
        }
    }
}
