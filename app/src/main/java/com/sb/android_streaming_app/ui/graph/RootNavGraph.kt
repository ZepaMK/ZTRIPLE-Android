package com.sb.android_streaming_app.ui.graph

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sb.android_streaming_app.ui.screens.detail.DetailScreen
import com.sb.android_streaming_app.ui.screens.home.HomeScreen
import com.sb.android_streaming_app.ui.screens.stream.StreamScreen
import com.sb.android_streaming_app.ui.utils.SocketHandler
import com.sb.android_streaming_app.ui.utils.SocketHandler.mSocket

/**
 * Created by Zep S. on 03/03/2023.
 */

/**
 * This class manages the navigation of the application.
 * To learn more
 * @param navController
 */
@Composable
fun RootNavGraph(navController: NavHostController, device: String) {
    NavHost(
        navController = navController,
        startDestination = NavConstants.Home.route
    ) {
        composable(route = NavConstants.Home.route) {
            HomeScreen(onClick = {
                navController.navigate("${NavConstants.Movie.route}/${it}")
                if (mSocket != null) {
                    mSocket?.emit("play-vod", 1, "userId")
                }
            })
        }
        composable(
            "${NavConstants.Movie.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            if (mSocket?.connected() == true) {
                backStackEntry.arguments?.let { StreamScreen(it.getInt("id"), device) }
            } else {
                backStackEntry.arguments?.let { DetailScreen(it.getInt("id")) }
            }
        }
    }
}
