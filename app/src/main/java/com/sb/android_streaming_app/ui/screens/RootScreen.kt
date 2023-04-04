package com.sb.android_streaming_app.ui.screens

import android.annotation.SuppressLint
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Cast
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import com.sb.android_streaming_app.R
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.sb.android_streaming_app.ui.graph.RootNavGraph
import com.sb.android_streaming_app.ui.components.ConnectedDialog
import com.sb.android_streaming_app.ui.graph.NavConstants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by Zep S. on 03/03/2023.
 */
@SuppressLint("UnusedMaterialScaffoldPaddingParameter", "CoroutineCreationDuringComposition")
@Composable
fun RootScreen(viewModel: RootViewModel = hiltViewModel()) {
    val navController = rememberNavController()
    val scaffoldState: ScaffoldState = rememberScaffoldState()
    val coroutineScope: CoroutineScope = rememberCoroutineScope()

    Scaffold(
        backgroundColor = MaterialTheme.colors.primary,
        scaffoldState = scaffoldState,
        topBar = {
            TopAppBar(
                {
                    Box(
                        modifier = Modifier
                            .padding(start = 40.dp)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = stringResource(id = R.string.app_name))
                    }
                },
                backgroundColor = MaterialTheme.colors.primary,
                actions = {
                    TopAppBarActionButton(
                        imageVector = Icons.Outlined.Cast,
                        description = "stream",
                        connected = viewModel.connected.value
                    ) {
                        if (viewModel.connected.value) viewModel.openConnectedDialog() else viewModel.openDevicePicker()
                    }
                }
            )
        },
        bottomBar = { BottomBar(navController = navController) }
    ) {
        RootNavGraph(navController = navController)
        if (viewModel.connected.value) {
            ConnectedDialog(
                viewModel.connected,
                viewModel.dialogOpen,
                viewModel.device,
            ) { viewModel.deviceDisconnected(false) }
        }
        Log.d("2ndScreenAPP", viewModel.lauched.value.toString())

        when (viewModel.lauched.value) {
            1 -> coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "Connecting to ${viewModel.device.friendlyName}") }
            2 -> coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "App launched on ${viewModel.device.friendlyName}") }
            3 -> coroutineScope.launch { scaffoldState.snackbarHostState.showSnackbar(message = "App could not launch, did you install the app on your TV?") }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController) {
    val screens = listOf(
        NavConstants.Home,
    )
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val bottomBarDestination = screens.any { it.route == currentDestination?.route }
    if (bottomBarDestination) {
        BottomNavigation {
            screens.forEach { screen ->
                AddItem(
                    screen = screen,
                    currentDestination = currentDestination,
                    navController = navController
                )
            }
        }
    }
}

@Composable
fun RowScope.AddItem(
    screen: NavConstants,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    BottomNavigationItem(
        label = {
            Text(text = screen.title)
        },
        icon = {
            Icon(
                imageVector = screen.icon,
                contentDescription = "Navigation Icon"
            )
        },
        selected = currentDestination?.hierarchy?.any {
            it.route == screen.route
        } == true,
        unselectedContentColor = LocalContentColor.current.copy(alpha = ContentAlpha.disabled),
        onClick = {
            navController.navigate(screen.route) {
                popUpTo(navController.graph.findStartDestination().id)
                launchSingleTop = true
            }
        }
    )
}

@Composable
fun TopAppBarActionButton(
    imageVector: ImageVector,
    description: String,
    connected: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = {
        onClick()
    }) {
        Icon(
            imageVector = imageVector,
            contentDescription = description,
            tint = if (connected) Color.White else Color.DarkGray
        )
    }
}
