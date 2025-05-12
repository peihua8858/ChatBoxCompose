package com.peihua.chatbox.shared.data

import androidx.compose.runtime.mutableStateOf
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.peihua.chatbox.shared.currentTimeMillis
import com.peihua.chatbox.shared.data.db.ChatBoxMessage
import com.peihua.chatbox.shared.data.db.UserType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
@Entity
public data class Message(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    public val id: Long,
    @SerialName("menu_id")
    public val menu_id: Long,
    @SerialName("user_type")
    public val user_type: Long,
    @SerialName("content")
    public val content: String,
    @SerialName("create_time")
    public val create_time: Long,
    @SerialName("update_time")
    public val update_time: Long,
){
    companion object{
        fun Message.ChatBoxMessage():ChatBoxMessage{
            return ChatBoxMessage(
                if (user_type == 1L) UserType.USER else UserType.SYSTEM,
                mutableStateOf(content),
                "",
                menu_id,
                create_time,
                update_time
            )
        }
        fun Message(menu_id: Long,content: String,isUser: Boolean): Message{
            return Message(0,menu_id,if (isUser) 1L else 2L,content,currentTimeMillis(),currentTimeMillis())
        }
    }
}

@Serializable
@Entity
public data class Menu(
    @PrimaryKey(autoGenerate = true)
    @SerialName("id")
    public val id: Long,
    @SerialName("menu_name")
    public val menu_name: String,
    @SerialName("isDefault")
    public val isDefault: Long,
    @SerialName("icon")
    public val icon: String?,
    @SerialName("create_time")
    public val create_time: Long,
    @SerialName("update_time")
    public val update_time: Long,
)
