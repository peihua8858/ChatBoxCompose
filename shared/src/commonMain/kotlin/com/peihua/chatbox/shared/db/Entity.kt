package com.peihua.chatbox.shared.db

import com.peihua.chatbox.shared.currentTimeMillis

fun Menu(name: String): Menu {
    return Menu(
        0, name, 0, null, currentTimeMillis(), currentTimeMillis()
    )
}

fun Message(menuId: Long, content: String): Message {
    return Message(0, menuId, content, currentTimeMillis(), currentTimeMillis())
}
