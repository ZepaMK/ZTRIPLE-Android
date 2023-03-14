package com.sb.android_streaming_app.data.repository

import com.sb.android_streaming_app.data.models.MovieItem
import com.sb.android_streaming_app.data.models.Response
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by Zep S. on 03/03/2023.
 */
@Singleton
class MovieRepositoryImpl @Inject constructor() : MovieRepository {

    override suspend fun getMovies(): Flow<Response<List<MovieItem>>> = callbackFlow {
        try {
            val response = Response.Success(MOVIES)
            trySend(response)
        } catch (e: Exception) {
            Response.Failure(e)
        }
        awaitClose {}
    }

    override suspend fun getMovie(id: Int): Response<MovieItem> {
        return try {
            val movie = MOVIES.single { it.id == id }
            Response.Success(movie)
        } catch (e: Exception) {
            Response.Failure(e)
        }
    }

    //Dummy data
    companion object {
        val MOVIES = listOf(
            MovieItem(
                1,
                "Big Buck Bunny",
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/c5/Big_buck_bunny_poster_big.jpg/1280px-Big_buck_bunny_poster_big.jpg",
                "https://storage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4"
            ),
            MovieItem(
                2,
                "Abraham Lincoln",
                "https://upload.wikimedia.org/wikipedia/en/8/80/AbrahmaLincoln1930.jpg",
                "https://upload.wikimedia.org/wikipedia/commons/2/2c/Abraham_Lincoln_%281930%29.webm"
            )
        )
    }
}
