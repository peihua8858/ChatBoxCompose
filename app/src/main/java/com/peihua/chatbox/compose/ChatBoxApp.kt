package com.peihua.chatbox.compose

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peihua.chatbox.compose.home.HomeScreen
import com.peihua.chatbox.compose.settings.SettingsScreen

@Composable
fun ChatBoxApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    ChatBoxNavHost(
        navController = navController, modifier
    )
}

@Composable
fun ChatBoxNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = ScreenRouter.Home.route) {
        composable(route = ScreenRouter.Home.route) {
            navController.HomeScreen(modifier)
        }
        composable(route = ScreenRouter.Settings.route) {
            navController.SettingsScreen()
        }
    }
}