package com.peihua.chatbox.shared.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.RadioButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun RadioButtonListTitle(
    modifier: Modifier = Modifier,
    selected: Boolean,
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
            RadioButton(selected = selected, onClick = {
                onCheckedChange(selected.not())
            })
            title()
        } else {
            title()
            RadioButton(selected = selected, onClick = {
                onCheckedChange(selected.not())
            })
        }
    }
}