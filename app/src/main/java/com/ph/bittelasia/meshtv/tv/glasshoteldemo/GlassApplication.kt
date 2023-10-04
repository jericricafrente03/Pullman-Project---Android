package com.ph.bittelasia.meshtv.tv.glasshoteldemo

import android.app.Application


class GlassApplication : Application(), Thread.UncaughtExceptionHandler {
    override fun uncaughtException(p0: Thread, p1: Throwable) {
        Thread.setDefaultUncaughtExceptionHandler(this)
    }
}