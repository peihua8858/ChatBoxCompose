package com.peihua.chatbox.shared.data.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.MapColumn
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.peihua.chatbox.shared.data.Menu
import kotlinx.coroutines.flow.Flow

@Dao
interface MenuDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruittie: Menu)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(fruitties: List<Menu>)

    @Query("SELECT * FROM Menu")
    fun getAllAsFlow(): Flow<List<Menu>>

    @Query("SELECT COUNT(*) as count FROM Menu")
    suspend fun count(): Int

    @Query("SELECT * FROM Menu WHERE id in (:ids)")
    suspend fun loadAll(ids: List<Long>): List<Menu>

    @Query("SELECT * FROM Menu WHERE id in (:ids)")
    suspend fun loadMapped(ids: List<Long>): Map<
        @MapColumn(columnName = "id")
        Long,
            Menu,
        >
}
