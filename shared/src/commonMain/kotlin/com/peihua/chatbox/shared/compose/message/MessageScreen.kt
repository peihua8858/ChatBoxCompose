package com.peihua.chatbox.shared.compose.message

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MessageScreen(menuId: String, modifier: Modifier = Modifier) {

    MessageContent(modifier)
}

@Composable
fun MessageContent(modifier: Modifier = Modifier) {
    Box(
        modifier = Modifier
            .padding(32.dp)
    ) {
        Column(
            modifier = modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
        }
    }
}
