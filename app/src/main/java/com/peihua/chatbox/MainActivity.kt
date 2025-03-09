package com.peihua.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.peihua.chatbox.compose.ChatBoxApp
import com.peihua.chatbox.ui.theme.ChatBoxComposeTheme
import dev.tclement.fonticons.IconFont
import dev.tclement.fonticons.LocalIconFont
import dev.tclement.fonticons.LocalIconSize
import dev.tclement.fonticons.LocalIconTint
import dev.tclement.fonticons.LocalIconWeight
import dev.tclement.fonticons.ProvideIconParameters
import dev.tclement.fonticons.StaticIconFont
import dev.tclement.fonticons.rememberStaticIconFont

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val font = Font(R.font.materialicons_regular)
            val iconFont = rememberStaticIconFont(font)
            ChatBoxComposeTheme {
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