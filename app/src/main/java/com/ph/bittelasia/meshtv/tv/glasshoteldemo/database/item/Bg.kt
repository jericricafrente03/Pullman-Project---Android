package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName

@Entity(tableName = "bg")
data class Bg(
    @PrimaryKey
    @SerialName("id")
    val id: Int,

    @ColumnInfo(name = "background")
    @SerialName("background")
    val background: String,
)