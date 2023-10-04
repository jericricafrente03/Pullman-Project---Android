package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MainUi
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MessagesItem
import kotlinx.coroutines.flow.Flow

@Dao
interface MessagesDao {
    @Query("SELECT * FROM messages")
    fun getAllMessages(): Flow<List<MessagesItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMessages(mess: List<MessagesItem>)

    @Query("DELETE FROM messages")
    suspend fun deleteMessages()
}