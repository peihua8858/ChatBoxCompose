package com.peihua.chatbox.compose.settings.tabs.other


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peihua.chatbox.R
import com.peihua.chatbox.common.CheckboxListTile
import com.peihua.chatbox.common.ExtendedListTile
import com.peihua.chatbox.common.tabLayout.TabPager

@Composable
fun OtherSettingScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
    ) {
        ExtendedListTile(
            modifier = Modifier,
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
                        text = stringResource(id = R.string.settingsProxy),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = "",
                        label = { Text(text = stringResource(R.string.settingsProxyHost)) },
                        onValueChange = {})
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        value = "",
                        label = { Text(text = stringResource(id = R.string.settingsProxyPort)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = ""
                            )
                        },
                        onValueChange = {})
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        value = "",
                        label = { Text(text = stringResource(R.string.settingsProxyUsername)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = ""
                            )
                        },
                        onValueChange = {})
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        value = "",
                        label = { Text(text = stringResource(R.string.settingsProxyPassword)) },
                        trailingIcon = {
                            Icon(
                                imageVector = Icons.Outlined.Clear,
                                contentDescription = ""
                            )
                        },
                        onValueChange = {})
                }
            }
        )
        ExtendedListTile(
            modifier = Modifier.padding(top = 16.dp),
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
                        text = stringResource(id = R.string.settingsKeyboardShortcuts),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                Column(modifier = Modifier.padding(8.dp)) {
                    Text(text = "无")
                }
            }
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
                        text = stringResource(id = R.string.settingsBackupAndRestore),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                TabPager(
                    tabs = listOf(
                        stringResource(R.string.settingsBackupAndRestoreBackup),
                        stringResource(R.string.settingsBackupAndRestoreRestore),
                    )
                ) { m, state, index ->
                    when (index) {
                        0 -> BackupView(m)
                        1 -> RestoreView(m)
                    }
                }
            })
    }
}

@Composable
fun BackupView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        CheckboxListTile(checked = true, onCheckedChange = {

        }) {
            Text(text = "Settings")
        }
        CheckboxListTile(
            modifier = Modifier.padding(top = 8.dp),
            checked = true,
            onCheckedChange = {

            }) {
            Text(text = "Api key")
        }
        CheckboxListTile(
            modifier = Modifier.padding(top = 8.dp),
            checked = true,
            onCheckedChange = {

            }) { Text(text = stringResource(R.string.settingsBackupChat)) }

        Button(
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                // 导出备份数据
            }) {
            Text(text = stringResource(R.string.settingsBackupAndRestoreExport))
        }
    }
}

@Composable
fun RestoreView(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .padding(8.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(text = stringResource(R.string.settingsRestoreTips))
        Button(
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                // 导入备份数据
            }) {
            Text(text = stringResource(R.string.settingsBackupAndRestoreImport))
        }
    }
}