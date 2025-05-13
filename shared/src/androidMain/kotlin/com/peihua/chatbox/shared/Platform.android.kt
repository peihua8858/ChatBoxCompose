package com.peihua.chatbox.shared

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.runtime.Composable
import com.peihua.chatbox.shared.utils.dLog
import java.util.Locale

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
        dLog { "changeLanguage: $language" }
        setLocale(LocalContext.context, language)
    }
    fun setLocale(context: Context, localeCode: String) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            context.getSystemService(LocaleManager::class.java).applicationLocales =
                LocaleList.forLanguageTags(localeCode)
        } else {
            saveToLocalSharedAndUpdateResources(context = context, localeTag = localeCode)
        }
    }
    private fun saveToLocalSharedAndUpdateResources(context: Context, localeTag: String) {
        setLocaleForDevicesLowerThanTiramisu(localeTag, context)
    }
    private fun setLocaleForDevicesLowerThanTiramisu(localeTag: String, context: Context) {
        val locale = Locale(localeTag)
        Locale.setDefault(locale)
        val resources = context.resources
        val configuration = resources.configuration
        configuration.setLocale(locale)
        resources.updateConfiguration(configuration, resources.displayMetrics)
    }
}

private val mPlatform: Platform = AndroidPlatform()
actual fun platform(): Platform = mPlatform