package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "tv")
data class TvItem(
    @PrimaryKey
    @SerialName("id")
    val id: String,

    @ColumnInfo(name = "tvCategory")
    @SerialName("tvCategory")
    val tvCategory: String,

    @ColumnInfo(name = "title")
    @SerialName("title")
    val title: String,
)