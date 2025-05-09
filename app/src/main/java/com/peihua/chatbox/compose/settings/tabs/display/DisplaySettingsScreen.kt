package com.peihua.chatbox.compose.settings.tabs.display

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.SliderDefaults.Track
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peihua.chatbox.R
import com.peihua.chatbox.common.CheckboxListTile
import com.peihua.chatbox.ui.theme.ThemeMode
import dev.tclement.fonticons.FontIcon

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplaySettingsScreen(modifier: Modifier = Modifier) {
    val languageCodes = stringArrayResource(id = R.array.language_list_value)
    val localeList = languageCodes.map { Locale(it) }
    val isExpanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(Locale.current) }
    val sliderPosition = remember { mutableFloatStateOf(1f) }
    val textScalerData =
        TextScalerData.create(sliderPosition.floatValue)
    var selectedIndex = remember { mutableIntStateOf(0) }
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
                label = { Text(stringResource(id = R.string.language)) },
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
                            Text(
                                text = locale.displayName,
                                color = if (selected) colorScheme.onSecondaryContainer else colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            selectedOption.value = locale
                            isExpanded.value = !isExpanded.value
                        },
                    )
                }
            }
        }
        val interactionSource: MutableInteractionSource = remember { MutableInteractionSource() }
        val colors = SliderDefaults.colors(
            thumbColor = MaterialTheme.colorScheme.primary,
            activeTrackColor = MaterialTheme.colorScheme.primary,
            inactiveTrackColor = MaterialTheme.colorScheme.secondaryContainer,
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.text_font_size))
            Slider(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp),
                value = sliderPosition.floatValue,
                onValueChange = { sliderPosition.floatValue = it },
                colors = colors,
                steps = 2,
                valueRange = 0.5f..2f,
                interactionSource = interactionSource,
                thumb = {
                    Box(modifier = Modifier.height(100.dp)) {
                        // Custom thumb with tooltip
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Top
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(
                                        color = MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(5.dp)
                                    )
                                    .padding(4.dp)
                            ) {
                                Text(
                                    style = MaterialTheme.typography.labelLarge,
                                    text = textScalerData.textScalerName,
                                    color = MaterialTheme.colorScheme.onPrimary,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier
                                        .padding(4.dp)
                                        .width(40.dp)
                                )
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Box(
                                modifier = Modifier
                                    .size(20.dp)
                                    .background(
                                        MaterialTheme.colorScheme.primary,
                                        shape = RoundedCornerShape(20.dp)
                                    )
                            )
                        }
                    }
                },
                track = { sliderState ->
                    Track(
                        colors = colors, sliderState = sliderState,
                        thumbTrackGapSize = 0.dp,
                        trackInsideCornerSize = 0.dp
                    )
                },
            )
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(text = stringResource(id = R.string.theme))
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
                        index = ThemeMode.system.index,
                        count = 3
                    ),
                    onClick = { selectedIndex.intValue = ThemeMode.system.index },
                    selected = ThemeMode.system.index == selectedIndex.intValue,
                    icon = {

                    },
                    label = {
                        FontIcon(
                            icon = Char(0xf5e4),
                            tint = if (selectedIndex.intValue == ThemeMode.system.index)
                                colorScheme.onPrimary
                            else colorScheme.primary,
                            contentDescription = stringResource(R.string.system_mode)
                        )
                    }
                )
                SegmentedButton(
                    colors = colors,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = ThemeMode.light.index,
                        count = 3
                    ),
                    onClick = { selectedIndex.intValue = ThemeMode.light.index },
                    selected = ThemeMode.light.index == selectedIndex.intValue,
                    icon = {

                    },
                    label = {
                        FontIcon(
                            icon = Char(0xf852),
                            tint = if (selectedIndex.intValue == ThemeMode.light.index)
                                colorScheme.onPrimary
                            else colorScheme.primary,
                            contentDescription = stringResource(R.string.light_mode)
                        )
                    }
                )
                SegmentedButton(
                    colors = colors,
                    shape = SegmentedButtonDefaults.itemShape(
                        index = ThemeMode.dark.index,
                        count = 3
                    ),
                    onClick = { selectedIndex.intValue = ThemeMode.dark.index },
                    selected = ThemeMode.dark.index == selectedIndex.intValue,
                    icon = {

                    },
                    label = {
                        FontIcon(
                            icon = Char(0xf68c),
                            tint = if (selectedIndex.intValue == ThemeMode.dark.index)
                                colorScheme.onPrimary
                            else colorScheme.primary,
                            contentDescription = stringResource(R.string.dark_mode)
                        )
                    }
                )
            }
        }
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = true,
            onCheckedChange = {
            },
            title = {
                Text(text = stringResource(id = R.string.settingsShowWordCount))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = true,
            onCheckedChange = {
            },
            title = {
                Text(text = stringResource(id = R.string.settingsShowTokenCount))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = true,
            onCheckedChange = {
            },
            title = {
                Text(text = stringResource(id = R.string.settingsShowTokenUsage))
            }
        )
        CheckboxListTile(
            modifier = Modifier.padding(top = 16.dp),
            checked = true,
            onCheckedChange = {
            },
            title = {
                Text(text = stringResource(id = R.string.settingsShowModelName))
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
    get() = platformLocale.displayName
