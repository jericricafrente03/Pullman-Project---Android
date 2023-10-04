package com.ph.bittelasia.libvlc.control.exception

import android.util.Log

class PlayerException(message: String) : RuntimeException(message) {
    init {
        Log.e(this.javaClass.simpleName, "@PlayerException: $message")
    }
}