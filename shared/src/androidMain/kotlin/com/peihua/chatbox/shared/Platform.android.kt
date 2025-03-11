package com.peihua.chatbox.shared

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val isAndroid: Boolean = true
    override val isIOS: Boolean = false

    @Composable
    override fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else return null
    }

    override fun format(value: Float, digits: Int): String {
        return "%.${digits}f".format(value)
    }

    override fun format(value: Double, digits: Int): String {
        return "%.${digits}f".format(value)
    }

    override fun format(value: Int, digits: Int): String {
        return "%.${digits}f".format(value)
    }
}

private val mPlatform: Platform = AndroidPlatform()
actual fun platform(): Platform = mPlatform

actual fun Float.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Double.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Int.format(digits: Int): String = mPlatform.format(this, digits)