package com.peihua.chatbox.shared.compose.settings.tabs.model


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuAnchorType
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.settingsModelProvider
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModelSettingsScreen(modifier: Modifier = Modifier) {
    val colorScheme = MaterialTheme.colorScheme
    val isExpanded = remember { mutableStateOf(false) }
    val selectedOption = remember { mutableStateOf(ModelProvider.OPenAI) }
    val models = Chat_Models
    val modelProvider = selectedOption.value
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
                value = selectedOption.value.name,
                onValueChange = {
                },
                label = { Text(stringResource(Res.string.settingsModelProvider)) },
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
                models.forEach { item ->
                    val selected = selectedOption.value == item
                    DropdownMenuItem(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(if (selected) colorScheme.secondaryContainer else Color.Transparent),
                        text = {
                            Text(
                                text = item.name,
                                color = if (selected) colorScheme.onSecondaryContainer else colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelMedium
                            )
                        },
                        onClick = {
                            selectedOption.value = item
                            isExpanded.value = !isExpanded.value
                        },
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        modelProvider.contentView(Modifier, selectedOption.value.model, { selectedOption.value.model = it })
    }
}