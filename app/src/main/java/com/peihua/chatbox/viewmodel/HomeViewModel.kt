package com.peihua.chatbox.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.peihua.chatbox.compose.home.DrawerItem
import com.peihua.chatbox.utils.ResultData
import com.peihua.chatbox.utils.dLog
import com.peihua.chatbox.utils.request
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {
    val homeLiveData = mutableStateOf<ResultData<List<DrawerItem>>>(ResultData.Starting())
    init {
        requestMenus()
    }
    fun requestMenus() {
        dLog { ">>>>>>requestMenus" }
        request(homeLiveData) {
            dLog { ">>>>>>requestMenus111" }
            delay(3000)
            dLog { ">>>>>>requestMenus222" }
            val menuItems = listOf(
                DrawerItem("Message", "New Chat", isDefault = true),
                DrawerItem("Message1", "New Chat1", isDefault = false),
                DrawerItem("Message2", "New Chat2", isDefault = false),
                DrawerItem("Message3", "New Chat3", isDefault = false),
                DrawerItem("Message4", "New Chat4", isDefault = false)
            )
            menuItems
        }
    }
}