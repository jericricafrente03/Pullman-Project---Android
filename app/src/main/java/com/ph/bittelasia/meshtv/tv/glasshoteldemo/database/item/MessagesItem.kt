package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Entity(tableName = "messages")
class MessagesItem(
    @PrimaryKey
    @SerialName("id")
    val id: Int,

    @ColumnInfo(name = "sender")
    @SerialName("sender")
    val sender: String,

    @ColumnInfo(name = "message")
    @SerialName("message")
    val message: String,

    @ColumnInfo(name = "time")
    @SerialName("time")
    val time: String,

    @ColumnInfo(name = "messageIcon")
    @SerialName("messageIcon")
    val messageIcon: String,

    @ColumnInfo(name = "status")
    @SerialName("status")
    val status: String,

    @ColumnInfo(name = "subTitle")
    @SerialName("subTitle")
    val subTitle: String

)