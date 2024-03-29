package com.sb.android_streaming_app.data.models

/**
 * Created by Zep S. on 03/03/2023.
 */
sealed class Response<out T> {
    object Loading: Response<Nothing>()

    data class Success<out T>(
        val data: T
    ): Response<T>()

    data class Failure(
        val e: Exception?
    ): Response<Nothing>()
}
