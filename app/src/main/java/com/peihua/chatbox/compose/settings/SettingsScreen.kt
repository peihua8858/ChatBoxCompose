package com.peihua.chatbox.compose.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.peihua.chatbox.R
import com.peihua.chatbox.common.tabLayout.TabPager
import com.peihua.chatbox.common.ChatBoxTopBar
import com.peihua.chatbox.compose.navigateBack
import com.peihua.chatbox.compose.settings.tabs.other.OtherSettingScreen
import com.peihua.chatbox.compose.settings.tabs.chat.ChatSettingsScreen
import com.peihua.chatbox.compose.settings.tabs.display.DisplaySettingsScreen
import com.peihua.chatbox.compose.settings.tabs.model.ModelSettingsScreen
import com.peihua.chatbox.utils.DLog
@Composable
fun SettingsScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(), topBar = {
            ChatBoxTopBar(
                navigateUp = { navigateBack() },
                title = { stringResource(id = R.string.settings) })
        }) {
        Box(
            modifier = Modifier
                .padding(it)
                .fillMaxSize()
        ) {
            SettingsContent()
        }
    }
}

@Composable
fun SettingsContent(modifier: Modifier = Modifier) {
    val mTabs = listOf(
        SettingsTabItem(stringResource(R.string.model)) { m, state -> ModelSettingsScreen(m) },
        SettingsTabItem(stringResource(R.string.display)) { m, state -> DisplaySettingsScreen(m) },
        SettingsTabItem(stringResource(R.string.chat)) { m, state -> ChatSettingsScreen(m) },
        SettingsTabItem(stringResource(R.string.other)) { m, state -> OtherSettingScreen(m) },
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { mTabs.size }
    TabPager(modifier = modifier, tabs = mTabs, pagerState = pagerState) { m, state, index ->
        mTabs[index].content(m.padding(dimensionResource(id = R.dimen.dp_32)), state)
    }
}

data class SettingsTabItem(
    val title: String,
    val content: @Composable (Modifier, PagerState) -> Unit
) {
    override fun toString(): String {
        return title
    }
}