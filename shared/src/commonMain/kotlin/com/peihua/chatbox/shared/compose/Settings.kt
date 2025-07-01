package com.peihua.chatbox.shared.compose

import com.peihua.chatbox.shared.compose.settings.tabs.display.DisplaySettings
import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelSettings
import com.peihua.chatbox.shared.compose.settings.tabs.other.OtherSettings
import kotlinx.serialization.Serializable

@Serializable
data class Settings(
    var display: DisplaySettings,
    var modelProvider: ModelSettings,
    var proxy: OtherSettings,
) {
    companion object {
        fun default(): Settings {
            return Settings(
                display = DisplaySettings.default(),
                modelProvider = ModelSettings.default(),
                proxy = OtherSettings.default(),
            )
        }

    }
}
