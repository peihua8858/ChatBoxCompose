package com.peihua.chatbox.shared.compose.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.about
import com.peihua.chatbox.shared.components.ChatBoxTopBar
import com.peihua.chatbox.shared.components.ColorView
import com.peihua.chatbox.shared.components.text.ScaleText
import com.peihua.chatbox.shared.compose.navigateBack
import com.peihua.chatbox.shared.theme.Colors
import org.jetbrains.compose.resources.stringResource

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(), topBar = {
            ChatBoxTopBar(
                navigateUp = { navigateBack() },
                title = { stringResource(Res.string.about) })
        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            AboutContent()
        }
    }
}

@Composable
fun AboutContent(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(32.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            listOf(
                Colors.Red, Colors.Pink, Colors.Purple,
                Colors.DeepPurple, Colors.Indigo, Colors.Blue,
                Colors.LightBlue, Colors.Cyan, Colors.Teal,
                Colors.Green, Colors.LightGreen,
                Colors.Grey, Colors.BlueGrey, Colors.Brown,
                Colors.DeepOrange, Colors.Orange,
                Colors.Yellow, Colors.Lime, Colors.Amber
            ).forEach { colorPalette ->
                ScaleText(
                    text = colorPalette.name,
                    fontSize = 16.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                ColorView(color = colorPalette[50])
                Box(modifier = Modifier.height(2.dp)) // 分割线之间的间隔
                for (shade in 100..900 step 100) {
                    ColorView(color = colorPalette[shade])
                    Box(modifier = Modifier.height(2.dp)) // 分割线之间的间隔
                }
                Box(modifier = Modifier.height(20.dp)) // 分割线之间的间隔
            }
        }
    }
}