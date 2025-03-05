package com.peihua.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.peihua.chatbox.compose.ChatBoxApp
import com.peihua.chatbox.ui.theme.ChatBoxComposeTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ChatBoxComposeTheme {
                ChatBoxApp()
            }
        }
    }
}