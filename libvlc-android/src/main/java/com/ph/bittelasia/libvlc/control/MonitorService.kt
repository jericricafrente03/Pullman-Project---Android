package com.ph.bittelasia.libvlc.control

import android.app.IntentService
import android.content.Intent
import android.util.Log

class MonitorService(name: String?) : IntentService(name) {
    override fun onHandleIntent(intent: Intent?) {
        val date = intent!!.getStringExtra("date")
        Log.e(TAG, "@onHandleIntent: $date")
    }

    companion object {
        private const val TAG = "MonitorService"
    }
}