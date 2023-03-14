package com.sb.android_streaming_app.data

import com.sb.android_streaming_app.data.repository.MovieRepository
import com.sb.android_streaming_app.data.repository.MovieRepositoryImpl
import com.sb.android_streaming_app.data.use_cases.GetMovie
import com.sb.android_streaming_app.data.use_cases.GetMovies
import com.sb.android_streaming_app.data.use_cases.MovieUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

/**
 * Created by Zep S. on 03/03/2023.
 */
@Module
@InstallIn(SingletonComponent::class)
object Datamodule {

    @Provides
    fun provideMovieRepository(): MovieRepository = MovieRepositoryImpl()

    @Provides
    fun provideMovieCases(
        repo: MovieRepository
    ) = MovieUseCases(
        getMovie = GetMovie(repo),
        getMovies = GetMovies(repo),
    )
}
