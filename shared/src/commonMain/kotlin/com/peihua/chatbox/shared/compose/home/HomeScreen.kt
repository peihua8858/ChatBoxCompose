package com.peihua.chatbox.shared.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.savedstate.read
import chatboxcompose.shared.generated.resources.Res
import chatboxcompose.shared.generated.resources.about
import chatboxcompose.shared.generated.resources.logo
import chatboxcompose.shared.generated.resources.settings
import coil3.compose.AsyncImage
import com.peihua.chatbox.shared.components.ChatBoxTopBar
import com.peihua.chatbox.shared.components.NavigationIcon
import com.peihua.chatbox.shared.compose.ScreenRouter
import com.peihua.chatbox.shared.compose.message.MessageScreen
import com.peihua.chatbox.shared.compose.navigateTo
import com.peihua.chatbox.shared.repository.HomeViewModel
import com.peihua.chatbox.shared.utils.ResultData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(HomeViewModel::class),
) {
    val resultData = viewModel.homeLiveData
    when (resultData.value) {
        is ResultData.Starting -> {
            Box(modifier = Modifier.fillMaxSize()) {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }
        }

        is ResultData.Success -> {
            val menuItems = (resultData.value as ResultData.Success<List<DrawerItem>>).data
            var selectedIndex = 0
            menuItems.forEachIndexed { index, item ->
                selectedIndex = if (item.isDefault) index else selectedIndex
            }
            NavigationDrawer(
                modifier = modifier,
                menuItems = menuItems,
                defaultSelectIndex = selectedIndex
            )
        }

        else -> {
            Box(modifier = Modifier.fillMaxSize()) {
                Row(
                    Modifier
                        .align(Alignment.Center)
                ) {
                    Text(
                        text = "请求失败,",
                        style = typography.titleMedium,
                    )
                    //text 下划线
                    Text(
                        text = "请点击重试",
                        style = typography.titleMedium,
                        color = Color.Blue,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier
                            .clickable {
                                viewModel.requestMenus()
                            })
                }
            }
        }
    }
}

@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    menuItems: List<DrawerItem>,
    defaultSelectIndex: Int,
) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerController = rememberNavController()
    val scope = rememberCoroutineScope()
    val title = remember { mutableStateOf("New Chat") }
    val selectedIndex = remember { mutableStateOf(defaultSelectIndex) }
    DismissibleNavigationDrawer(
        modifier = modifier
            .windowInsetsPadding(ScaffoldDefaults.contentWindowInsets)
            .background(
                color = Color.White,
                shape = RoundedCornerShape(8.dp)
            ),
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = 16.dp,
                            bottom = 16.dp,
                        )
                ) {
                    Row(
                        modifier = Modifier
                            .padding(
                                start = 16.dp,
                                end = 16.dp
                            )
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(shape = RoundedCornerShape(8.dp)),
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = ""
                        )
                        Text(
                            text = "ChatBox", modifier = Modifier
                                .padding(start = 8.dp)
                        )
                    }
                    HorizontalDivider(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 16.dp)
                    )
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                    ) {
                        items(menuItems.size) { index ->
                            ChatBoxDrawerItem(
                                item = menuItems[index],
                                isSelected = selectedIndex.value == index,
                                onClick = { item ->
                                    scope.launch {
                                        title.value = item.title
                                        selectedIndex.value = index
                                        drawerController.navigate(ScreenRouter.Message(item.menuId).route)
                                        drawerState.close()
                                    }
                                })
                            if (index < menuItems.size - 1) {
                                VerticalDivider(
                                    modifier = Modifier.padding(
                                        top = 16.dp,
                                    )
                                )
                            }
                        }
                    }
                    Column(
                        modifier = Modifier
                            .wrapContentHeight()
                    ) {
                        HorizontalDivider(
                            modifier = Modifier
                                .padding(top = 16.dp, bottom = 16.dp)
                        )
                        ChatBoxDrawerItem(
                            item = DrawerItem("",
                                stringResource(Res.string.settings),
                            ), onClick = {
                                scope.launch {
                                    navigateTo(ScreenRouter.Settings.route)
                                    drawerState.close()
                                }
                            })
                        ChatBoxDrawerItem(
                            item = DrawerItem("",
                                stringResource(Res.string.about),
                            ), onClick = {
                                scope.launch {
                                    navigateTo(ScreenRouter.About.route)
                                    drawerState.close()
                                }
                            })
                    }

                }
            }
        }) {
        Scaffold(topBar = {
            ChatBoxTopBar(
                navigationIcon = {
                    NavigationIcon(imageVector = Icons.Default.Menu) {
                        scope.launch {
                            if (drawerState.isOpen) {
                                drawerState.close()
                            } else {
                                drawerState.open()
                            }
                        }
                    }
                },
                title = {
                    title.value
                },
            )
        }) {
            Box(
                modifier = Modifier
                    .padding(it)
                    .fillMaxSize()
            ) {
                NavigationSetup(drawerController = drawerController)
            }
        }
    }
}

@Composable
fun ChatBoxDrawerItem(
    modifier: Modifier = Modifier,
    item: DrawerItem,
    isSelected: Boolean = false,
    onClick: (DrawerItem) -> Unit,
) {
    NavigationDrawerItem(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp
        ),
        label = { Text(item.title) },
        selected = isSelected,
        icon = {
            item.icon?.let {
                AsyncImage(model = it, contentDescription = "")
            }
        },
        shape = RoundedCornerShape(8.dp),
        onClick = {
            onClick(item)
        }
    )
}

@Composable
fun NavigationSetup(drawerController: NavHostController) {
    NavHost(navController = drawerController, startDestination = "message/New Chat") {
        // 使用 navArgument 接受参数
        composable("message/{menuId}") { backStackEntry ->
//            val menuId = backStackEntry.arguments?.getString("menuId") ?: "New Chat"
            val menuId = backStackEntry.arguments?.read<String> {
                getStringOrElse("menuId") { "New Chat" }
            } ?: "New Chat"
            MessageScreen(menuId)
        }
    }
}

data class DrawerItem(
    val menuId: String,
    val title: String,
    val icon: String? = null,
    val isDefault: Boolean = false,
)