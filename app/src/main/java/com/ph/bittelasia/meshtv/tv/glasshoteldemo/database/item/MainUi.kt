package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "mainui")
data class MainUi(
    @PrimaryKey
    @SerialName("id")
    val id: Int,

    @ColumnInfo(name = "logo")
    @SerialName("logo")
    val logo: String,

    @ColumnInfo(name = "caption")
    @SerialName("caption")
    val caption : String,

    @ColumnInfo(name = "enable")
    @SerialName("enable")
    val enable: Boolean
)
