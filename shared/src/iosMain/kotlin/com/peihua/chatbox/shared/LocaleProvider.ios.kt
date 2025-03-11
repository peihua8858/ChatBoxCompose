package com.peihua.chatbox.shared

import platform.Foundation.NSLocale

class IosLocaleProvider(locale: String) : LocaleProvider(locale) {
    val nsLocale = NSLocale(locale)
    override fun displayName(): String {
        return nsLocale.displayNameForKey("NSLocaleLocaleIdentifier", locale) as String
    }
}

actual fun String.localeProvider(): LocaleProvider = IosLocaleProvider(this)