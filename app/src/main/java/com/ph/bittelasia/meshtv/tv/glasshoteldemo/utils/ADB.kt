package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import android.util.Log
import eu.chainfire.libsuperuser.Shell

object ADB {

    private var TAG = "MAINTENANCE/ADB"
    private  var result: String? = null

    fun exec(adb: String) {
            try {
                Log.i(TAG, "@exec --> command : [$adb]")
                val suAvailable = Shell.SU.available()
                if (suAvailable) {
                    val suVersion = Shell.SU.version(false)
                    val suVersionInternal = Shell.SU.version(true)
                    val suResult = Shell.SU.run(
                        arrayOf(
                            adb
                        )
                    )
                    Log.i(TAG, "@result --> $suResult")
                    result = suResult.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    fun result(adb: String): String {
        var result = ""
        try {
            Log.i(TAG, "@exec --> command : [$adb]")
            val suAvailable = Shell.SU.available()
            if (suAvailable) {
                val suVersion = Shell.SU.version(false)
                val suVersionInternal = Shell.SU.version(true)
                val suResult = Shell.SU.run(
                    arrayOf(
                        adb
                    )
                )
                Log.i(TAG, "@exec --> $suResult")
                return suResult.toString().also { result = it }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return result
    }
}