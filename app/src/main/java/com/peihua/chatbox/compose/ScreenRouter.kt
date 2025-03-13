package com.peihua.chatbox.compose

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument

sealed class ScreenRouter(
    val route: String,
    val navArguments: List<NamedNavArgument> = emptyList()
) {
    /**
     * 首页
     */
    data object Home : ScreenRouter("home")

    /**
     * 消息
     */
    data class Message(val menuId: String) :
        ScreenRouter("message/$menuId", listOf(navArgument("menuId") {
            type = NavType.StringType
            defaultValue = "New Chat"
        }))
    /**
     * 设置
     */
    data object Settings : ScreenRouter("settings")
    /**
     * 关于
     */
    data object About : ScreenRouter("about")
}