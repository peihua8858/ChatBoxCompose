package com.peihua.chatbox.shared.compose

import androidx.compose.material3.ColorScheme
import com.peihua.chatbox.shared.compose.settings.tabs.display.TextScalerData
import com.peihua.chatbox.shared.theme.ThemeMode

data class AppConfig(
    val themeMode: ThemeMode,
    val language: String,
    val fontTextScalerData: TextScalerData

) {

}