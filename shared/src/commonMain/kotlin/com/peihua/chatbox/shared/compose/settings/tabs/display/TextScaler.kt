package com.peihua.chatbox.shared.compose.settings.tabs.display

import androidx.compose.runtime.Composable
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.text_font_extraLarge
import chatboxcompose.shared.generated.resources.text_font_large
import chatboxcompose.shared.generated.resources.text_font_nomal
import chatboxcompose.shared.generated.resources.text_font_small
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringResource

@Serializable
data class TextScaler(val scale: Float, val name: String) {
    companion object {
        @Composable
        fun create(d: Float): TextScaler {
            return TextScaler(d, parse(d))
        }

        @Composable
        fun parse(d: Float): String {
            return when (d) {
                0.8f -> stringResource(Res.string.text_font_small)
                1.2f -> stringResource(Res.string.text_font_large)
                1.4f -> stringResource(Res.string.text_font_extraLarge)
                else -> stringResource(Res.string.text_font_nomal)
            }
        }

        val Default = TextScaler(1.0f, "Normal")
    }
}