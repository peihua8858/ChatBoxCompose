package com.peihua.chatbox.shared.compose.settings

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.chat
import chatboxcompose.shared.generated.resources.display
import chatboxcompose.shared.generated.resources.model
import chatboxcompose.shared.generated.resources.other
import chatboxcompose.shared.generated.resources.settings
import com.peihua.chatbox.shared.components.ChatBoxTopBar
import com.peihua.chatbox.shared.components.tabLayout.TabPager
import com.peihua.chatbox.shared.compose.navigateBack
import com.peihua.chatbox.shared.compose.settings.tabs.chat.ChatSettingsScreen
import com.peihua.chatbox.shared.compose.settings.tabs.display.DisplaySettingsScreen
import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelSettingsScreen
import com.peihua.chatbox.shared.compose.settings.tabs.other.OtherSettingScreen
import org.jetbrains.compose.resources.stringResource

@Composable
fun SettingsScreen() {
    Scaffold(
        modifier = Modifier
            .fillMaxSize(), topBar = {
            ChatBoxTopBar(
                navigateUp = { navigateBack() },
                title = { stringResource(Res.string.settings) })
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
        SettingsTabItem(stringResource(Res.string.model)) { m, state -> ModelSettingsScreen(m) },
        SettingsTabItem(stringResource(Res.string.display)) { m, state -> DisplaySettingsScreen(m) },
        SettingsTabItem(stringResource(Res.string.chat)) { m, state -> ChatSettingsScreen(m) },
        SettingsTabItem(stringResource(Res.string.other)) { m, state -> OtherSettingScreen(m) },
    )
    val pagerState = rememberPagerState(
        initialPage = 0,
        initialPageOffsetFraction = 0f
    ) { mTabs.size }
    TabPager(modifier = modifier, tabs = mTabs, pagerState = pagerState) { m, state, index ->
        mTabs[index].content(m.padding(32.dp), state)
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