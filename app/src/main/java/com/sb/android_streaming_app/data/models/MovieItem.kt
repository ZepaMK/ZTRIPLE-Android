package com.sb.android_streaming_app.data.models

import android.media.MediaMetadataRetriever

/**
 * Created by Zep S. on 03/03/2023.
 */
data class MovieItem(
    val id: Int,
    val title: String,
    val imageUrl: String,
    val movieUrl: String,
) {
    fun duration(): Long {
        val retriever = MediaMetadataRetriever()
        retriever.setDataSource(
            this.movieUrl,
            HashMap()
        )
        return retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)?.toLong()!! / 1000
    }
}

