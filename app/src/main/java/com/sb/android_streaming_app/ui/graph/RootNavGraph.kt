package com.sb.android_streaming_app.ui.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sb.android_streaming_app.ui.screens.detail.DetailScreen
import com.sb.android_streaming_app.ui.screens.home.HomeScreen

/**
 * Created by Zep S. on 03/03/2023.
 */

/**
 * This class manages the navigation of the application.
 * To learn more
 * @param navController
 */
@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = NavConstants.Home.route
    ) {
        composable(route = NavConstants.Home.route) {
            HomeScreen(onClick = { navController.navigate("${NavConstants.Movie.route}/${it}") })
        }
        composable(
            "${NavConstants.Movie.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { DetailScreen(it.getInt("id")) }
        }
    }
}
