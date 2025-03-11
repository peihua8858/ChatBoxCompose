package com.peihua.chatbox.shared

abstract class LocaleProvider(val locale: String) {
    abstract fun displayName(): String
}

expect fun String.localeProvider(): LocaleProvider