package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.TvItem
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model.ChannelData
import kotlinx.coroutines.flow.Flow

@Dao
interface TvListDao {
    @Query("SELECT * FROM tvList")
    fun getAllTvList(): Flow<List<ChannelData>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTvList(tv: List<ChannelData>)

    @Query("DELETE FROM tvList")
    suspend fun deleteTvList()
}