package com.peihua.chatbox.shared

import android.os.Build
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable

class AndroidPlatform : Platform {
    override val name: String = "Android"
    override val isAndroid: Boolean = true
    override val isIOS: Boolean = false

    @Composable
    override fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val context = androidx.compose.ui.platform.LocalContext.current
            if (isDarkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        } else return null
    }

    override fun changeLanguage(language: String) {
        MultiLanguage.changeLanguage(LocalContext.context, language)
    }
}

private val mPlatform: Platform = AndroidPlatform()
actual fun platform(): Platform = mPlatform