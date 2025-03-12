package com.peihua.chatbox.shared

import platform.Foundation.NSNumber
import platform.Foundation.NSNumberFormatter

class IosFormater : Formater() {
    // 创建 NSNumberFormatter
    val formatter = NSNumberFormatter()
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
}

private val mFormater: Formater = IosFormater()
actual fun formater(): Formater = mFormater
