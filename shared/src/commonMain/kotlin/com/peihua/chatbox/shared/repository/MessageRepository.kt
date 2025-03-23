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
    val updateState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    val listMsgState = mutableStateOf<ResultData<List<Message>>>(ResultData.Initialize())
    val selMsgState = mutableStateOf<ResultData<Message>>(ResultData.Initialize())
    fun requestAllMessages() {
        request(listMsgState) {
            selectAllMessages()
        }
    }

    fun requestAllMessagesByMenuId(menuId: Long) {
        request(listMsgState) {
            val result = selectAllByMenuId(menuId)
            result.ifEmpty {
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
            messageQueries.insertMessage(menu._id, message.content, message.create_time, message.update_time)
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
            messageQueries.insertMessage(menuId, message.content, message.create_time, message.update_time)
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