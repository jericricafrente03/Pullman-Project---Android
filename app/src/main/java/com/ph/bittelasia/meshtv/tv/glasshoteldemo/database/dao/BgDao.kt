package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.Bg
import kotlinx.coroutines.flow.Flow

@Dao
interface BgDao {
    @Query("SELECT * FROM bg")
    fun getBg(): Flow<List<Bg>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBg(ui: List<Bg>)

    @Query("DELETE FROM bg")
    suspend fun deleteBg()
}