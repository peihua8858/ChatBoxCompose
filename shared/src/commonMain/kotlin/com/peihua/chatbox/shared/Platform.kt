package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

interface Platform {
    val name: String
    val isAndroid: Boolean
    val isIOS: Boolean

    @Composable
    fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme?
}

expect fun platform(): Platform