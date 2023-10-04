package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Entity(tableName = "tvList")
data class ChannelData(
    @ColumnInfo(name = "channelCategoryId")
    @SerialName("channelCategoryId")
    val channelCategoryId: String,

    @ColumnInfo(name = "channelDescription")
    @SerialName("channelDescription")
    val channelDescription: String,

    @ColumnInfo(name = "channelImage")
    @SerialName("channelImage")
    val channelImage: String,

    @ColumnInfo(name = "channelPrograms")
    @SerialName("channelPrograms")
    val channelPrograms: String,

    @ColumnInfo(name = "channelTitle")
    @SerialName("channelTitle")
    val channelTitle: String,

    @ColumnInfo(name = "channelUri")
    @SerialName("channelUri")
    val channelUri: String,

    @ColumnInfo(name = "enabled")
    @SerialName("enabled")
    val enabled: String,

    @PrimaryKey
    @SerialName("id")
    val id: String
)
