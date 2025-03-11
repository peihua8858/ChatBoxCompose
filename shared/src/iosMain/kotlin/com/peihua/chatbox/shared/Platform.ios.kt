package com.peihua.chatbox.shared

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable
import platform.Foundation.NSNumber
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

    override fun format(value: Float, digits: Int): String {
        // 设置数字格式
        formatter.minimumFractionDigits = digits.toULong()
        formatter.maximumFractionDigits = digits.toULong()
        // 将 Float 转换为 NSNumber
        val number = NSNumber(value)

        return formatter.stringFromNumber(number) ?: this.toString()
    }

    override fun format(value: Double, digits: Int): String {
        // 设置数字格式
        formatter.minimumFractionDigits = digits.toULong()
        formatter.maximumFractionDigits = digits.toULong()
        // 将 Float 转换为 NSNumber
        val number = NSNumber(value)

        return formatter.stringFromNumber(number) ?: this.toString()
    }

    override fun format(value: Int, digits: Int): String {
        // 设置数字格式
        formatter.minimumFractionDigits = digits.toULong()
        formatter.maximumFractionDigits = digits.toULong()
        // 将 Float 转换为 NSNumber
        val number = NSNumber(value)

        return formatter.stringFromNumber(number) ?: this.toString()
    }
}

private val mPlatform: Platform = IOSPlatform()
actual fun platform(): Platform = mPlatform

actual fun Float.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Double.format(digits: Int): String = mPlatform.format(this, digits)
actual fun Int.format(digits: Int): String = mPlatform.format(this, digits)