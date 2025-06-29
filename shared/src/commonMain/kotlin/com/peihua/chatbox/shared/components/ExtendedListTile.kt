package com.peihua.chatbox.shared.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp


@Composable
fun ExtendedListTile(
    modifier: Modifier = Modifier,
    isExtended: Boolean,
    durationMillis: Int = 800,
    title: @Composable (Boolean) -> Unit,
    content: @Composable () -> Unit
) {
    val isExtended = remember { mutableStateOf(isExtended) }
    Column(
        modifier = modifier
            .border(
                1.dp,
                MaterialTheme.colorScheme.primary,
                RoundedCornerShape(8.dp)
            )
            .clip(RoundedCornerShape(8.dp))
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable {
                    isExtended.value = !isExtended.value
                }) {
            title(isExtended.value)
        }
        AnimatedVisibility(
            visible = isExtended.value,
            enter =expandVertically(),
            exit = shrinkVertically(
                animationSpec = tween(durationMillis = durationMillis)
            ),
            // 关键：动画结束后才真正移除内容，加上这句会内容先消失，再折叠
//            modifier = Modifier.animateContentSize()
        ) {
            content()
        }
    }
}

