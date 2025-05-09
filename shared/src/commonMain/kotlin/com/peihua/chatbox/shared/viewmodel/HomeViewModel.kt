package com.peihua.chatbox.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.peihua.chatbox.shared.currentTimeMillis
import com.peihua.chatbox.shared.data.db.AppDatabase
import com.peihua.chatbox.shared.data.db.DatabaseHelper
import com.peihua.chatbox.shared.data.db.Menu
import com.peihua.chatbox.shared.data.db.MenuQueries
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext
import com.peihua.chatbox.shared.utils.set

class HomeViewModel(
    val database: AppDatabase = DatabaseHelper.database,
    val menuQueries: MenuQueries = database.menuQueries,
) : ViewModel() {
    val menusState = mutableStateOf<ResultData<List<Menu>>>(ResultData.Initialize())
    fun requestMenus() {
        request(menusState) {
            val result = selectAllMenus()
            result.ifEmpty {
                val menuItems = listOf(
                    Menu("New Chat"),
                    Menu("New Chat1"),
                    Menu("New Chat2"),
                    Menu("New Chat3"),
                    Menu("New Chat4")
                )
                insertMenus(menuItems)
                menuItems
            }
        }
    }

    fun insertMenus(menus: List<Menu>) {
        for (menu in menus) {
            insertMenu(menu)
        }
    }

    fun insertMenu(menu: Menu) {
        request(menusState) {
            insetMenu(menu)
            selectAllMenus()

        }
    }

    fun updateMenuName(menuId: Long, name: String, menuItems: List<Menu>) {
        request {
            menuQueries.updateMenuById(name, currentTimeMillis(), menuId)
            if (menuItems.isNotEmpty()) {
                for ((index, item) in menuItems.withIndex()) {
                    if (item._id == menuId) {
                        menuItems[index] = Menu(name)
                        break
                    }
                }
            }
        }
    }

    suspend fun insetMenu(menu: Menu) {
        return withContext(Dispatchers.IO) {
            menuQueries.insertMenu(
                menu.menu_name,
                menu.isDefault,
                menu.icon,
                menu.create_time,
                menu.update_time
            )
        }
    }

    suspend fun selectAllMenus(): List<Menu> {
        return withContext(Dispatchers.IO) {
            val result = menuQueries.selectAllMenusSortedByUpdateTime()
            return@withContext result.executeAsList()
        }
    }
}