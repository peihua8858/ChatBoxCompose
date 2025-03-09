package com.peihua.chatbox.compose.settings.tabs.display

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.peihua.chatbox.R

data class TextScalerData(val textScaler: Float, val textScalerName: String) {
    companion object {
        @Composable
        fun create(d: Float): TextScalerData {
            val name: String = when (d) {
                0.5f -> stringResource(R.string.text_font_small)
                1.5f -> stringResource(R.string.text_font_large)
                2.0f -> stringResource(R.string.text_font_extraLarge)
                else -> stringResource(R.string.text_font_nomal)
            }
            return TextScalerData(d, name)
        }
    }
}