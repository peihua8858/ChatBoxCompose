package com.peihua.chatbox.shared.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peihua.chatbox.shared.compose.Settings
import com.peihua.chatbox.shared.compose.settings
import com.peihua.chatbox.shared.compose.settings.tabs.model.Model
import com.peihua.chatbox.shared.compose.settings.tabs.model.ModelProvider
import com.peihua.chatbox.shared.data.Message
import com.peihua.chatbox.shared.data.Message.Companion.ChatBoxMessage
import com.peihua.chatbox.shared.data.database.AppDatabase
import com.peihua.chatbox.shared.data.database.MessageDao
import com.peihua.chatbox.shared.data.db.ChatBoxMessage
import com.peihua.chatbox.shared.data.remote.repository.ChatAiRepository
import com.peihua.chatbox.shared.data.remote.repository.ChatAiRepositoryFactory
import com.peihua.chatbox.shared.data.remote.repository.impl.OpenAiRepositoryImpl
import com.peihua.chatbox.shared.di.Factory
import com.peihua.chatbox.shared.di.FactoryImpl
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.dLog
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class RoomMessageViewModel(
    val factory: Factory = FactoryImpl(),
    val database: AppDatabase = factory.createRoomDatabase(),
    val messageQueries: MessageDao = database.messageDao(),
    var repository: ChatAiRepository = ChatAiRepositoryFactory.create(settings.value.modelProvider.model),
) : ViewModel() {
    val enInputState = mutableStateOf(true)
    private val _messages = mutableStateListOf<ChatBoxMessage>()
    val messages: MutableState<ResultData<List<ChatBoxMessage>>> =
        mutableStateOf(ResultData.Initialize())

    // 发送消息方法
    private fun sendMessage(menuId: Long, message: String): Flow<ChatBoxMessage> {
        dLog { "RoomMessageViewModel>>>>>> load menuId>>$menuId" }
        return flow {
            checkModel()
            if (message.isEmpty()) {
                val result = selectAllByMenuId(menuId)
                result.map { it.ChatBoxMessage() }.forEach { emit(it) }
            } else {
                enInputState.value = false
                // 在这里处理你的消息发送并更新数据库
                val userMsg = ChatBoxMessage(menuId, message, isUser = true)
                emit(userMsg) // 替换为相应的消息对象
                insertMessage(Message.Message(menuId, message, true), menuId)
                var dbMsg = insertMessage(Message.Message(menuId, "Let me thinking...", false), menuId)
                val sysMsg = ChatBoxMessage(menuId, "Let me thinking...", isUser = false)
                emit(sysMsg)
                repository.textCompletionsWithStream(message)
                    .onCompletion {
                        enInputState.value = true
                    }
                    .collect { aiResponse ->
                        dLog { ">>>>>>AI Response: $aiResponse" }
                        if (sysMsg.message.value == "Let me thinking...") {
                            // 更新最后一条消息（AI 的回复）
                            sysMsg.message.value = aiResponse
                        } else {
                            sysMsg.message.value += aiResponse
                        }
                        dbMsg = updateMessage(dbMsg.copy(content = sysMsg.message.value))
                    }
            }
        }
    }

    /**
     * 查询所有消息
     * @param menuId 菜单id
     */
    fun queryAllMessagesByMenuId(menuId: Long) {
        request(messages) {
            delay(2000)
            _messages.clear()
            val result = selectAllByMenuId(menuId)
            result.map { it.ChatBoxMessage() }.forEach { _messages.add(0, it) }
            _messages
        }
    }

    fun sendMessageByMenuId(menuId: Long, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessage(menuId, message).collect {
                _messages.add(0, it)
            }
        }
    }

    suspend fun updateMessage(message: Message): Message {
        return withContext(Dispatchers.IO) {
            messageQueries.updateMessage(message)
            selectMessageById(message.id)
        }
    }

    suspend fun insertMessages(messages: List<Message>, menuId: Long) {
        for (message in messages) {
            insertMessage(message, menuId)
        }
    }

    suspend fun insertMessage(message: Message, menuId: Long): Message {
        return withContext(Dispatchers.IO) {
            val newMessage = message.copy(menu_id = menuId)
            messageQueries.insert(newMessage)
            val lastInsertedId = selectAllByMenuId(menuId).last().id
            selectMessageById(lastInsertedId)
        }
    }

    suspend fun selectMessageById(messageId: Long): Message {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectMessageById(messageId)
            return@withContext result
        }
    }

    suspend fun selectAllMessages(): List<Message> {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectAllMessagesSortedByUpdateTime()
            return@withContext result
        }
    }

    suspend fun selectAllByMenuId(menuId: Long): List<Message> {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectAllMessagesByMenuId(menuId)
            result
        }
    }
    var curModel:Model = Model.OPenAI
    /**
     * 检查模型
     */
    fun checkModel() {
        val model =  settings.value.modelProvider.model
        if (curModel != model) {
            curModel = model
            repository = ChatAiRepositoryFactory.create(model)
        }
    }
}

class ViewModelFactory(private val factory: Factory) : ViewModel() {
    val roomMessageViewModel = RoomMessageViewModel(factory)
}
//
//sealed class UiAction() {
//    data class QueryOrSendMsg(val menuId: Long, val query: String = "") : UiAction()
//    data class Scroll(val menuId: Long, val currentQuery: String) : UiAction()
//}
//
//data class UiState(
//    val menuId: Long,
//    val query: String,
//    val lastMeuId: Long,
//    val lastQueryScrolled: String,
//    val hasNotScrolledForCurrentSearch: Boolean = false,
//)
