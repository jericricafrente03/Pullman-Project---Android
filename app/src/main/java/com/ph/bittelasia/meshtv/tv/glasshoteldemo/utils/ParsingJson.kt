package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MessagesItem
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model.ChannelData
import java.io.IOException


object ParsingJson {
    fun getTvList(context: Context): List<ChannelData> {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("tvlist.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.e("error","$ioException")
        }
        val listCountryType = object : TypeToken<List<ChannelData>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)
    }
    fun getMessage(context: Context): List<MessagesItem> {
        lateinit var jsonString: String
        try {
            jsonString = context.assets.open("message.json")
                .bufferedReader()
                .use { it.readText() }
        } catch (ioException: IOException) {
            Log.e("error","$ioException")
        }
        val listCountryType = object : TypeToken<List<MessagesItem>>() {}.type
        return Gson().fromJson(jsonString, listCountryType)
    }
}