package com.sb.android_streaming_app.data.repository

import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import kotlinx.coroutines.flow.Flow

/**
 * Created by Zep S. on 03/03/2023.
 */
interface MovieRepository {

    suspend fun getMovies(): Flow<Response<List<MovieItem>>>

    suspend fun getMovie(id: Int): Response<MovieItem>
}
