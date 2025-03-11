package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable


class NativePlatform : Platform {
    override val name: String = "Android"
    override val isAndroid: Boolean = true
    override val isIOS: Boolean = false

    @Composable
    override fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme? {
        return null
    }

    override fun format(value: Float, digits: Int): String {
        return value.toString()
    }

    override fun format(value: Double, digits: Int): String {
        return value.toString()
    }

    override fun format(value: Int, digits: Int): String {
        return value.toString()
    }
}

private val mPlatform: Platform = NativePlatform()
actual fun platform(): Platform = mPlatform

actual fun Float.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Double.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Int.format(digits: Int): String = mPlatform.format(this, digits)