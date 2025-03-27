package com.peihua.chatbox.shared.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peihua.chatbox.shared.data.db.AppDatabase
import com.peihua.chatbox.shared.data.db.ChatBoxMessage
import com.peihua.chatbox.shared.data.db.DatabaseHelper
import com.peihua.chatbox.shared.data.db.Message
import com.peihua.chatbox.shared.data.db.MessageQueries
import com.peihua.chatbox.shared.data.remote.repository.ChatAiRepository
import com.peihua.chatbox.shared.data.remote.repository.impl.OpenAiRepositoryImpl
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

class MessageViewModel(
    val database: AppDatabase = DatabaseHelper.database,
    val messageQueries: MessageQueries = database.messageQueries,
    val repository: ChatAiRepository = OpenAiRepositoryImpl(),
) : ViewModel() {
    val enInputState = mutableStateOf(true)

    private val _messages = mutableStateListOf<ChatBoxMessage>()
    val messages: MutableState<ResultData<List<ChatBoxMessage>>> =
        mutableStateOf(ResultData.Initialize())

    // 发送消息方法
    private fun sendMessage(menuId: Long, message: String): Flow<ChatBoxMessage> {
        dLog { "MessageViewModel>>>>>> load menuId>>$menuId" }
        return flow {
            if (message.isEmpty()) {
                val result = selectAllByMenuId(menuId)
                result.map { it.ChatBoxMessage() }.forEach { emit(it) }
            } else {
                enInputState.value = false
                // 在这里处理你的消息发送并更新数据库
                val userMsg = ChatBoxMessage(menuId, message, isUser = true)
                emit(userMsg) // 替换为相应的消息对象
                insertMessage(Message(menuId, message, true), menuId)
                var dbMsg = insertMessage(Message(menuId, "Let me thinking...", false), menuId)
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

    fun queryAllMessagesByMenuId(menuId: Long) {
        request(messages) {
            delay(2000)
            val result = selectAllByMenuId(menuId)
            result.map { it.ChatBoxMessage() }.forEach { _messages.add(0,it)}
            _messages
        }
    }

    fun sendMessageByMenuId(menuId: Long, message: String) {
        viewModelScope.launch(Dispatchers.IO) {
            sendMessage(menuId, message).collect {
                _messages.add(0,it)
            }
            _messages
        }
    }

    suspend fun updateMessage(message: Message): Message {
        return withContext(Dispatchers.IO) {
            messageQueries.updateMessageContentById(
                message.content,
                message.update_time,
                message._id
            )
            selectMessageById(message._id)
        }
    }

    suspend fun insertMessages(messages: List<Message>, menuId: Long) {
        for (message in messages) {
            insertMessage(message, menuId)
        }
    }

    suspend fun insertMessage(message: Message, menuId: Long): Message {
        return withContext(Dispatchers.IO) {
            messageQueries.insertMessage(
                menuId,
                message.user_type,
                message.content,
                message.create_time,
                message.update_time
            )
            val lastInsertedId = messageQueries.lastInsertId().executeAsOne()
            selectMessageById(lastInsertedId)
        }
    }

    suspend fun selectMessageById(messageId: Long): Message {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectMessageById(messageId)
            return@withContext result.executeAsOne()
        }
    }

    suspend fun selectAllMessages(): List<Message> {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectAllMessagesSortedByUpdateTime()
            return@withContext result.executeAsList()
        }
    }

    suspend fun selectAllByMenuId(menuId: Long): List<Message> {
        return withContext(Dispatchers.IO) {
            val result = messageQueries.selectAllMessagesByMenuId(menuId).executeAsList()
            result
        }
    }
}

sealed class UiAction() {
    data class QueryOrSendMsg(val menuId: Long, val query: String = "") : UiAction()
    data class Scroll(val menuId: Long, val currentQuery: String) : UiAction()
}

data class UiState(
    val menuId: Long,
    val query: String,
    val lastMeuId: Long,
    val lastQueryScrolled: String,
    val hasNotScrolledForCurrentSearch: Boolean = false,
)
