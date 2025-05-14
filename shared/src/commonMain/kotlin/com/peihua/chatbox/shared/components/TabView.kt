package com.peihua.chatbox.shared.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peihua.chatbox.shared.components.text.ScaleText


@Composable
fun <T> TabView(
    modifier: Modifier,
    item: T,
    index: Int,
    isSelected: Boolean,
    onTabClick: (Int) -> Unit
) {
    val colorScheme= MaterialTheme.colorScheme
    Tab(
        modifier = modifier
            .heightIn(min = 48.dp)
            .background(Color.Transparent),
        selected = isSelected,
        selectedContentColor = colorScheme.primary,
        unselectedContentColor = colorScheme.onSecondaryContainer,
        onClick = {
            onTabClick(index)
        }) {
        ScaleText(
            item.toString(),
            style = MaterialTheme.typography.titleMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .wrapContentWidth(Alignment.CenterHorizontally)
                .padding(horizontal = 16.dp),
        )
    }
}