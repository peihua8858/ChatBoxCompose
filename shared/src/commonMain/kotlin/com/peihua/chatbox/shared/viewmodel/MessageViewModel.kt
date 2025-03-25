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
import com.peihua.chatbox.shared.utils.dLog
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.IO
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.collections.plusAssign


@OptIn(ExperimentalCoroutinesApi::class)
class MessageViewModel(
    val database: AppDatabase = DatabaseHelper.database,
    val messageQueries: MessageQueries = database.messageQueries,
) : ViewModel() {
    val updateState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    val listMsgState = mutableStateOf<ResultData<List<ChatBoxMessage>>>(ResultData.Initialize())
    val selMsgState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())

    private val _messages = MutableStateFlow<List<ChatBoxMessage>>(emptyList())
    val messages: StateFlow<List<ChatBoxMessage>> get() = _messages.asStateFlow()

    val mUiState: StateFlow<UiState>
    val userAction: (UiAction) -> Unit
    val pagingDataFlow: Flow<ChatBoxMessage>

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        // 发送消息的流
        val sendMsgAction = actionStateFlow
            .filterIsInstance<UiAction.SendMsg>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .onStart { emit(UiAction.SendMsg(Long.MIN_VALUE, query = "")) }

        // 滚动的流
        val loadMessagesAction = actionStateFlow
            .filterIsInstance<UiAction.LoadMessages>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .shareIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                replay = 1
            )
            .onStart { emit(UiAction.LoadMessages(Long.MIN_VALUE,"")) }
        val scrollAction = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 1)
            .onStart { emit(UiAction.Scroll(Long.MIN_VALUE,"")) }

        // 处理发送消息的流
        pagingDataFlow = sendMsgAction
            .flatMapLatest {
                dLog { "MessageViewModel>>>>>> load messages>>$it" }
                sendMessage(it.menuId, it.query)
            } // 替换为你的发送消息的函数
            .flowOn(Dispatchers.IO)
        // 组合 UI 状态
        mUiState = combine(
            sendMsgAction,
            scrollAction,
            loadMessagesAction
        ) { sendMsgAction, scrollAction, loadMessagesAction ->
            UiState(
                menuId = sendMsgAction.menuId,
                query = sendMsgAction.query,
                lastQueryScrolled = scrollAction.currentQuery,
                hasNotScrolledForCurrentSearch = sendMsgAction.query != scrollAction.currentQuery
            )
        }.flowOn(Dispatchers.IO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                UiState(menuId = 0L, query = "", lastQueryScrolled = "")// 初始化状态
            )
        // 用户动作的处理
        userAction = {
            viewModelScope.launch { actionStateFlow.emit(it) }
        }
    }

    // 发送消息方法
    private fun sendMessage(menuId: Long, message: String): Flow<ChatBoxMessage> {
        dLog { "MessageViewModel>>>>>> load menuId>>$menuId" }
        return flow {
            if (message.isEmpty()) {
                val result = selectAllByMenuId(menuId)
                result.map { it.ChatBoxMessage() }.forEach { emit(it) }
            } else {
                // 在这里处理你的消息发送并更新数据库
                val userMsg = ChatBoxMessage(menuId, message, isUser = true)
                emit(userMsg) // 替换为相应的消息对象
                insertMessage(Message(menuId, message, true), menuId)
                var dbMsg = insertMessage(Message(menuId, "", false), menuId)
                val sysMsg = ChatBoxMessage(
                    menuId,
                    "",
                    isUser = false
                )
                getAIResponseStream(message)
                    .onEach { aiResponse ->
                        if (sysMsg.message.value.isEmpty()) {
                            emit(sysMsg)
                        }
                        // 更新最后一条消息（AI 的回复）
                        sysMsg.message.value += aiResponse
                        dbMsg = updateMessage(dbMsg.copy(content = sysMsg.message.value))
                    }.last()
            }
        }
    }
    // 发送消息并处理 AI 的流式响应
//    fun sendMessage(menuId: Long, message: String): Flow<ChatBoxMessage> {
//        return flow {
//            if (message.isEmpty()) {
//                val result = selectAllByMenuId(menuId)
//                result.map { it.ChatBoxMessage() }
//            } else {
//                // 添加用户消息
//                _messages.value += ChatBoxMessage(menuId, message, isUser = true)
//                insertMessage(Message(menuId, message, true), menuId)
//                val msg = ChatBoxMessage(
//                    menuId,
//                    "",
//                    isUser = false
//                )
//                _messages.value = _messages.value.dropLast(1) + msg
//                var dbMsg = insertMessage(Message(menuId, "", false), menuId)
//                getAIResponseStream(message)
//                    .onEach { aiResponse ->
//                        // 更新最后一条消息（AI 的回复）
//                        msg.message.value += aiResponse
//                        dbMsg = updateMessage(dbMsg.copy(content = msg.message.value))
//                    }
//            }
//        }
//    }

    // 发送消息并处理 AI 的流式响应
    fun sendMessage1(menuId: Long, message: String) {
        viewModelScope.launch {
            // 添加用户消息
            _messages.value += ChatBoxMessage(menuId, message, isUser = true)
            insertMessage(Message(menuId, message, true), menuId)


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
                "流式回复",
                "AI: 这是",
                "对你的",
                "流式回复",
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

    suspend fun requestMessagesByMenuId(menuId: Long): Flow<ChatBoxMessage> {
        dLog { "MessageViewModel>>>>>> requestMessagesByMenuId  messages" }
        return flow {
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
            rs.map { it.ChatBoxMessage() }.forEach { emit(it) }
        }

    }

    fun requestMessageById(messageId: Long) {
        request(selMsgState) {
            selectMessageById(messageId)
        }
    }

    fun updateMessageContent(message: Message) {
        request(updateState) {
            messageQueries.updateMessageContentById(
                message.content,
                message.update_time,
                message._id
            )
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

sealed class UiAction {
    data class SendMsg(val menuId: Long, val query: String) : UiAction()
    data class Scroll(val menuId: Long, val currentQuery: String) : UiAction()
    data class LoadMessages(val menuId: Long,val query: String) : UiAction() // 新增 LoadMessages 动作
}

data class UiState(
    val menuId: Long,
    val query: String,
    val lastQueryScrolled: String,
    val hasNotScrolledForCurrentSearch: Boolean = false,
)