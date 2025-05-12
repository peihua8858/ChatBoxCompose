package com.peihua.chatbox.shared.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.peihua.chatbox.shared.data.Message
import kotlinx.coroutines.flow.Flow

@Dao
interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(message: Message)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(messages: List<Message>)

    @Query("SELECT * FROM Message")
    fun getAllAsFlow(): Flow<List<Message>>

    @Query("SELECT COUNT(*) as count FROM Message")
    suspend fun count(): Int

    @Query("SELECT * FROM Message WHERE id in (:ids)")
    suspend fun loadAll(ids: List<Long>): List<Message>

    @Query("SELECT * FROM Message WHERE id in (:ids)")
    suspend fun loadMapped(ids: List<Long>): Map<
            @MapColumn(columnName = "id")
            Long,
            Message,
            >


    @Update(entity = Message::class)
    suspend fun updateMessage(message: Message)



    @Query("SELECT * FROM Message WHERE id = :messageId")
    suspend fun selectMessageById(messageId: Long): Message

    @Query("SELECT * FROM Message WHERE menu_id = :menuId")
    suspend fun selectAllMessagesByMenuId(menuId: Long): List<Message>
    @Query("SELECT * FROM Message ORDER BY update_time DESC")
    suspend fun selectAllMessagesSortedByUpdateTime(): List<Message>
}
