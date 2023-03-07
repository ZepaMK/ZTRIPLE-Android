package com.sb.android_streaming_app.app

import android.app.Application
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.sb.android_streaming_app.data.repository.MovieRepository
import com.sb.android_streaming_app.data.repository.MovieRepositoryImpl
import com.sb.android_streaming_app.data.use_cases.GetMovie
import com.sb.android_streaming_app.data.use_cases.GetMovies
import com.sb.android_streaming_app.data.use_cases.MovieUseCases
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.scopes.ViewModelScoped
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(ViewModelComponent::class)
object AppModule {

    @Provides
    @ViewModelScoped
    fun provideVideoPlayer(app: Application): Player {
        return ExoPlayer.Builder(app)
            .build()
    }
}
