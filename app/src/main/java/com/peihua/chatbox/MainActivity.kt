package com.peihua.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.peihua.chatbox.shared.compose.ChatBoxApp
import com.peihua.chatbox.ui.theme.ChatBoxTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBoxTheme {
                ChatBoxApp()
            }
        }
    }
}