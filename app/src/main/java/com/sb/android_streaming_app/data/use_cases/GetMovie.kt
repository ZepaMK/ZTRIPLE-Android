package com.sb.android_streaming_app.data.use_cases

import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import com.sb.android_streaming_app.data.repository.MovieRepository
import javax.inject.Inject

/**
 * Created by Zep S. on 03/03/2023.
 */
class GetMovie @Inject constructor(
    private val movieRepository: MovieRepository
) {

    suspend operator fun invoke(id: Int): Response<MovieItem> {
        return movieRepository.getMovie(id)
    }
}
