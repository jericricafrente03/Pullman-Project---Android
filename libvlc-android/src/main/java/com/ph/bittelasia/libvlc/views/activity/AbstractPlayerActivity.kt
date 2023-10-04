package com.ph.bittelasia.libvlc.views.activity

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.view.KeyEvent
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ph.bittelasia.libvlc.R
import com.ph.bittelasia.libvlc.control.ActivityControl.checkPermission
import com.ph.bittelasia.libvlc.control.ActivityControl.getInput
import com.ph.bittelasia.libvlc.control.annotation.AttachPlayerFragment
import com.ph.bittelasia.libvlc.control.annotation.PlayerActivityLayout
import com.ph.bittelasia.libvlc.control.annotation.UpdateContents
import com.ph.bittelasia.libvlc.control.listener.OnChangeListener
import com.ph.bittelasia.libvlc.control.listener.OnFragmentListener
import com.ph.bittelasia.libvlc.control.listener.OnPermissionListener
import com.ph.bittelasia.libvlc.model.Channels
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.ph.bittelasia.libvlc.views.fragment.PlayerFragment
import com.ph.bittelasia.libvlc.views.fragment.PlayerVLCFragment
import java.util.*
import kotlin.collections.ArrayList

abstract class AbstractPlayerActivity : AppCompatActivity(), OnChangeListener, OnFragmentListener, OnPermissionListener {

    var videoList: ArrayList<VideoInfo> = arrayListOf()
    var isHasControl = false
    var channelIndex = 0
    var isRestarted = false

    private var playerFragment: PlayerFragment? = null
    private var monitor = 0
    private var timerMonitor: Timer? = null
    private var timerChannel: Timer? = null
    private var timerTaskMonitor: TimerTask? = null
    private var timerTaskChannel: TimerTask? = null
    private var counter: Long = 0
    private var seconds: Long = 0
    private val stats: Long = 0
    private var channelNo = ""


    //==============================================================================================
    override fun onStart() {
        super.onStart()
        Log.i(TAG, "@onStart()")
    }

    override fun onRestart() {
        super.onRestart()
        isRestarted = true
        Log.i(TAG, "@onRestart()")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            setContentView(checkLayout(this))
            updateObject(this)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        Log.i(TAG, "@onCreate: ")
    }

    override fun onResume() {
        super.onResume()
        Log.i(TAG, "@onResume: ")
    }

