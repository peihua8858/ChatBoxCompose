package com.peihua.chatbox.shared.data.db

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.peihua.chatbox.shared.currentTimeMillis
import com.peihua.chatbox.shared.data.db.Menu
import com.peihua.chatbox.shared.data.db.Message

enum class UserType(val value: Long) {
    USER(1),
    SYSTEM(0)
}

fun Menu(name: String): Menu {
    return Menu(
        0, name, 0, null, currentTimeMillis(), currentTimeMillis()
    )
}

fun Message(menuId: Long, content: String, userType: UserType): Message {
    return Message(0, menuId, userType.value, content, currentTimeMillis(), currentTimeMillis())
}

fun Message(menuId: Long, content: String, isUser: Boolean): Message {
    return Message(menuId, content, if (isUser) UserType.USER else UserType.SYSTEM)
}

fun Message(menuId: Long, content: String): Message {
    return Message(0, menuId, 0, content, currentTimeMillis(), currentTimeMillis())
}

fun ChatBoxMessage(menuId: Long, content: String, isUser: Boolean): ChatBoxMessage {
    return ChatBoxMessage(menuId, content, if (isUser) UserType.USER else UserType.SYSTEM)
}

fun ChatBoxMessage(menuId: Long, content: String, userType: UserType): ChatBoxMessage {
    return ChatBoxMessage(userType, mutableStateOf(content),"", menuId, currentTimeMillis(), currentTimeMillis())
}

fun Message.ChatBoxMessage(): ChatBoxMessage {
    return ChatBoxMessage(
        if (user_type == 1L) UserType.USER else UserType.SYSTEM,
        mutableStateOf(content),
        "",
        menu_id,
        create_time,
        update_time
    )
}

data class ChatBoxMessage(
    val userType: UserType,
    val message: MutableState<String>,
    val icon: String = "",
    val menu_id: Long,
    val create_time: Long,
    val update_time: Long,
)