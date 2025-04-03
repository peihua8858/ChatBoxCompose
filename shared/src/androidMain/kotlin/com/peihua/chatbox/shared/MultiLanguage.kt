package com.peihua.chatbox.shared

import android.annotation.TargetApi
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import android.os.LocaleList
import java.util.Locale

class MultiLanguage private constructor() {
    init {
        throw AssertionError()
    }

    /**
     * 获取当前设置的语言监听器
     *
     * @author dingpeihua
     * @version 1.0
     * @date 2020/1/8 10:42
     */
    interface LanguageLocalListener {
        /**
         * 获取选择设置语言
         *
         * @param context
         * @return
         */
        fun getLanguageLocale(context: Context?): Locale
    }

    companion object {
        private var languageLocalListener: LanguageLocalListener? = null

        fun initialize(listener: LanguageLocalListener?) {
            languageLocalListener = listener
        }

        fun attachContext(context: Context): Context? {
            try {
                return updateResources(context, getLanguageLocale(context))
            } catch (e: Exception) {
                e.printStackTrace()
            }
            return context
        }

        fun updateResources(context: Context, locale: Locale): Context? {
            val configuration = context.getResources().getConfiguration()
            configuration.setLocale(locale)
            configuration.setLocales(LocaleList(locale))
            configuration.setLayoutDirection(locale)
            return context.createConfigurationContext(configuration)
        }

        fun updateResourcesLegacy(context: Context, locale: Locale?): Context {
            val resources = context.getResources()
            val configuration = resources.getConfiguration()
            configuration.setLocale(locale)
            configuration.setLayoutDirection(locale)
            resources.updateConfiguration(configuration, resources.getDisplayMetrics())
            return context
        }
        /**
         * 设置语言类型
         */
        fun changeLanguage(context: Context, language: String) {
            val resources = context.resources
            val config = resources.configuration
            val locale: Locale = Locale(language)
            config.setLocale(locale)
            val localeList = LocaleList(locale)
            config.setLocales(localeList)
            resources.updateConfiguration(config, resources.getDisplayMetrics())
        }
        /**
         * 设置语言类型
         */
        fun changeLanguage(context: Context) {
            val resources = context.getApplicationContext().getResources()
            val config = resources.getConfiguration()
            val locale: Locale = getLanguageLocale(context)
            config.setLocale(locale)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                val localeList = LocaleList(locale)
                config.setLocales(localeList)
            }
            resources.updateConfiguration(config, resources.getDisplayMetrics())
        }

        /**
         * @param context
         */
        fun onConfigurationChanged(context: Context) {
            attachContext(context)
            changeLanguage(context)
        }


        /**
         * 获取选择的语言
         *
         * @param context
         * @return
         */
        private fun getLanguageLocale(context: Context?): Locale {
            if (languageLocalListener != null) {
                return languageLocalListener!!.getLanguageLocale(context)
            }
            return Locale.ENGLISH
        }

        /**
         * 获取系统语言
         *
         * @param newConfig
         * @return
         */
        fun getSystemLocal(newConfig: Configuration): Locale? {
            val locale: Locale?
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                locale = newConfig.getLocales().get(0)
            } else {
                locale = newConfig.locale
            }
            return locale
        }

        /**
         * 获取系统语言
         *
         * @param resources
         * @return
         */
        fun getSystemLocal(resources: Resources?): Locale? {
            if (resources != null) {
                return getSystemLocal(resources.getConfiguration())
            }
            return systemLocal
        }

        val systemLocal: Locale?
            /**
             * 获取系统语言
             *
             * @return
             */
            get() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    return LocaleList.getDefault().get(0)
                } else {
                    return Locale.getDefault()
                }
            }

        /**
         * 获取系统语言
         *
         * @param context
         * @return
         */
        fun getSystemLocal(context: Context?): Locale? {
            if (context != null) {
                return getSystemLocal(context.getResources())
            }
            return systemLocal
        }
    }
}
