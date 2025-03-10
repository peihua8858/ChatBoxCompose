package com.peihua.chatbox.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Switch
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SwitchListTile(
    modifier: Modifier = Modifier,
    checked: Boolean,
    onCheckedChange: ((Boolean) -> Unit),
    verticalAlignment: Alignment.Vertical = Alignment.CenterVertically,
    horizontalArrangement: Arrangement.Horizontal = Arrangement.Start,
    controlAffinity: ListTileControlAffinity = ListTileControlAffinity.leading,
    title: @Composable () -> Unit
) {
    Row(
        modifier = modifier,
        verticalAlignment = verticalAlignment,
        horizontalArrangement = horizontalArrangement,
    ) {
        if (controlAffinity == ListTileControlAffinity.leading) {
            Switch(checked = checked, onCheckedChange = onCheckedChange)
            title()
        } else {
            title()
            Switch(checked = checked, onCheckedChange = onCheckedChange)
        }
    }
}