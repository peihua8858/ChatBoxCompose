package com.peihua.chatbox.shared.compose

import com.peihua.chatbox.shared.compose.settings.tabs.display.TextScaler
import com.peihua.chatbox.shared.theme.ThemeMode
import kotlinx.serialization.Serializable
@Serializable
data class Settings(
    val themeMode: ThemeMode,
    val language: String,
    val showAvatar: Boolean,
    val showWordCount: Boolean,
    val showTokenCount: Boolean,
    val showModelName: Boolean,
    val showTokenUsage: Boolean,
    val spellCheck: Boolean,
    val textScaler: TextScaler
)