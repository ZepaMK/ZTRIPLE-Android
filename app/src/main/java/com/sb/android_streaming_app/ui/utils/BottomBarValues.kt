package com.triple.androidstreamingapp.ui.screens.utils

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Movie
import androidx.compose.material.icons.filled.Settings
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomBarValues(
    val route: String,
    val title: String,
    val icon: ImageVector
) {
    object Home : BottomBarValues(
        route = "HOME",
        title = "HOME",
        icon = Icons.Default.Home
    )

    object Movie : BottomBarValues(
        route = "MOVIE",
        title = "MOVIE",
        icon = Icons.Default.Movie
    )

    object Settings : BottomBarValues(
        route = "SETTINGS",
        title = "SETTINGS",
        icon = Icons.Default.Settings
    )
}
