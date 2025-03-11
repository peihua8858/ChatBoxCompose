package com.peihua.chatbox.shared

import java.util.Locale

 class AndroidLocaleProvider(local: String) : LocaleProvider(local) {
    val local = Locale(local)
    override fun displayName(): String {
        return local.displayName
    }
}

actual fun String.localeProvider(): LocaleProvider = AndroidLocaleProvider(this)