package com.peihua.chatbox.compose.settings.tabs.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peihua.chatbox.R
import com.peihua.chatbox.common.SwitchListTile

@Composable
fun ChatSettingsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val isSpellCheck = remember { mutableStateOf(true) }
    val chatTips =
        remember { mutableStateOf("You are a helpful assistant. You can help me by answering my questions. You can also ask me questions.") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        OutlinedTextField(
            value = chatTips.value,
            label = { Text(text = stringResource(id = R.string.settingsNewChatTips)) },
            onValueChange = {
                chatTips.value = it
            })
        Text(
            text = stringResource(id = R.string.settingsResetDefault),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    chatTips.value = ""
                })
        SwitchListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = isSpellCheck.value, onCheckedChange = {
                isSpellCheck.value = it
            }) {
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(id = R.string.settingsSpellCheck)
            )
        }
    }
}
