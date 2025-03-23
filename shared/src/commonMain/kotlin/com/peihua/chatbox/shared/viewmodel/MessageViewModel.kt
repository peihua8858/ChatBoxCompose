package com.peihua.chatbox.shared.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peihua.chatbox.shared.db.AppDatabase
import com.peihua.chatbox.shared.db.ChatBoxMessage
import com.peihua.chatbox.shared.db.DatabaseHelper
import com.peihua.chatbox.shared.db.Menu
import com.peihua.chatbox.shared.db.Message
import com.peihua.chatbox.shared.db.MessageQueries
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MessageViewModel(
    val database: AppDatabase = DatabaseHelper.database,
    val messageQueries: MessageQueries = database.messageQueries
) : ViewModel() {
    val updateState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    val listMsgState = mutableStateOf<ResultData<List<ChatBoxMessage>>>(ResultData.Initialize())
    val selMsgState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())

    private val _messages = MutableStateFlow<List<ChatBoxMessage>>(emptyList())
    val messages: StateFlow<List<ChatBoxMessage>> get() = _messages

    val mUiState: StateFlow<UiState>
//    val userAction: (UiAction) -> Unit
    val pagingDataFlow: Flow<ResultData<ChatBoxMessage>>

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        val searchAction = actionStateFlow
            .filterIsInstance<UiAction.SendMsg>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .onStart { emit(UiAction.SendMsg(Long.MIN_VALUE, query = "")) }
        val scrollAction = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 1)
            .onStart { emit(UiAction.Scroll(query = "")) }
        mUiState = combine(searchAction, scrollAction, ::Pair)
            .flowOn(Dispatchers.IO)
            .map { (search, scroll) ->
                UiState(query = search.query, currentQuery = scroll.query)
            }.stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                UiState(query = "", currentQuery = "")
            )
        pagingDataFlow =
            searchAction.flatMapLatest { sendMessage(it.menuId, it.query) }
                .flowOn(Dispatchers.IO)
    }

    // 发送消息并处理 AI 的流式响应
    fun sendMessage(menuId: Long, message: String): Flow<ResultData<ChatBoxMessage>> {
        return
    }

    // 发送消息并处理 AI 的流式响应
    fun sendMessage1(menuId: Long, message: String) {
        viewModelScope.launch {
            // 添加用户消息
            _messages.value += ChatBoxMessage(menuId, message, isUser = true)
            insertMessage(Message(menuId, message, true), menuId)
//            val systemMsg = ChatBoxMessage()
            // 获取 AI 的流式响应
            val result = getAIResponseStream(message)
                .onEach { aiResponse ->
                    // 更新最后一条消息（AI 的回复）
                    val msg = ChatBoxMessage(
                        menuId,
                        aiResponse,
                        isUser = false
                    )
                    _messages.value = _messages.value.dropLast(1) + msg
                    insertMessage(Message(menuId, aiResponse, false), menuId)
                }
                .catch { e ->
                    // 处理错误
                    _messages.value += ChatBoxMessage(menuId, "Error: ${e.message}", isUser = false)
                }
                .collect(
                    collector = {
                    }
                )
            result
        }
    }

    // 模拟 AI 返回的流式数据
    private fun getAIResponseStream(message: String): Flow<String> {
        return flow {
            // 模拟流式返回数据
            val responseParts = listOf(
                "AI: 这是",
                "对你的",
                "流式回复"
            )
            responseParts.forEach { part ->
                emit(part)
                delay(500) // 模拟延迟
            }
        }
    }

//    fun requestAllMessages() {
//        request(listMsgState) {
//            selectAllMessages()
//        }
//    }

    fun requestAllMessagesByMenuId(menuId: Long) {
        request(listMsgState) {
            val result = selectAllByMenuId(menuId)
            val rs = result.ifEmpty {
                val messages = listOf(
                    Message(menuId, "New Chat $menuId content"),
                    Message(menuId, "New Chat1 $menuId content"),
                    Message(menuId, "New Chat2 $menuId content"),
                    Message(menuId, "New Chat3 $menuId content"),
                    Message(menuId, "New Chat4 $menuId content")
                )
                insertMessages(messages, menuId)
                messages
            }
            _messages.value = rs.map { it.ChatBoxMessage() }
            messages.value
        }
    }

    fun requestMessageById(messageId: Long) {
        request(selMsgState) {
            selectMessageById(messageId)
        }
    }

    fun updateMessageContent(message: Message) {
        request(updateState) {
            messageQueries.updateMessageContentById(message.content, message.update_time, message._id)
            message
        }
    }

    fun updateMessageMenuById(messageId: Long, menu: Menu) {
        request(updateState) {
            messageQueries.updateMessageMenuById(menu._id, menu.update_time, messageId)
            selectMessageById(messageId)
        }
    }

    fun insertMessage(message: Message, menu: Menu) {
        request(updateState) {
            messageQueries.insertMessage(
                menu._id,
                message.user_type,
                message.content,
                message.create_time,
                message.update_time
            )
            val lastInsertedId = messageQueries.lastInsertId().executeAsOne()
            selectMessageById(lastInsertedId)
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

sealed class UiAction {
    data class SendMsg(val menuId: Long, val query: String) : UiAction()
    data class Scroll(val query: String) : UiAction()
}

data class UiState(
    val query: String,
    val currentQuery: String
)