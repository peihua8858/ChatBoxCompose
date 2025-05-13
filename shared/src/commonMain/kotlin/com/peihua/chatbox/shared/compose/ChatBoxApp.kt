package com.peihua.chatbox.shared.compose

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset
import androidx.navigation.NavHostController
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.Navigator
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.peihua.chatbox.shared.compose.about.AboutScreen
import com.peihua.chatbox.shared.compose.home.HomeScreen
import com.peihua.chatbox.shared.compose.settings.SettingsScreen
import com.peihua.chatbox.shared.compose.settings.tabs.display.TextScalerData
import com.peihua.chatbox.shared.di.FactoryImpl
import com.peihua.chatbox.shared.theme.ChatBoxTheme
import com.peihua.chatbox.shared.theme.ThemeMode
import com.peihua.chatbox.shared.utils.dLog

private lateinit var appRouter: NavHostController

/**
 * 返回上一页
 */
fun navigateBack() {
    check(::appRouter.isInitialized)
    appRouter.navigateUp()
}

/**
 * 弹出当前页面
 */
fun popBackStack() {
    check(::appRouter.isInitialized)
    appRouter.popBackStack()
}

/**
 * 跳转到指定路由
 *
 * @param route 目标路由
 */
fun navigateTo(route: String) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route)
}

/**
 * 跳转到指定路由（泛型版本）
 *
 * @param route 目标路由
 * @param navOptions 导航选项
 * @param navigatorExtras 导航额外参数
 */
fun <T : Any> navigateTo(
    route: T, navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, navOptions, navigatorExtras)

}

/**
 * 使用构建器跳转到指定路由
 *
 * @param route 目标路由
 * @param builder 构建器
 */
fun navigateTo(route: String, builder: NavOptionsBuilder.() -> Unit) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, builder)
}

/**
 * 导航到指定路由
 *
 * @param route 目标路由
 * @param navOptions 导航选项
 * @param navigatorExtras 导航额外参数
 */
fun navigate(
    route: String,
    navOptions: NavOptions? = null,
    navigatorExtras: Navigator.Extras? = null,
) {
    check(::appRouter.isInitialized)
    appRouter.navigate(route, navOptions, navigatorExtras)
}

val settings = mutableStateOf<Settings>(
    Settings(
        themeMode = ThemeMode.Light,
        language = "zh",
        showAvatar = true,
        showWordCount = true,
        showTokenCount = true,
        showModelName = true,
        showTokenUsage = true,
        spellCheck = true,
        fontTextScalerData = TextScalerData(1.0f, "Normal"),
    )
)

fun changeSettings(
    themeMode: ThemeMode = settings.value.themeMode,
    language: String = settings.value.language,
    fontTextScalerData: TextScalerData = settings.value.fontTextScalerData,
    showAvatar: Boolean = settings.value.showAvatar,
    showWordCount: Boolean = settings.value.showWordCount,
    showTokenCount: Boolean = settings.value.showTokenCount,
    showModelName: Boolean = settings.value.showModelName,
    showTokenUsage: Boolean = settings.value.showTokenUsage,
    spellCheck: Boolean = settings.value.spellCheck,
) {
    changeSettings(
        settings.value.copy(
            themeMode = themeMode,
            language = language,
            fontTextScalerData = fontTextScalerData,
            showAvatar = showAvatar,
            showWordCount = showWordCount,
            showTokenCount = showTokenCount,
            showModelName = showModelName,
            showTokenUsage = showTokenUsage,
            spellCheck = spellCheck,
        )
    )
}

fun changeSettings(st: Settings) {
    settings.value = st
    appDataStore.settings.updateSettings(st)
}

private val mFactory by lazy { FactoryImpl() }

private val appDataStore by lazy { mFactory.createDataStore() }

/**
 * 主应用组件
 *
 * @param modifier 修饰符
 * @param theme 主题设置
 */
@Composable
fun ChatBoxApp(
    modifier: Modifier = Modifier,
    theme: (themeMode: ThemeMode, colorScheme: ColorScheme) -> Unit = { model, colorScheme -> },
) {
    LaunchedEffect(settings) {
        appDataStore.settings.data.collect {
            settings.value = it
        }
    }
    dLog { "settings: ${settings.value}" }
    // 记住导航控制器实例
    val navController = rememberNavController()
    appRouter = navController
//    println("appConfig: ${appConfig.value}")
    ChatBoxTheme(settings.value) { model, colorScheme ->
        theme(model, colorScheme)
        ChatBoxNavHost(
            navController = navController, modifier
        )
    }
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