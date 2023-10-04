package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.dao.*
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.*
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model.ChannelData

@Database(
    entities = [
        AmenitiesItem::class,
        MainUi::class,
        ChannelData::class,
        Bg::class
    ], version = 1, exportSchema = false
)
abstract class GlassDB : RoomDatabase() {
    companion object {
        @Volatile
        private var INSTANCE: GlassDB? = null

        fun db(context: Context): GlassDB {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    GlassDB::class.java,
                    "glass_db"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
    abstract fun amenitiesDao(): AmenitiesDao
    abstract fun mainUiDao(): MainUiDao
    abstract fun tvListDao(): TvListDao
    abstract fun bGDao(): BgDao
}