package com.peihua.chatbox.shared.compose.settings.tabs.display

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Brightness4
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.dark_mode
import chatboxcompose.shared.generated.resources.language
import chatboxcompose.shared.generated.resources.language_list_value
import chatboxcompose.shared.generated.resources.settingsShowModelName
import chatboxcompose.shared.generated.resources.settingsShowTokenCount
import chatboxcompose.shared.generated.resources.settingsShowTokenUsage
import chatboxcompose.shared.generated.resources.settingsShowWordCount
import chatboxcompose.shared.generated.resources.system_mode
import chatboxcompose.shared.generated.resources.text_font_size
import chatboxcompose.shared.generated.resources.theme
import com.peihua.chatbox.shared.components.ChatBoxSliderTips
import com.peihua.chatbox.shared.components.CheckboxListTile
import com.peihua.chatbox.shared.components.text.ScaleText
import com.peihua.chatbox.shared.compose.changeSettings
import com.peihua.chatbox.shared.compose.settings
import com.peihua.chatbox.shared.localeProvider
import com.peihua.chatbox.shared.theme.ThemeMode
import kotlinx.serialization.Serializable
import org.jetbrains.compose.resources.stringArrayResource
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySettingsScreen(modifier: Modifier = Modifier) {
    val languageCodes = stringArrayResource(Res.array.language_list_value)
    val localeList = languageCodes.map { Locale(it) }
    val isExpanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(Locale.current) }
    val displaySettings = settings.value.display
    val textScaler = displaySettings.textScaler
    val sliderPosition = remember { mutableFloatStateOf(textScaler.scale) }
    val colorScheme = MaterialTheme.colorScheme
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        ExposedDropdownMenuBox(
            modifier = Modifier.fillMaxWidth(),
            expanded = isExpanded.value,
            onExpandedChange = { isExpanded.value = it },
        ) {
            OutlinedTextField(
                value = selectedOption.value.displayName,
                onValueChange = {
                },
                label = { ScaleText(stringResource(Res.string.language)) },
                readOnly = true,
                textStyle = MaterialTheme.typography.labelMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .menuAnchor(MenuAnchorType.PrimaryNotEditable)
            )
            ExposedDropdownMenu(
                expanded = isExpanded.value,
                onDismissRequest = { isExpanded.value = false },
            ) {
                localeList.forEach { locale ->
                    val selected = selectedOption.value.toLanguageTag() == locale.toLanguageTag()
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (selected) colorScheme.secondaryContainer else Color.Transparent),
                        text = {
                            ScaleText(
                                text = locale.displayName,
                                color = if (selected) colorScheme.onSecondaryContainer else colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            selectedOption.value = locale
                             changeSettings(display = displaySettings.copy(language = locale.toLanguageTag()))
                            isExpanded.value = !isExpanded.value
                        },
                    )
                }
            }
        }
        ChatBoxSliderTips(
            modifier = Modifier.padding(top = 16.dp),
            value = sliderPosition.value,
            title = stringResource(Res.string.text_font_size),
            steps = 2,
            valueRange = 0.8f..1.4f,
            thumbText = {
                TextScaler.parse(it)
            },
            onChangValue = {
                sliderPosition.value = it
                 changeSettings(display = displaySettings.copy(textScaler = textScaler.copy(scale = it)))
            })
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            ScaleText(text = stringResource(Res.string.theme))
            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.padding(
                    start = 16.dp,
                    end = 16.dp
                )
            ) {
                val colors = SegmentedButtonDefaults.colors().copy(
                    activeContainerColor = colorScheme.primary,
                    activeContentColor = colorScheme.onPrimary,
                    activeBorderColor = colorScheme.primary,
                    inactiveContainerColor = colorScheme.onPrimary,
                    inactiveContentColor = colorScheme.primary,
                    inactiveBorderColor = colorScheme.primary,
                )
                SegmentedButton(
                    colors = colors,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = ThemeMode.System.index,
                        count = 3
                    ),
                    onClick = {  changeSettings(display = displaySettings.copy(themeMode = ThemeMode.System)) },
                    selected = ThemeMode.System == displaySettings.themeMode,
                    icon = {

                    },
                    label = {
                        Icon(
                            Icons.Default.Brightness4,
                            contentDescription = stringResource(Res.string.system_mode)
                        )
                    }
                )
                SegmentedButton(
                    colors = colors,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = ThemeMode.Light.index,
                        count = 3
                    ),
                    onClick = {  changeSettings(display = displaySettings.copy(themeMode = ThemeMode.Light)) },
                    selected = ThemeMode.Light == displaySettings.themeMode,
                    icon = {

                    },
                    label = {
                        Icon(
                            Icons.Default.LightMode,
                            contentDescription = stringResource(Res.string.dark_mode)
                        )
                    }
                )
                SegmentedButton(
                    colors = colors,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = ThemeMode.Dark.index,
                        count = 3
                    ),
                    onClick = {  changeSettings(display = displaySettings.copy(themeMode = ThemeMode.Dark)) },
                    selected = ThemeMode.Dark == displaySettings.themeMode,
                    icon = {

                    },
                    label = {
                        Icon(
                            Icons.Default.DarkMode,
                            contentDescription = stringResource(Res.string.dark_mode)
                        )
                    }
                )
            }
        }
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = displaySettings.showWordCount,
            onCheckedChange = {
            },
            title = {
                ScaleText(text = stringResource(Res.string.settingsShowWordCount))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = displaySettings.showTokenCount,
            onCheckedChange = {
                 changeSettings(display = displaySettings.copy(showTokenCount = it))
            },
            title = {
                ScaleText(text = stringResource(Res.string.settingsShowTokenCount))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = displaySettings.showTokenUsage,
            onCheckedChange = {
                 changeSettings(display = displaySettings.copy(showTokenUsage = it))
            },
            title = {
                ScaleText(text = stringResource(Res.string.settingsShowTokenUsage))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = displaySettings.showModelName,
            onCheckedChange = {
                changeSettings(display = displaySettings.copy(showModelName = it))
            },
            title = {
                ScaleText(text = stringResource(Res.string.settingsShowModelName))
            }
        )
    }
}

data class LangDisplayOption(val title: String, val subtitle: String, val locale: Locale) {
    override fun toString(): String {
        return title
    }
}


val Locale.displayName: String
    get() = language.localeProvider().displayName()


@Serializable
data class DisplaySettings(
    val themeMode: ThemeMode,
    val language: String,
    val showAvatar: Boolean,
    val showWordCount: Boolean,
    val showTokenCount: Boolean,
    val showModelName: Boolean,
    val showTokenUsage: Boolean,
    val spellCheck: Boolean,
    val textScaler: TextScaler,
) {
    companion object {
        fun default(): DisplaySettings {
            return DisplaySettings(
                themeMode = ThemeMode.System,
                language = Locale.current.toLanguageTag(),
                showAvatar = true,
                showWordCount = true,
                showTokenCount = true,
                showModelName = true,
                showTokenUsage = true,
                spellCheck = true,
                textScaler = TextScaler(1.0f, "Normal"),
            )
        }
    }
}