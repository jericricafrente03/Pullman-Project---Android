package com.ph.bittelasia.libvlc.control

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.ph.bittelasia.libvlc.model.Channels
import com.ph.bittelasia.libvlc.views.activity.AbstractPlayerActivity
import java.io.File

object ActivityControl {
    fun launchPlayer(
        channels: Channels?,
        activity: AppCompatActivity,
        c: Class<*>?,
        REQUEST_CODE: Int
    ) {
        val bundle = Bundle()
        bundle.putParcelable(AbstractPlayerActivity.KEY, channels)
        val intent = Intent(activity, c)
        intent.putExtras(bundle)
        activity.startActivityForResult(intent, REQUEST_CODE)
    }

    fun refreshPlayer(channels: Channels?, activity: AppCompatActivity, c: Class<*>?) {
        val bundle = Bundle()
        bundle.putParcelable(AbstractPlayerActivity.KEY, channels)
        val intent = Intent(activity, c)
        intent.putExtras(bundle)
        activity.finish()
        activity.startActivity(intent)
    }

    //--------------------------Function to check and request permission----------------------------
    @JvmStatic
    fun checkPermission(context: Context, permission: String, requestCode: Int) {
        try {
            if (ContextCompat.checkSelfPermission(
                    context,
                    permission
                ) == PackageManager.PERMISSION_DENIED
            ) {
                ActivityCompat.requestPermissions(
                    (context as Activity),
                    arrayOf(permission),
                    requestCode
                )
            } else {
                Log.i(context.toString(), "@checkPermission: " + "Permission already granted")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteCache(context: Context) {
        try {
            val dir = context.cacheDir
            deleteDir(dir)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun deleteDir(dir: File?): Boolean {
        return if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                val success = deleteDir(File(dir, children[i]))
                if (!success) {
                    return false
                }
            }
            dir.delete()
        } else if (dir != null && dir.isFile) {
            dir.delete()
        } else {
            false
        }
    }

    @JvmStatic
    fun getInput(event: KeyEvent): String {
        var channel = ""
        when (event.keyCode) {
            KeyEvent.KEYCODE_0, KeyEvent.KEYCODE_NUMPAD_0 -> channel += 0
            KeyEvent.KEYCODE_1, KeyEvent.KEYCODE_NUMPAD_1 -> channel += 1
            KeyEvent.KEYCODE_2, KeyEvent.KEYCODE_NUMPAD_2 -> channel += 2
            KeyEvent.KEYCODE_3, KeyEvent.KEYCODE_NUMPAD_3 -> channel += 3
            KeyEvent.KEYCODE_4, KeyEvent.KEYCODE_NUMPAD_4 -> channel += 4
            KeyEvent.KEYCODE_5, KeyEvent.KEYCODE_NUMPAD_5 -> channel += 5
            KeyEvent.KEYCODE_6, KeyEvent.KEYCODE_NUMPAD_6 -> channel += 6
            KeyEvent.KEYCODE_7, KeyEvent.KEYCODE_NUMPAD_7 -> channel += 7
            KeyEvent.KEYCODE_8, KeyEvent.KEYCODE_NUMPAD_8 -> channel += 8
            KeyEvent.KEYCODE_9, KeyEvent.KEYCODE_NUMPAD_9 -> channel += 9
        }
        return channel
    }
}