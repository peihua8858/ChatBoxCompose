package com.peihua.chatbox.compose.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.peihua.chatbox.common.tabLayout.TabPager
import com.peihua.chatbox.compose.ChatBoxTopBar
import com.peihua.chatbox.compose.settings.tabs.AdvancedScreen
import com.peihua.chatbox.compose.settings.tabs.ChatScreen
import com.peihua.chatbox.compose.settings.tabs.DisplayScreen
import com.peihua.chatbox.compose.settings.tabs.ModelScreen

@Composable
fun NavHostController.SettingsScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(), topBar = {
            ChatBoxTopBar(navigateUp = { navigateUp() }, title = { "设置" })
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
        SettingsTabItem("模型") { m, state -> ModelScreen(m) },
        SettingsTabItem("显示") { m, state -> DisplayScreen(m) },
        SettingsTabItem("聊天") { m, state -> ChatScreen(m) },
        SettingsTabItem("高级") { m, state -> AdvancedScreen(m) },
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { mTabs.size }
    TabPager(modifier = modifier, tabs = mTabs, pagerState = pagerState) { m, state, index ->
        mTabs[index].content(m, state)
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