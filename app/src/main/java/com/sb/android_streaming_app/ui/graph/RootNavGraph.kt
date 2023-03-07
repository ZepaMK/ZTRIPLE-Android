package com.sb.android_streaming_app.ui.graph

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.sb.android_streaming_app.ui.screens.detail.DetailScreen
import com.sb.android_streaming_app.ui.screens.home.HomeScreen
import com.sb.android_streaming_app.ui.screens.settings.SettingsScreen
import com.sb.android_streaming_app.ui.utils.BottomBarValues

@Composable
fun RootNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = BottomBarValues.Home.route
    ) {
        composable(route = BottomBarValues.Home.route) {
            HomeScreen(onClick = { navController.navigate("${BottomBarValues.Movie.route}/${it}") })
        }
        composable(
            "${BottomBarValues.Movie.route}/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            backStackEntry.arguments?.let { DetailScreen(it.getInt("id")) }
        }

        composable(route = BottomBarValues.Settings.route) {
            SettingsScreen()
        }
    }
}
