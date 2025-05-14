package com.peihua.chatbox.shared.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
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
import com.peihua.chatbox.shared.components.stateView.ErrorView
import com.peihua.chatbox.shared.components.stateView.LoadingView
import com.peihua.chatbox.shared.components.text.ScaleText
import com.peihua.chatbox.shared.compose.ScreenRouter
import com.peihua.chatbox.shared.compose.message.MessageScreen
import com.peihua.chatbox.shared.compose.navigateTo
import com.peihua.chatbox.shared.data.db.Menu
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.viewmodel.HomeViewModel
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource

/**
 * 主屏幕组件
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(HomeViewModel::class),
) {
    val menusState = viewModel.menusState
    val resultData = menusState.value
    val requestMenus = {
        viewModel.requestMenus()
    }
    when (resultData) {
        is ResultData.Initialize -> requestMenus()
        is ResultData.Starting -> {
            LoadingView()
        }

        is ResultData.Success -> {
            val menuItems = resultData.data
            var selectedIndex = 0
            for ((index, item) in menuItems.withIndex()) {
                if (item.isSelected) {
                    selectedIndex = index
                    break
                }
            }
            NavigationDrawer(
                modifier = modifier,
                menuItems = menuItems,
                onEditName = { menuId, menuName ->
                    viewModel.updateMenuName(menuId, menuName, menuItems)
                },
                defaultSelectIndex = selectedIndex
            )
        }

        else -> {
            ErrorView {
                requestMenus()
            }
        }
    }
}

/**
 * 导航抽屉组件
 */
@Composable
fun NavigationDrawer(
    modifier: Modifier = Modifier,
    menuItems: List<Menu>,
    defaultSelectIndex: Int,
    onEditName: (Long, String) -> Unit,
) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerController = rememberNavController()
    val scope = rememberCoroutineScope()
    val firstMenu = menuItems[0]
    val title = remember { mutableStateOf(firstMenu.menu_name) }
    val selectedIndex = remember { mutableStateOf(defaultSelectIndex) }

    DismissibleNavigationDrawer(
        modifier = modifier
            .windowInsetsPadding(ScaffoldDefaults.contentWindowInsets)
            .background(Color.White, shape = RoundedCornerShape(8.dp)),
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 16.dp, bottom = 16.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(start = 16.dp, end = 16.dp)
                            .fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Start
                    ) {
                        Image(
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            painter = painterResource(Res.drawable.logo),
                            contentDescription = ""
                        )

                        ScaleText(
                            text = "ChatBox",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

                    LazyColumn(modifier = Modifier.weight(1f)) {
                        items(count = menuItems.size) { index ->
                            ChatBoxDrawerItem(
                                item = menuItems[index],
                                isSelected = selectedIndex.value == index,
                                updateName = { menuId, menuName ->
                                    onEditName(menuId, menuName)
                                    title.value = menuName
                                },
                                onClick = { item ->
                                    scope.launch {
                                        title.value = item.menu_name
                                        selectedIndex.value = index
                                        val messageRoute = ScreenRouter.Message(item._id)
                                        drawerController.navigate(messageRoute.route)
                                        drawerState.close()
                                    }
                                }
                            )

                            if (index < menuItems.size - 1) {
                                VerticalDivider(modifier = Modifier.padding(top = 16.dp))
                            }
                        }
                    }

                    Column(modifier = Modifier.wrapContentHeight()) {
                        HorizontalDivider(modifier = Modifier.padding(top = 16.dp, bottom = 16.dp))

                        ChatBoxDrawerItem(
                            item = Menu(stringResource(Res.string.settings)),
                            onClick = {
                                scope.launch {
                                    navigateTo(ScreenRouter.Settings.route)
                                    drawerState.close()
                                }
                            })
                        ChatBoxDrawerItem(
                            item = Menu(stringResource(Res.string.about)),
                            onClick = {
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
                NavigationSetup(drawerController = drawerController, firstMenu)
            }
        }
    }
}

/**
 * 聊天盒抽屉项组件
 */
@Composable
fun ChatBoxDrawerItem(
    modifier: Modifier = Modifier,
    item: Menu,
    isSelected: Boolean = false,
    updateName: (Long, String) -> Unit = { _, _ -> },
    onClick: (Menu) -> Unit,
) {
    val editName = remember { mutableStateOf(item.menu_name) }
    val isEditName = remember { mutableStateOf(false) }
    NavigationDrawerItem(
        modifier = modifier.padding(
            start = 16.dp,
            end = 16.dp
        ),
        label = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                if (isEditName.value) {
                    OutlinedTextField(value = editName.value, onValueChange = {
                        editName.value = it
                    }, modifier = Modifier.weight(1f))
                } else {
                    ScaleText(editName.value, modifier = Modifier.weight(1f))
                }
                if (isSelected) {
                    IconButton(onClick = {
                        isEditName.value = !isEditName.value
                        if (!isEditName.value) {
                            updateName(item._id, editName.value)
                        }
                    }, modifier = Modifier.padding(start = 8.dp).wrapContentWidth()) {
                        Icon(
                            imageVector = if (isEditName.value) Icons.Default.Done else Icons.Default.Edit,
                            contentDescription = "Avatar",
                            modifier = Modifier
                                .size(24.dp) // 头像大小
                                .clip(CircleShape) // 圆形裁剪
                                .background(Color.Transparent), // 默认背景色
                        )
                    }
                }
            }
        },
        selected = isSelected,
        icon = {
            item.icon?.let {
                AsyncImage(model = it, contentDescription = "")
            }
        },
        shape = RoundedCornerShape(8.dp),
        onClick = { onClick(item) }
    )
}

/**
 * 设置导航配置
 */
@Composable
fun NavigationSetup(drawerController: NavHostController, menu: Menu) {
    NavHost(navController = drawerController, startDestination = "message/${menu._id}") {
        // 使用 navArgument 接受参数
        composable(route = "message/{menuId}") { backStackEntry ->
            val menuId = backStackEntry.arguments?.read {
                getString("menuId").toLong()
            } ?: 0L
            MessageScreen(menuId)
        }
    }
}

val Menu.isSelected: Boolean
    get() = this.isDefault == 1L