package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MainUi
import kotlinx.coroutines.flow.Flow

@Dao
interface MainUiDao {
    @Query("SELECT * FROM mainui")
    fun getAllMainUi(): Flow<List<MainUi>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMainUi(ui: List<MainUi>)

    @Query("DELETE FROM amenities")
    suspend fun deleteMainUi()
}