package com.peihua.chatbox.shared.viewmodel

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.GraphicsLayerScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.peihua.chatbox.shared.data.db.AppDatabase
import com.peihua.chatbox.shared.data.db.ChatBoxMessage
import com.peihua.chatbox.shared.data.db.DatabaseHelper
import com.peihua.chatbox.shared.data.db.Menu
import com.peihua.chatbox.shared.data.db.Message
import com.peihua.chatbox.shared.data.db.MessageQueries
import com.peihua.chatbox.shared.data.remote.repository.ChatAiRepository
import com.peihua.chatbox.shared.data.remote.repository.impl.OpenAiRepositoryImpl
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.WorkScope
import com.peihua.chatbox.shared.utils.dLog
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterIsInstance
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MessageViewModel2(
    val database: AppDatabase = DatabaseHelper.database,
    val messageQueries: MessageQueries = database.messageQueries,
    val repository: ChatAiRepository = OpenAiRepositoryImpl(),
) : ViewModel(),CoroutineScope by WorkScope() {
    val updateState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    val selMsgState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    val _messages = mutableStateListOf<ChatBoxMessage>()
    val enInputState = mutableStateOf(true)
    val mUiState: StateFlow<UiState>
    val userAction: (UiAction) -> Unit
    val pagingDataFlow: Flow<ChatBoxMessage>

    init {
        val actionStateFlow = MutableSharedFlow<UiAction>()
        // 发送消息的流
        val sendMsgAction = actionStateFlow
            .filterIsInstance<UiAction.QueryOrSendMsg>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .catch { e ->
                dLog { "Error in stream: ${e.message}" }  // 日志输出
            }
        val scrollAction = actionStateFlow
            .filterIsInstance<UiAction.Scroll>()
            .distinctUntilChanged()
            .flowOn(Dispatchers.IO)
            .shareIn(viewModelScope, SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000), 1)

        // 处理发送消息的流
        pagingDataFlow = sendMsgAction
            .flatMapLatest {
                dLog { "MessageViewModel>>>>>> load messages>>$it" }
                sendMessage(it.menuId, it.query)
            } // 替换为你的发送消息的函数
            .flowOn(Dispatchers.IO)
            .catch { e ->
                dLog { "Error in stream: ${e.message}" }  // 日志输出
            }
        // 组合 UI 状态
        val combineFlow = combine(
            sendMsgAction,
            scrollAction,
        ) { sendMsgAction, scrollAction ->
            UiState(
                menuId = sendMsgAction.menuId,
                query = sendMsgAction.query,
                lastMeuId = scrollAction.menuId,
                lastQueryScrolled = scrollAction.currentQuery,
                hasNotScrolledForCurrentSearch = sendMsgAction.query != scrollAction.currentQuery
            )
        }

        // 组合 UI 状态
        mUiState = combineFlow.flowOn(Dispatchers.IO)
            .stateIn(
                viewModelScope,
                SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                UiState(menuId = 0L, query = "", lastMeuId = 0L, lastQueryScrolled = "")// 初始化状态
            )
        // 用户动作的处理
        userAction = {
            viewModelScope.launch {
                dLog { "User Action Triggered: $it" }  // 添加调试日志
                actionStateFlow.emit(it)
            }
        }
        launch {
            pagingDataFlow.collect {
                dLog { "Paging Data Collected: $it" }  // 添加调试日志
                _messages.add(0,it)
            }
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

    override fun onCleared() {
        super.onCleared()
        cancel()
    }
}


