package com.peihua.chatbox.compose.settings.tabs

import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.intl.Locale
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import com.peihua.chatbox.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DisplayScreen(modifier: Modifier = Modifier) {
    val languageCodes = stringArrayResource(id = R.array.language_list_value)
    val localeList = languageCodes.map { Locale(it) }
    val isExpanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(Locale.current) }
    var sliderPosition = remember { mutableFloatStateOf(1f) }
    Column(modifier = modifier.fillMaxSize()) {
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
                            .background(if (selected) Color.Blue else Color.Transparent),
                        text = {
                            Text(
                                text = locale.displayName,
                                color = if (selected) Color.White else Color.Black,
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
        Text(text = stringResource(id = R.string.text_font_size))
        Slider(
            value = sliderPosition.floatValue,
            onValueChange = { sliderPosition.floatValue = it },
            colors = colors,
            steps = 3,
            valueRange = 0f..50f,
            interactionSource = interactionSource,
            thumb = {
                SliderDefaults.Thumb(
                    interactionSource = interactionSource,
                    colors = colors,
                    enabled = true,
                    thumbSize = DpSize(20.dp, 20.dp)
                )
            },
            track = { sliderState ->
                SliderDefaults.Track(colors = colors, enabled = true, sliderState = sliderState)
            },
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