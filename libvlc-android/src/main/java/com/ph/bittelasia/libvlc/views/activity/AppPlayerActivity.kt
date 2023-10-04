package com.ph.bittelasia.libvlc.views.activity

import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ph.bittelasia.libvlc.control.annotation.ActivityFragment
import com.ph.bittelasia.libvlc.control.annotation.PlayerActivityLayout
import com.ph.bittelasia.libvlc.control.annotation.UpdateContents
import com.ph.bittelasia.libvlc.views.fragment.AppFragment
import java.lang.RuntimeException
import java.util.*

abstract class AppPlayerActivity : AppCompatActivity() {
    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(checkLayout(this))
            initializeObject(this)
            updateObject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        Log.i(TAG, "@onAttachFragment: " + fragment.javaClass.simpleName)
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private fun checkLayout(`object`: Any?): Int {
        if (`object` == null) {
            throw RuntimeException(this.javaClass.simpleName + "The object to layout is null")
        }
        val clazz: Class<*> = `object`.javaClass
        return if (!clazz.isAnnotationPresent(PlayerActivityLayout::class.java)) {
            throw RuntimeException(clazz.simpleName + " is not annotated with Layout")
        } else {
            Objects.requireNonNull(clazz.getAnnotation(PlayerActivityLayout::class.java)).value
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Throws(Exception::class)
    private fun initializeObject(`object`: Any) {
        val clazz: Class<*> = `object`.javaClass
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            if (field.isAnnotationPresent(ActivityFragment::class.java)) {
                val container = Objects.requireNonNull(
                    field.getAnnotation(
                        ActivityFragment::class.java
                    )
                ).containerID
                if (field[`object`] is AppFragment) {
                    val fragment = (field[`object`] as AppFragment)
                    attachFragment<AppFragment?>(fragment, container)
                } else {
                    throw RuntimeException("$TAG: instance is not a subclass of BasicFragment")
                }
            }
        }
    }

    @Throws(Exception::class)
    private fun updateObject(`object`: Any) {
        val clazz: Class<*> = `object`.javaClass
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(UpdateContents::class.java)) {
                method.isAccessible = true
                method.invoke(`object`)
            }
        }
    }

    private fun <T : AppFragment?> attachFragment(player: T, containerID: Int) {
        if (containerID == 0) if (player != null) {
            throw RuntimeException((player.tag?.javaClass?.simpleName  + " -> Must use non-zero containerViewId"))
        }
        supportFragmentManager.beginTransaction().replace(containerID, player!!, player.javaClass.simpleName).commitAllowingStateLoss()
    }

    companion object {
        private const val TAG = "AppPlayerActivity"
    }
}