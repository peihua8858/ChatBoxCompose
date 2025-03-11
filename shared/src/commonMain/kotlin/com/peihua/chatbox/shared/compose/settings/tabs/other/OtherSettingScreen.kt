package com.peihua.chatbox.shared.compose.settings.tabs.other


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
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.settingsBackupAndRestore
import chatboxcompose.shared.generated.resources.settingsBackupAndRestoreBackup
import chatboxcompose.shared.generated.resources.settingsBackupAndRestoreExport
import chatboxcompose.shared.generated.resources.settingsBackupAndRestoreImport
import chatboxcompose.shared.generated.resources.settingsBackupAndRestoreRestore
import chatboxcompose.shared.generated.resources.settingsBackupChat
import chatboxcompose.shared.generated.resources.settingsKeyboardShortcuts
import chatboxcompose.shared.generated.resources.settingsProxy
import chatboxcompose.shared.generated.resources.settingsProxyHost
import chatboxcompose.shared.generated.resources.settingsProxyPassword
import chatboxcompose.shared.generated.resources.settingsProxyPort
import chatboxcompose.shared.generated.resources.settingsProxyUsername
import chatboxcompose.shared.generated.resources.settingsRestoreTips
import com.peihua.chatbox.shared.components.CheckboxListTile
import com.peihua.chatbox.shared.components.ExtendedListTile
import com.peihua.chatbox.shared.components.tabLayout.TabPager
import org.jetbrains.compose.resources.stringResource

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
                        text = stringResource(Res.string.settingsProxy),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                Column(modifier = Modifier.padding(8.dp)) {
                    OutlinedTextField(
                        value = "",
                        label = { Text(text = stringResource(Res.string.settingsProxyHost)) },
                        onValueChange = {})
                    OutlinedTextField(
                        modifier = Modifier.padding(top = 8.dp),
                        value = "",
                        label = { Text(text = stringResource(Res.string.settingsProxyPort)) },
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
                        label = { Text(text = stringResource(Res.string.settingsProxyUsername)) },
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
                        label = { Text(text = stringResource(Res.string.settingsProxyPassword)) },
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
                        text = stringResource(Res.string.settingsKeyboardShortcuts),
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
                        text = stringResource(Res.string.settingsBackupAndRestore),
                        Modifier.padding(start = 4.dp)
                    )
                }
            },
            content = {
                TabPager(
                    tabs = listOf(
                        stringResource(Res.string.settingsBackupAndRestoreBackup),
                        stringResource(Res.string.settingsBackupAndRestoreRestore),
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

            }) { Text(text = stringResource(Res.string.settingsBackupChat)) }

        Button(
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                // 导出备份数据
            }) {
            Text(text = stringResource(Res.string.settingsBackupAndRestoreExport))
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
        Text(text = stringResource(Res.string.settingsRestoreTips))
        Button(
            modifier = Modifier.padding(top = 8.dp),
            shape = MaterialTheme.shapes.small,
            onClick = {
                // 导入备份数据
            }) {
            Text(text = stringResource(Res.string.settingsBackupAndRestoreImport))
        }
    }
}