    override fun onPause() {
        super.onPause()
        timerTaskMonitor?.cancel()
        timerTaskChannel?.cancel()
        timerMonitor = null
        timerChannel = null
        Log.i(TAG, "@onPause()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.i(TAG, "@onDestroy()")
    }

    override fun onStop() {
        super.onStop()
        Log.i(TAG, "@onStop()")
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray)
    {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                onPermissionGranted()
            } else {
                onPermissionDenied()
            }
        }
    }

    override fun onAttachFragment(fragment: Fragment) {
        super.onAttachFragment(fragment)
        if (fragment is PlayerFragment) {
            playerFragment = fragment
            if (playerFragment is PlayerVLCFragment) {
                if(videoList.size>0)
                 (playerFragment as PlayerVLCFragment).play(videoList[0], false)
            }
            Log.i(TAG, "@onAttachFragment: " + playerFragment?.tag)
        }
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //=================================OnFragmentListener===========================================
    //----------------------------------------------------------------------------------------------
    override fun onFragmentDetached(fragment: Fragment) {
        if (fragment is PlayerFragment) playerFragment = fragment
        Log.i(TAG, "@onFragmentDetached: " + playerFragment!!.tag)
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==============================OnPermissionListener============================================
    //----------------------------------------------------------------------------------------------
    override fun onPermissionGranted() {
        Log.i(TAG, "@onRequestPermissionsResult: " + "Storage Permission Granted")
    }

    override fun onPermissionDenied() {
        Log.e(TAG, "@onRequestPermissionsResult: " + "Storage Permission Denied")
    }

    override fun onPermissionAlreadyGranted() {}

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //=====================================Method===================================================
    //----------------------------------------------------------------------------------------------
    fun loadFragments(channels : ArrayList<VideoInfo>) {
        try {
            videoList.addAll(channels)
            channelIndex = 0
            onChannelIndex(0)
            initializeObject(this)
            Log.i(TAG, "@loadedFragments")
//            playerMonitor()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun attachFragment(fragment: Fragment, containerID: Int) {
       runOnUiThread {
           try {
               if (containerID == 0) throw RuntimeException(fragment.tag?.javaClass?.simpleName + " -> Must use non-zero containerViewId")
               supportFragmentManager.beginTransaction().replace(
                   containerID,
                   fragment,
                   fragment.javaClass.simpleName
               ).commitAllowingStateLoss()
           }catch (e : Exception){
               e.printStackTrace()
           }
       }
    }

    open fun removeFragment(fragment: Fragment){
        runOnUiThread {
            try {
                supportFragmentManager.beginTransaction().remove(
                    fragment
                ).commitAllowingStateLoss()
            }catch (e : Exception){
                e.printStackTrace()
            }
        }

    }

    private fun <T : PlayerFragment?> attachFragment(player: T, containerID: Int) {
        if (containerID == 0) throw RuntimeException(player?.tag?.javaClass?.simpleName + " -> Must use non-zero containerViewId")
        supportFragmentManager.beginTransaction().replace(
                containerID,
                player!!,
                player.javaClass.simpleName
        ).commitAllowingStateLoss()
    }

    fun <T : PlayerVLCFragment?> removePlayer(player: T) {
        try {
            supportFragmentManager.beginTransaction().remove(player!!).commitAllowingStateLoss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(Exception::class)
    private fun initializeObject(`object`: Any) {
        val clazz: Class<*> = `object`.javaClass
        for (field in clazz.declaredFields) {
            field.isAccessible = true
            if (field.isAnnotationPresent(AttachPlayerFragment::class.java)) {
                val container = Objects.requireNonNull(
                        field.getAnnotation(
                                AttachPlayerFragment::class.java
                        )
                ).containerID
                if(container==0)
                {
                    val fragment = (field[`object`] as Fragment)
                    attachFragment(fragment, container)
                }
                else if (field[`object`] is PlayerFragment)
                {
                    val fragment = (field[`object`] as PlayerFragment)
                    attachFragment<PlayerFragment?>(fragment, container)
                }
                else
                {
                    val fragment = (field[`object`] as Fragment)
                    attachFragment(fragment, container)
                }
            }
        }
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

    @Throws(Exception::class)
    private fun updateObject(`object`: Any) {
        val clazz: Class<*> = `object`.javaClass
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(UpdateContents::class.java)) {
                method.isAccessible = true
                try {
                    method.invoke(`object`)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * This method is used to change channel using up and down keypad
     * @param pageUp is Boolean, to identify whether the key is up or not
     * @return VideoInfo
     */
    fun channelTune(pageUp: Boolean): VideoInfo? {
        var video: VideoInfo? = null
        try {
            if (pageUp) {
                channelIndex++
                // 1 < 5
                if (channelIndex < videoList!!.size) {
                    video = videoList!![channelIndex]
                } else {
                    video = videoList!![0]
                    channelIndex = 0
                }
            } else {
                channelIndex--
                // -1 > 0
                if (channelIndex >= 0) {
                    video = videoList!![channelIndex]
                } else {
                    video = videoList!![videoList!!.size - 1]
                    channelIndex = videoList!!.size
                }
            }
            onChannelIndex(channelIndex)
            Log.d(TAG, "@channelTune: source = " + video!!.path + ";  no = " + video.channelNo)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return video
    }

    fun channelNumber(event: KeyEvent?, player: PlayerVLCFragment): String {
        channelNo += getInput(event!!)
        timerChannel?.purge()
        timerChannel?.cancel()
        timerChannel = null
        timerChannel = Timer()
        timerChannel?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    var info: VideoInfo? = null
                    if(videoList!=null)
                    for (x in videoList!!.indices) {
                        val no = videoList!![x]!!.channelNo.toString() + ""
                        info = videoList!![x]
                        if (no == channelNo) {
                            channelIndex = x
                            timerChannel?.purge()
                            timerChannel?.cancel()
                            onChannelChanged(info)
                            break
                        }
                    }
                    channelNo = ""
                }
            }
        }.also { timerTaskChannel = it }, 3000)
        return channelNo
    }

    /**
     * This method is used to monitor the status of the source
     */
    private fun playerMonitor() {
        val count = 0
        if (timerMonitor == null)
            timerMonitor = Timer()
        timerMonitor?.scheduleAtFixedRate(object : TimerTask() {
            override fun run() {
                try {
                    if (playerFragment != null) {
                        if (seconds < counter - 2) {
                            if (playerFragment is PlayerVLCFragment)
                                if (!(playerFragment as PlayerVLCFragment).getmMediaPlayer()?.isPlaying!!) {
                                    monitor++
                                    onStatus(
                                        Calendar.getInstance().time.toString() + " => " + (playerFragment as PlayerVLCFragment).uriPath + "",
                                        false
                                    )
                                    Log.i("meme","if playerFragment -> ${(playerFragment as PlayerVLCFragment).uriPath}")
                                    Log.i("meme","if playerFragment -> $monitor")
                                } else {
                                    monitor = 0
                                }
                        } else {
                            monitor = 0
                            Log.i("meme","else playerFragment -> $monitor")
                        }
                    }
                    counter++
                    Log.i("meme","playerFragment -> $playerFragment")

                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.also { timerTaskMonitor = it }, 0, 1000)
    }

    fun setTVPreference(info: VideoInfo) {
        try {
            val editor = getSharedPreferences("TVPLAYERPreference", Context.MODE_PRIVATE).edit()
            editor.putInt("channelNo", info.channelNo)
            editor.putString("path", info.path + "")
            editor.putString("name", info.name + "")
            editor.putString("description", info.description + "")
            editor.putString("icon", info.icon + "")
            editor.apply()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun setCounter(counter: Long) {
        this.counter = counter
    }

    fun setSeconds(seconds: Long) {
        this.seconds = seconds
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    companion object {
        //=================================Variable=====================================================
        //---------------------------------Constant-----------------------------------------------------
        const val TAG = "VLC/TVActivity"
        const val KEY = "serializable"
        const val CHANNEL_NO = "channel_no"
        private const val STORAGE_PERMISSION_CODE = 101
    }
}