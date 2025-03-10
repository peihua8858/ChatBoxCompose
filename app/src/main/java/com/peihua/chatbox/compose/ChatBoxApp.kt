package com.peihua.chatbox.compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.VisibilityThreshold
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavDirections
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peihua.chatbox.compose.about.AboutScreen
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
fun SetLanguage(locale: Locale) {
    val configuration = LocalConfiguration.current
    configuration.setLocale(locale.platformLocale)
    val resources = LocalContext.current.resources
    resources.updateConfiguration(configuration, resources.displayMetrics)
}

/**
 * 导航Host
 * 页面切换右进右出
 * @param navController 导航控制器
 * @param modifier 修饰符
 */
@Composable
fun ChatBoxNavHost(navController: NavHostController, modifier: Modifier = Modifier) {
    NavHost(
        navController = navController, startDestination = ScreenRouter.Home.route,
        enterTransition = {
            slideIn(tween(700, easing = LinearOutSlowInEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            }
        },
        exitTransition = {
            fadeOut(animationSpec = tween(1500))
        },
        popEnterTransition = {
            fadeIn(animationSpec = tween(500))
        },
        popExitTransition = {
            slideOut(tween(700, easing = FastOutSlowInEasing)) { fullSize ->
                IntOffset(fullSize.width, 0)
            }
        }
    ) {
        composable(route = ScreenRouter.Home.route) {
            HomeScreen(modifier)
        }
        composable(route = ScreenRouter.Settings.route) {
            SettingsScreen()
        }
        composable(route = ScreenRouter.About.route) {
            AboutScreen()
        }
    }
}