package com.peihua.chatbox.shared

class AndroidFormater : Formater() {
    override fun format(value: Float, digits: Int): String {
        return "%.${digits}f".format(value)
    }

    override fun format(value: Double, digits: Int): String {
        return "%.${digits}f".format(value)
    }
}

private val mFormater: Formater = AndroidFormater()
actual fun formater(): Formater = mFormater
