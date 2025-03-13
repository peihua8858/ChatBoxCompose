package com.peihua.chatbox.shared.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.peihua.chatbox.shared.compose.about.AboutScreen
import com.peihua.chatbox.shared.compose.home.HomeScreen
import com.peihua.chatbox.shared.compose.settings.SettingsScreen

private lateinit var appRouter: NavHostController

fun navigateBack() {
    check(::appRouter.isInitialized)
    appRouter.navigateUp()
}

fun popBackStack() {
    check(::appRouter.isInitialized)
    appRouter.popBackStack()
}

fun navigateTo(route: String) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route)
}

fun <T : Any> navigateTo(
    route: T, navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, navOptions, navigatorExtras)

}

fun navigateTo(route: String, builder: NavOptionsBuilder.() -> Unit) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, builder)
}

fun navigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, navOptions, navigatorExtras)
}

@Composable
fun ChatBoxApp(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    appRouter = navController
    ChatBoxNavHost(
        navController = navController, modifier
    )
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