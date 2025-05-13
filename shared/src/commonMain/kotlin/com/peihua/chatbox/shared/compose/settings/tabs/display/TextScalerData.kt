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
data class TextScalerData(val textScaler: Float, val textScalerName: String) {
    companion object {
        @Composable
        fun create(d: Float): TextScalerData {
            val name: String = when (d) {
                0.5f -> stringResource(Res.string.text_font_small)
                1.5f -> stringResource(Res.string.text_font_large)
                2.0f -> stringResource(Res.string.text_font_extraLarge)
                else -> stringResource(Res.string.text_font_nomal)
            }
            return TextScalerData(d, name)
        }
    }
}