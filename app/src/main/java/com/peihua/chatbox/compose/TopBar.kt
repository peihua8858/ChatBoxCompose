package com.peihua.chatbox.compose

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.peihua.chatbox.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatBoxTopBar(
    modifier: Modifier = Modifier, title: () -> String,
    navigateUp: () -> Unit = {},
    navigationIcon: @Composable () -> Unit = {
        NavigationIcon(navigateUp = navigateUp)
    }, actions: @Composable RowScope.() -> Unit = {}
) {
    val typography = MaterialTheme.typography
    TopAppBar(
        title = {
            Box(modifier = Modifier.fillMaxWidth()) {
                Text(
                    style = typography.titleMedium,
                    text = title(),
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .wrapContentWidth(Alignment.CenterHorizontally) // 水平居中
                        .align(Alignment.Center),
                    fontSize = dimensionResource(id = R.dimen.sp_20).value.sp,
                )
            }
        },
        navigationIcon = navigationIcon,
        actions = actions,
        modifier = modifier
    )
}

/**
 * 导航图标
 */
@Composable
fun NavigationIcon(
    imageVector: ImageVector = Icons.AutoMirrored.Filled.ArrowBack,
    navigateUp: () -> Unit = {}
) {
    Image(
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .size(dimensionResource(id = R.dimen.dp_32))
            .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8)))
            .clickable { navigateUp() },
        imageVector = imageVector,
        contentDescription = ""
    )
}