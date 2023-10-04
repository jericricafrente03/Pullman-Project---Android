package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.AmenitiesItem
import kotlinx.coroutines.flow.Flow


@Dao
interface AmenitiesDao {
    @Query("SELECT * FROM amenities")
    fun getAllAmenities(): Flow<List<AmenitiesItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAmenities(appData: List<AmenitiesItem>)

    @Query("DELETE FROM amenities")
    suspend fun deleteAllAmenities()
}