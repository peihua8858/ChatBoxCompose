package com.peihua.chatbox.shared.compose.settings.tabs.model

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.model
import chatboxcompose.shared.generated.resources.settingsModelAndTokenTips
import chatboxcompose.shared.generated.resources.settingsModelProviderModelAndToken
import chatboxcompose.shared.generated.resources.settingsModelProviderOpenAI
import chatboxcompose.shared.generated.resources.settingsModelProviderOpenAIHost
import chatboxcompose.shared.generated.resources.settingsModelTemperature
import com.peihua.chatbox.shared.components.ExtendedListTile
import com.peihua.chatbox.shared.components.ChatBoxSliderTips
import com.peihua.chatbox.shared.theme.Colors
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenAiSettingsContent(
    modifier: Modifier = Modifier,
    model: Model,
    modelChange: (Model) -> Unit,
) {
    val apiKey = remember { mutableStateOf(model.apiKey) }
    val hostState = remember { mutableStateOf(model.host) }
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        OutlinedTextField(
            value = apiKey.value,
            onValueChange = {
                apiKey.value = it
                modelChange(model.copy(apiKey = it))
            },
            label = { Text(stringResource(Res.string.settingsModelProviderOpenAI)) },
            textStyle = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        OutlinedTextField(
            value = hostState.value,
            onValueChange = {
                hostState.value = it
                modelChange(model.copy(host = it))
            },
            label = { Text(stringResource(Res.string.settingsModelProviderOpenAIHost)) },
            textStyle = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth()
        )
        ExtendedListTile(
            modifier = Modifier
                .padding(top = 16.dp),
            isExtended = false,
            title = { isExtended ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.secondaryContainer)
                        .padding(16.dp)

                ) {
                    Icon(
                        imageVector = if (isExtended) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = ""
                    )
                    Text(
                        text = stringResource(Res.string.settingsModelProviderModelAndToken),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                ModelAndToken(
                    modifier = Modifier.padding(8.dp),
                    selectedOption = model
                ) {
                    modelChange(it)
                }
            })
    }
}

@Composable
fun ModelAndToken(
    modifier: Modifier = Modifier,
    selectedOption: Model,
    changeModel: (Model) -> Unit,
) {
    val modelState = remember { mutableStateOf(selectedOption.model) }
    val temperatureState = remember { mutableStateOf(selectedOption.temperature) }
    Column {
        Text(
            text = stringResource(Res.string.settingsModelAndTokenTips),
            style = MaterialTheme.typography.bodySmall,
            color = Colors.Yellow[500]
        )
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = modelState.value,
            onValueChange = {
                modelState.value = it
                changeModel(selectedOption.copy(model = it))
            },
            label = { Text(stringResource(Res.string.model)) },
            textStyle = MaterialTheme.typography.labelMedium,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))
        ChatBoxSliderTips(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            value = temperatureState.value,
            titleOrientation = Orientation.Vertical,
            title = stringResource(Res.string.settingsModelTemperature),
            valueRange = 0.5f..2f,
            onChangValue = {
                temperatureState.value = it
                changeModel(selectedOption.copy(temperature = it))
            })
        Spacer(modifier = Modifier.height(8.dp))
        ChatBoxSliderTips(
            modifier = Modifier.padding(top = 16.dp, start = 16.dp),
            value = temperatureState.value,
            titleOrientation = Orientation.Vertical,
            title = stringResource(Res.string.settingsModelTemperature),
            valueRange = 0.5f..2f,
            onChangValue = {
                temperatureState.value = it
                changeModel(selectedOption.copy(temperature = it))
            })
    }
}