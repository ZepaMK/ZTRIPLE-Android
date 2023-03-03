package com.sb.android_streaming_app.data.repository

import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MovieRepositoryImpl @Inject constructor() : MovieRepository {

    override suspend fun getMovies(): Flow<Response<List<MovieItem>>> = callbackFlow {
        try {
            val movies = listOf(MovieItem(1, "Big Buck Bunny", ""))
            val response = Response.Success(movies)
            trySend(response)
        } catch (e: Exception) {
            Response.Failure(e)
        }
        awaitClose {}
    }

    override suspend fun getMovie(id: Int): Response<MovieItem> {
        return try {
            val movie = MovieItem(1, "Big Buck Bunny", "")
            Response.Success(movie)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }
}