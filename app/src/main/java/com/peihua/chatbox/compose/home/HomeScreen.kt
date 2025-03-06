package com.peihua.chatbox.compose.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.peihua.chatbox.R
import com.peihua.chatbox.compose.ChatBoxTopBar
import com.peihua.chatbox.compose.NavigationIcon
import com.peihua.chatbox.compose.ScreenRouter
import com.peihua.chatbox.compose.message.MessageScreen
import com.peihua.chatbox.utils.ResultData
import com.peihua.chatbox.viewmodel.HomeViewModel
import kotlinx.coroutines.launch

@Composable
fun NavHostController.HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(HomeViewModel::class.java)
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
                Text(text = "请求失败", modifier = Modifier.align(Alignment.Center))
            }
        }
    }
}

@Composable
fun NavHostController.NavigationDrawer(
    modifier: Modifier = Modifier,
    menuItems: List<DrawerItem>,
    defaultSelectIndex: Int,
) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val drawerController = rememberNavController()
    val scope = rememberCoroutineScope()
    val title = remember { mutableStateOf("Home") }
    val selectedIndex = remember { mutableStateOf(defaultSelectIndex) }
    DismissibleNavigationDrawer(
        modifier = modifier
            .windowInsetsPadding(ScaffoldDefaults.contentWindowInsets)
            .background(
                color = colorResource(id = R.color.white),
                shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8))
            ),
        drawerState = drawerState,
        drawerContent = {
            DismissibleDrawerSheet {
                ConstraintLayout(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(
                            top = dimensionResource(id = R.dimen.dp_16),
                            bottom = dimensionResource(id = R.dimen.dp_16),
                        )
                ) {
                    val (header, line1, menuList, line2, bottomItem) = createRefs()
                    ConstraintLayout(
                        modifier = Modifier
                            .constrainAs(header) {
                                top.linkTo(parent.top)
                            }
                            .padding(
                                start = dimensionResource(id = R.dimen.dp_16),
                                end = dimensionResource(id = R.dimen.dp_16)
                            )
                            .fillMaxWidth()
                    ) {
                        val (logo, text) = createRefs()
                        Image(contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .constrainAs(logo) {
                                    top.linkTo(parent.top)
                                    start.linkTo(parent.start)
                                }
                                .size(dimensionResource(id = R.dimen.dp_32))
                                .clip(shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8))),
                            painter = painterResource(id = R.mipmap.logo),
                            contentDescription = "")
                        Text(text = "ChatBox", modifier = Modifier
                            .constrainAs(text) {
                                top.linkTo(logo.top)
                                bottom.linkTo(logo.bottom)
                                start.linkTo(logo.end)
                            }
                            .padding(start = 8.dp)
                        )
                    }
                    HorizontalDivider(modifier = Modifier
                        .constrainAs(line1) {
                            top.linkTo(header.bottom)
                        }
                        .padding(top = 16.dp, bottom = 16.dp))
                    LazyColumn(modifier = Modifier
                        .constrainAs(menuList) {
                            top.linkTo(line1.bottom)
                            bottom.linkTo(line2.top)
                            height = Dimension.fillToConstraints
                        }) {
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
                    HorizontalDivider(modifier = Modifier
                        .constrainAs(line2) {
                            bottom.linkTo(bottomItem.top)
                        }
                        .padding(top = 16.dp, bottom = 16.dp))
                    Column(modifier = Modifier
                        .wrapContentHeight()
                        .constrainAs(bottomItem) {
                            bottom.linkTo(parent.bottom)
                        }) {
                        ChatBoxDrawerItem(
                            item = DrawerItem(
                                "",
                                "Setting",
                            ), onClick = {
                                scope.launch {
                                    navigate(ScreenRouter.Settings.route)
                                    drawerState.close()
                                }
                            })
                        ChatBoxDrawerItem(
                            item = DrawerItem(
                                "",
                                "About",
                            ), onClick = {
                                scope.launch {
                                    navigate(ScreenRouter.Settings.route)
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
    onClick: (DrawerItem) -> Unit
) {
    NavigationDrawerItem(
        modifier = modifier.padding(
            start = dimensionResource(id = R.dimen.dp_16),
            end = dimensionResource(id = R.dimen.dp_16)
        ),
        label = { Text(item.title) },
        selected = isSelected,
        icon = {
            item.icon?.let {
                AsyncImage(model = it, contentDescription = "")
            }
        },
        shape = RoundedCornerShape(dimensionResource(id = R.dimen.dp_8)),
        onClick = {
            onClick(item)
        }
    )
}

@Composable
fun NavHostController.NavigationSetup(drawerController: NavHostController) {
    NavHost(navController = drawerController, startDestination = "message/New Chat") {
        // 使用 navArgument 接受参数
        composable("message/{menuId}") { backStackEntry ->
            val menuId = backStackEntry.arguments?.getString("menuId") ?: "New Chat"
            MessageScreen(menuId)
        }
    }
}

data class DrawerItem(
    val menuId: String,
    val title: String,
    val icon: String? = null,
    val isDefault: Boolean = false
)