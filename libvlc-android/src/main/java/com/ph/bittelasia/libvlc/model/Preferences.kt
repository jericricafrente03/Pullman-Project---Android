package com.ph.bittelasia.libvlc.model

import android.content.SharedPreferences
import org.json.JSONArray
import org.json.JSONException
import java.util.*

object Preferences {
    const val TAG = "VLC/Util/Preferences"
    fun getFloatArray(pref: SharedPreferences, key: String?): FloatArray? {
        var array: FloatArray? = null
        val s = pref.getString(key, null)
        if (s != null) {
            try {
                val json = JSONArray(s)
                array = FloatArray(json.length())
                for (i in array.indices) array[i] = json.getDouble(i).toFloat()
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }
        return array
    }

    fun putFloatArray(editor: SharedPreferences.Editor, key: String?, array: FloatArray) {
        try {
            val json = JSONArray()
            for (f in array) json.put(f.toDouble())
            editor.putString(key, json.toString())
        } catch (e: JSONException) {
            e.printStackTrace()
        }
    }

    fun putVideoInfoArray(
        editor: SharedPreferences.Editor,
        key: String?,
        list: ArrayList<VideoInfo?>
    ) {
        try {
            val json = JSONArray()
            for (videoInfo in list) {
                json.put(videoInfo)
            }
            editor.putString(key, json.toString())
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}