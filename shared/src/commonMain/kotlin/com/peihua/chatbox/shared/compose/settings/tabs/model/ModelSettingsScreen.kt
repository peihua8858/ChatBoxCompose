package com.peihua.chatbox.shared.compose.settings.tabs.model


import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.peihua.chatbox.shared.utils.DLog

@Composable
fun ModelSettingsScreen(modifier: Modifier = Modifier) {
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = "模型")
    }
}