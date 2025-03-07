package com.peihua.chatbox.compose.about

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.peihua.chatbox.R
import com.peihua.chatbox.common.ChatBoxTopBar
import com.peihua.chatbox.compose.navigateBack

@Composable
fun AboutScreen(modifier: Modifier = Modifier) {
    Scaffold(
        modifier = modifier
            .fillMaxSize(), topBar = {
            ChatBoxTopBar(
                navigateUp = { navigateBack() },
                title = { stringResource(id = R.string.about) })
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
        Text(text = "关于")
    }
}