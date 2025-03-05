package com.peihua.chatbox.compose.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DismissibleDrawerSheet
import androidx.compose.material3.DismissibleNavigationDrawer
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.ScaffoldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.dimensionResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import com.peihua.chatbox.R
import com.peihua.chatbox.compose.ChatBoxTopBar
import com.peihua.chatbox.compose.message.MessageScreen
import com.peihua.chatbox.compose.NavigationIcon
import com.peihua.chatbox.compose.ScreenRouter
import kotlinx.coroutines.launch

@Composable
fun NavHostController.HomeScreen(modifier: Modifier = Modifier) {
    val drawerState: DrawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val title = remember { mutableStateOf("Home") }
    val drawerController = rememberNavController()
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
                ChatBoxDrawerItem(item = DrawerItem("HomeMessage","Home", isSelected = true), onClick = {
                    scope.launch {
                        drawerState.close()
                        title.value = it.title
                        drawerController.navigate(ScreenRouter.Message(it.menuId).route)
                    }
                })
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.dp_16))
                )
                ChatBoxDrawerItem(item = DrawerItem("SecondMessage","Message", isSelected = true), onClick = {
                    scope.launch {
                        drawerState.close()
                        title.value = it.title
                        drawerController.navigate(ScreenRouter.Message(it.menuId).route)
                    }
                })
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(dimensionResource(id = R.dimen.dp_16))
                )
                ChatBoxDrawerItem(item = DrawerItem("ThirdMessage","Setting", isSelected = true), onClick = {
                    scope.launch {
                        navigate(ScreenRouter.Settings.route)
                        drawerState.close()
//                        title.value = it.title
//                        drawerController.navigate(ScreenRouter.Message(it.menuId).route)
                    }
                })
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
fun ChatBoxDrawerItem(item: DrawerItem, onClick: (DrawerItem) -> Unit) {
    NavigationDrawerItem(
        label = { Text(item.title) },
        selected = item.isSelected,
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
    val isSelected: Boolean = false
)