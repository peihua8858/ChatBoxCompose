package com.peihua.chatbox

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import com.peihua.chatbox.shared.compose.ChatBoxApp
import com.peihua.chatbox.shared.theme.ThemeMode
import com.peihua.chatbox.shared.theme.argb

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        enableEdgeToEdge()
        setContent {
            ChatBoxApp { model,colorScheme ->
                window.decorView.setOnApplyWindowInsetsListener { view, insets ->
                    val windowInsets =
                        WindowInsetsCompat.toWindowInsetsCompat(window.decorView.rootWindowInsets)
                    val statusBarInsets =
                        windowInsets.getInsets(WindowInsetsCompat.Type.statusBars())
                    val color = argb(
                        colorScheme.surface.alpha,
                        colorScheme.surface.red,
                        colorScheme.surface.green,
                        colorScheme.surface.blue
                    )
                    view.setBackgroundColor(color)
                    //状态栏高亮flag
                    WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars =
                        model == ThemeMode.light
                    // Adjust padding to avoid overlap
                    view.setPadding(0, statusBarInsets.top, 0, 0)
                    insets
                }
            }
        }

    }
}