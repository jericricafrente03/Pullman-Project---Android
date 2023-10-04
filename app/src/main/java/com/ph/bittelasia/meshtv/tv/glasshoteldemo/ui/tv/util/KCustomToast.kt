package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.util

import android.app.Activity
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.Toast
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R

class KCustomToast {
    companion object {
        private lateinit var layoutInflater: LayoutInflater
        fun infoToast(context: Activity) {
            context.runOnUiThread {
                try {
                    layoutInflater = LayoutInflater.from(context)
                    val layout = layoutInflater.inflate(
                        R.layout.custom_toast_layout,
                        (context).findViewById(R.id.custom_toast_layout)
                    )
                    val toast = Toast(context.applicationContext)
                    toast.duration = Toast.LENGTH_LONG
                    toast.setGravity(Gravity.FILL_HORIZONTAL, 0, 10)
                    toast.view = layout //setting the view of custom toast layout
                    toast.show()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
}