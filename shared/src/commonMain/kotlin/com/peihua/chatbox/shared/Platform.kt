package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import kotlin.time.Clock
import kotlin.time.ExperimentalTime

interface Platform {
    val name: String
    val isAndroid: Boolean
    val isIOS: Boolean

    @Composable
    fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme?

    fun changeLanguage(language: String)
}

expect fun platform(): Platform

//时间毫秒
@OptIn(ExperimentalTime::class)
fun currentTimeMillis(): Long {
    return Clock.System.now().toEpochMilliseconds()
}