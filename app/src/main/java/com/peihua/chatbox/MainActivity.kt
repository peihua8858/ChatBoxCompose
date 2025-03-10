package com.peihua.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.ui.text.font.Font
import com.peihua.chatbox.compose.ChatBoxApp
import com.peihua.chatbox.ui.theme.ChatBoxTheme
import dev.tclement.fonticons.LocalIconSize
import dev.tclement.fonticons.LocalIconWeight
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.fonticons.rememberStaticIconFont

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val font = Font(R.font.materialicons_regular)
            val iconFont = rememberStaticIconFont(font)
            ChatBoxTheme {
                ProvideIconParameters(
                    iconFont = iconFont,
                    size = LocalIconSize.current,
                    tint = MaterialTheme.colorScheme.secondaryContainer,
                    weight = LocalIconWeight.current,
                ) {
                    ChatBoxApp()
                }
            }
        }
    }
}