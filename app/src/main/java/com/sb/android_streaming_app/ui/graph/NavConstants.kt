package com.sb.android_streaming_app.ui.graph

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.ui.graphics.vector.ImageVector

/**
 * Created by Zep S. on 03/03/2023.
 */
sealed class NavConstants(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : NavConstants(
        route = "HOME",
        title = "HOME",
        icon = Icons.Default.Home
    )

    object Movie : NavConstants(
        route = "MOVIE",
        title = "MOVIE",
        icon = Icons.Default.Movie
    )
}
