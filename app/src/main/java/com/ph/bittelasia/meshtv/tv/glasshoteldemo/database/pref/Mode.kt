package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

object Mode {
    private val Context.DataStore: DataStore<Preferences> by preferencesDataStore(name = "dayMode")
    val ID = stringPreferencesKey("ID")
    val STATUS = stringPreferencesKey("STATUS")

    suspend fun Context.saveDayMode(mode: DayMode) {
        coroutineScope {
            launch {
                DataStore.edit { datastore ->
                    mode.apply {
                        datastore[Mode.STATUS] = STATUS
                        datastore[Mode.ID] = ID
                    }
                }
            }
        }
    }
    private suspend fun Context.readDayMode(callback: suspend (Flow<DayMode>) -> Unit) {
        callback.invoke(DataStore.data.map { pref ->
            DayMode.apply {
                STATUS = pref[Mode.STATUS]?: ""
                ID = pref[Mode.ID]?:""
            }
        })
    }
    fun Context.readDayMode(dispatcher: CoroutineDispatcher, callback: suspend (DayMode) -> Unit) {
        CoroutineScope(Dispatchers.Main).launch {
            readDayMode {
                it.collect { mode ->
                    launch(dispatcher) {
                        callback.invoke(mode)
                    }
                }
            }
        }
    }
}