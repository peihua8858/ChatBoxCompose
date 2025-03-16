package com.peihua.chatbox.shared.repository

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.peihua.chatbox.shared.compose.home.DrawerItem
import com.peihua.chatbox.shared.http.HttpClient
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.dLog
import com.peihua.chatbox.shared.utils.request
import io.ktor.client.call.body
import io.ktor.client.request.post
import kotlinx.coroutines.delay

class HomeViewModel : ViewModel() {
    val homeLiveData = mutableStateOf<ResultData<List<DrawerItem>>>(ResultData.Starting())
    val httpClient by lazy { HttpClient { } }

    init {
        requestMenus()
    }

    fun requestMenus() {
        request(homeLiveData) {
            delay(1000)
           val response= httpClient.post("https://www.baidu.com")
          val result=  response.body<String>()
            dLog { "result>>>$result" }
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