package com.peihua.chatbox.shared.compose.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.about
import com.peihua.chatbox.shared.components.ChatBoxTopBar
import com.peihua.chatbox.shared.compose.navigateBack
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
    Box(modifier = modifier.fillMaxSize()) {
        Text(text = stringResource(Res.string.about))
    }
}