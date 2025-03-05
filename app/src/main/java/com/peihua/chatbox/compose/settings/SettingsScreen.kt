package com.peihua.chatbox.compose.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.peihua.chatbox.compose.ChatBoxTopBar

@Composable
fun NavHostController.SettingsScreen() {
    Scaffold(topBar = {
        ChatBoxTopBar(
            navigateUp = {
                popBackStack()
            },
            title = {
                "设置"
            },
        )
    }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            Text(text = "设置页面")
        }
    }
}