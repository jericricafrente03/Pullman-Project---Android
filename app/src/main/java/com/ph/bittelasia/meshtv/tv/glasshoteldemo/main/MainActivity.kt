package com.ph.bittelasia.meshtv.tv.glasshoteldemo.main

import android.view.KeyEvent
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseActivity
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.pref.DayMode
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.pref.Mode.saveDayMode
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.ActivityMainBinding
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.alert.Alert
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.ADB
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivity : BaseActivity<ActivityMainBinding>() {
    private val alert by lazy { Alert() }
    private var controlsEnabled = true

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                showMyDayMode(keyCode)
            }
        }
        return super.onKeyUp(keyCode, event)
    }
    private suspend fun showMyDayMode(code: Int) {
        try {
            when (code) {
                KeyEvent.KEYCODE_1 -> {
                    if (controlsEnabled) {
                        controlsEnabled = false
                        removeFragment(alert, R.id.viewData)
                    } else {
                        controlsEnabled = true
                        attachFragment(alert, R.id.viewData)
                    }
                }
                KeyEvent.KEYCODE_S -> {
                    ADB.exec( "monkey -p com.wyst.mboxsettings -c android.intent.category.LAUNCHER 1")
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getLayout() = R.layout.activity_main
}