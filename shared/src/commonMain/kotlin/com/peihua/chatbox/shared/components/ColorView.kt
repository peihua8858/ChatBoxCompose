package com.peihua.chatbox.shared.components

import androidx.compose.foundation.gestures.Orientation
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp


@Composable
fun ColorView(
    modifier: Modifier = Modifier,
    orientation: Orientation = Orientation.Horizontal,
    thickness: Dp = 50.dp,
    color: Color,
) {
    if (orientation == Orientation.Vertical) {
        VerticalDivider(
            modifier = modifier,
            color = color,
            thickness = thickness,
        )
    } else {
        HorizontalDivider(
            modifier = modifier,
            color = color,
            thickness = thickness,
        )
    }
}