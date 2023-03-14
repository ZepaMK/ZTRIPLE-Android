package com.sb.android_streaming_app.data.use_cases

import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.data.repository.MovieRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

/**
 * Created by Zep S. on 03/03/2023.
 */
class GetMovies @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(): Flow<Response<List<MovieItem>>> {
        return movieRepository.getMovies()
    }
}
