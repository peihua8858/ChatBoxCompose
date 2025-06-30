package com.peihua.chatbox.shared.compose.settings.tabs.chat

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.settingsNewChatTips
import chatboxcompose.shared.generated.resources.settingsResetDefault
import chatboxcompose.shared.generated.resources.settingsSpellCheck
import com.peihua.chatbox.shared.components.SwitchListTile
import com.peihua.chatbox.shared.components.text.ScaleText
import com.peihua.chatbox.shared.compose.changeSettings
import com.peihua.chatbox.shared.compose.settings
import org.jetbrains.compose.resources.stringResource

@Composable
fun ChatSettingsScreen(modifier: Modifier = Modifier) {
    val scrollState = rememberScrollState()
    val chatTips =
        remember { mutableStateOf("You are a helpful assistant. You can help me by answering my questions. You can also ask me questions.") }
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
    ) {
        OutlinedTextField(
            value = chatTips.value,
            label = { ScaleText(text = stringResource(Res.string.settingsNewChatTips)) },
            onValueChange = {
                chatTips.value = it
            })
        ScaleText(
            text = stringResource(Res.string.settingsResetDefault),
            style = MaterialTheme.typography.labelSmall,
            modifier = Modifier
                .padding(top = 8.dp)
                .clickable {
                    chatTips.value = ""
                })
        SwitchListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = settings.value.display.spellCheck,
            onCheckedChange = {
                changeSettings(display = settings.value.display.copy(spellCheck = it))
            }) {
            ScaleText(
                modifier = Modifier.padding(start = 8.dp),
                text = stringResource(Res.string.settingsSpellCheck)
            )
        }
    }
}
