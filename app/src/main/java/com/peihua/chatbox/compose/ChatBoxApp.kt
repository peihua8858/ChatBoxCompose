package com.peihua.chatbox.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.navigation.NavDirections
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peihua.chatbox.compose.home.HomeScreen
import com.peihua.chatbox.compose.settings.SettingsScreen

@SuppressLint("StaticFieldLeak")
private lateinit var appRouter: NavHostController

fun navigateTo(route: String) {
    assert(::appRouter.isInitialized)
    appRouter.navigate(route)
}

fun navigateBack() {
    assert(::appRouter.isInitialized)
    appRouter.navigateUp()
}

fun popBackStack() {
    assert(::appRouter.isInitialized)
    appRouter.popBackStack()
}

fun navigateTo(route: String, builder: NavOptionsBuilder.() -> Unit) {
    assert(::appRouter.isInitialized)
    appRouter.navigate(route, builder)
}

fun navigate(directions: NavDirections, navigatorExtras: Navigator.Extras) {
    assert(::appRouter.isInitialized)
    appRouter.navigate(directions, navigatorExtras)
}

fun navigate(directions: NavDirections, navOptions: NavOptions?) {
    assert(::appRouter.isInitialized)
    appRouter.navigate(directions, navOptions)
}

fun navigate(directions: NavDirections) {
    assert(::appRouter.isInitialized)
    appRouter.navigate(directions)
}

@Composable
fun ChatBoxApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    appRouter = navController
    ChatBoxNavHost(
        navController = navController, modifier
    )
}

@Composable
fun ChatBoxNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(navController = navController, startDestination = ScreenRouter.Home.route) {
        composable(route = ScreenRouter.Home.route) {
            HomeScreen(modifier)
        }
        composable(route = ScreenRouter.Settings.route) {
            SettingsScreen()
        }
    }
}