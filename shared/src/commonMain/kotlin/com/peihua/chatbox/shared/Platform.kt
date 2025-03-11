package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

interface Platform {
    val name: String
    val isAndroid: Boolean
    val isIOS: Boolean

    @Composable
    fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme?
    fun format(value: Float, digits: Int): String
    fun format(value: Double, digits: Int): String
    fun format(value: Int, digits: Int): String
}

expect fun platform(): Platform
expect fun Float.format(digits: Int): String
expect fun Double.format(digits: Int): String
expect fun Int.format(digits: Int): String