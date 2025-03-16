package com.peihua.chatbox.shared.repository

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.peihua.chatbox.shared.db.AppDatabase
import com.peihua.chatbox.shared.db.DatabaseHelper
import com.peihua.chatbox.shared.db.Menu
import com.peihua.chatbox.shared.db.Message
import com.peihua.chatbox.shared.db.MessageQueries
import com.peihua.chatbox.shared.utils.ResultData
import com.peihua.chatbox.shared.utils.request
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.withContext


class MessageRepository(
    val database: AppDatabase = DatabaseHelper.database,
    val messageQueries: MessageQueries = database.messageQueries
) : ViewModel() {
    val updateState = mutableStateOf<ResultData<Message>>(ResultData.Starting())
    val listMsgState = mutableStateOf<ResultData<List<Message>>>(ResultData.Starting())
    val selMsgState = mutableStateOf<ResultData<Message>>(ResultData.Starting())
    fun requestAllMessages() {
        request(listMsgState) {
            selectAllMessages()
        }
    }

    fun requestMessageById(messageId: Long) {
        this.request(selMsgState) {
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
}