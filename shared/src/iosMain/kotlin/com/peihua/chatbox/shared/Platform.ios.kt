package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import platform.Foundation.NSNumberFormatter

class IOSPlatform : Platform {
    override val name: String = "iOS"
    override val isAndroid: Boolean = false
    override val isIOS: Boolean = true

    // 创建 NSNumberFormatter
    val formatter = NSNumberFormatter()

    @Composable
    override fun dynamicColorScheme(isDarkTheme: Boolean): ColorScheme? {
        return null
    }
}

private val mPlatform: Platform = IOSPlatform()
actual fun platform(): Platform = mPlatform
