package com.peihua.chatbox.compose.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.peihua.chatbox.R
import com.peihua.chatbox.common.tabLayout.TabPager
import com.peihua.chatbox.common.ChatBoxTopBar
import com.peihua.chatbox.compose.navigateBack
import com.peihua.chatbox.compose.settings.tabs.AdvancedScreen
import com.peihua.chatbox.compose.settings.tabs.ChatScreen
import com.peihua.chatbox.compose.settings.tabs.DisplayScreen
import com.peihua.chatbox.compose.settings.tabs.ModelScreen

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
        SettingsTabItem(stringResource(R.string.model)) { m, state -> ModelScreen(m) },
        SettingsTabItem(stringResource(R.string.display)) { m, state -> DisplayScreen(m) },
        SettingsTabItem(stringResource(R.string.chat)) { m, state -> ChatScreen(m) },
        SettingsTabItem(stringResource(R.string.advanced)) { m, state -> AdvancedScreen(m) },
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