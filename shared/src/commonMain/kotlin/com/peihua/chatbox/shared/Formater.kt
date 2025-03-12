package com.peihua.chatbox.shared

abstract class Formater {
    companion object {
        fun format(value: Float, digits: Int): String {
            return formater().format(value, digits)
        }

        fun format(value: Double, digits: Int): String {
            return formater().format(value, digits)
        }
    }

    abstract fun format(value: Float, digits: Int): String
    abstract fun format(value: Double, digits: Int): String
}

expect fun formater(): Formater
fun Float.format(digits: Int): String = formater().format(this, digits)
fun Double.format(digits: Int): String = formater().format(this, digits)