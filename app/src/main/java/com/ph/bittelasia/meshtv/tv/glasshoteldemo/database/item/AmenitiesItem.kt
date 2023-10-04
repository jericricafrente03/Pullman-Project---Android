package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Entity(tableName = "amenities")
data class AmenitiesItem(
    @ColumnInfo(name = "caption")
    @SerialName("caption")
    val caption: String,

    @ColumnInfo(name = "category")
    @SerialName("category")
    val category: String,

    @ColumnInfo(name = "description")
    @SerialName("description")
    val description: String,

    @ColumnInfo(name = "enable")
    @SerialName("enable")
    val enable: Boolean,

    @PrimaryKey
    @SerialName("id")
    val id: Int,

    @ColumnInfo(name = "image")
    @SerialName("image")
    val image: String,

    @ColumnInfo(name = "logo")
    @SerialName("logo")
    val logo: String
)