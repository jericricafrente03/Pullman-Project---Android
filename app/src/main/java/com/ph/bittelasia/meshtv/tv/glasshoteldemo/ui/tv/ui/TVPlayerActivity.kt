package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.ui

import android.Manifest
import android.annotation.SuppressLint
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.MotionEvent
import androidx.annotation.MainThread
import androidx.annotation.NonNull
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.ph.bittelasia.libvlc.control.annotation.AttachPlayerFragment
import com.ph.bittelasia.libvlc.control.annotation.PlayerActivityLayout
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.ph.bittelasia.libvlc.views.activity.AbstractPlayerActivity
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data.ProjectData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.db.GlassDB
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.repository.Repository
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment.TVChannelListFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment.TVLoaderFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment.TVPlayerFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment.TVStatusFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model.ChannelData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.util.KCustomToast
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.GlassViewModel
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.ViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

@SuppressLint("NonConstantResourceId")
@PlayerActivityLayout(value = R.layout.tv_player_activity_layout)
class TVPlayerActivity : AbstractPlayerActivity(), TVChannelListFragment.ChannelChangeCallBack {
    private lateinit var myViewModel: GlassViewModel

    companion object {
        var isTVOpen = false
        var name: String = this::class.java.simpleName
    }

    private var position = 0
    private var monitorIndexTimer: Timer? = null
    private var monitorPlayerTimer: Timer? = null
    private var monitorPlayerMessage: String? = null
    private var channelList: List<ChannelData>? = null
    private var channel: ChannelData? = null

    @AttachPlayerFragment(R.id.player_layout)
    val playerFragment = TVPlayerFragment()

    @AttachPlayerFragment(R.id.status_layout)
    val statusFragment = TVStatusFragment()

    @AttachPlayerFragment(R.id.list_layout)
    val channelListFragment = TVChannelListFragment()

    @AttachPlayerFragment(R.id.progress_layout)
    val progressFragment = TVLoaderFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        var monitorPlayerIndex: Int = 0

        CoroutineScope(Dispatchers.Main).launch {
            launch {
                val data = ProjectData()
                val repo = Repository(data, GlassDB.db(this@TVPlayerActivity), this@TVPlayerActivity)
                val factoryVm = ViewModelFactory(repo)
                myViewModel = ViewModelProvider(this@TVPlayerActivity, factoryVm)[GlassViewModel::class.java]
                checkStorageAccessPermission()
                myViewModel.tvList.observe(this@TVPlayerActivity) { dataList ->
                    channelList = dataList.data
                    download(channelList)
                }
            }
        }
        monitorPlayerTimer = Timer()
        monitorPlayerTimer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    try {
                        if (monitorPlayerMessage.equals(Calendar.getInstance().time.toString()))
                            monitorPlayerIndex++
                        if (monitorPlayerIndex >= 4) {
                            onEnd()
                            monitorPlayerIndex = 0
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }, 100, 5000)
    }

    @MainThread
    fun download(@NonNull list: List<*>?) {
        val intent = intent
        val id = intent.getStringExtra("channel")
        val videos = mutableListOf<VideoInfo>()
        if (list?.isEmpty() == false) {
            for (ch in list) {
                if (ch is ChannelData) {
                    val videoInfo = VideoInfo()
                    val order = ch.id
                    val title = ch.channelTitle
                    val path = ch.channelUri
                    val desc = ch.channelDescription
                    val cat = ch.channelCategoryId
                    val icon = ch.channelImage

                    videoInfo.channelNo = order.toInt()
                    videoInfo.name = title + ""
                    videoInfo.path = path + ""
                    videoInfo.description = desc + ""
                    videoInfo.categoryID = cat.toInt()
                    videoInfo.icon = icon
                    videos += videoInfo
                }
            }
            if (videos.isNotEmpty()) {
                loadFragments(ArrayList(videos))
                playerFragment.play(videos[id!!.toInt()], true)
                statusFragment.updateStatus(this, videos[id.toInt()])
                channelListFragment.videoList = videos
            }
        }
    }

    override fun onStart() {
        super.onStart()
        KCustomToast.infoToast(this)
        isTVOpen = true
    }

    override fun onStop() {
        super.onStop()
        isTVOpen = false
    }

    override fun onPause() {
        super.onPause()
        isTVOpen = false
        monitorIndexTimer?.cancel()
        monitorIndexTimer?.purge()
        monitorPlayerTimer?.cancel()
        monitorPlayerTimer?.purge()
    }

    override fun onDestroy() {
        super.onDestroy()
        isTVOpen = false
    }

    override fun onResume() {
        super.onResume()
        if (videoList.size > 0)
            statusFragment.updateStatus(this, videoList[position])
    }

    override fun dispatchKeyEvent(event: KeyEvent): Boolean {
        try {
            Log.i(TAG, "@dispatchKeyEvent: " + KeyEvent.keyCodeToString(event.keyCode))
            when (event.action) {
                KeyEvent.ACTION_DOWN ->
                    when (event.keyCode) {
                        KeyEvent.KEYCODE_NUMPAD_0,
                        KeyEvent.KEYCODE_NUMPAD_1,
                        KeyEvent.KEYCODE_NUMPAD_2,
                        KeyEvent.KEYCODE_NUMPAD_3,
                        KeyEvent.KEYCODE_NUMPAD_4,
                        KeyEvent.KEYCODE_NUMPAD_5,
                        KeyEvent.KEYCODE_NUMPAD_6,
                        KeyEvent.KEYCODE_NUMPAD_7,
                        KeyEvent.KEYCODE_NUMPAD_8,
                        KeyEvent.KEYCODE_NUMPAD_9,
                        KeyEvent.KEYCODE_DPAD_CENTER,
                        KeyEvent.KEYCODE_ENTER,
                        KeyEvent.KEYCODE_NUMPAD_ENTER,
                        KeyEvent.KEYCODE_DPAD_RIGHT,
                        KeyEvent.KEYCODE_DPAD_LEFT,
                        KeyEvent.KEYCODE_MENU,
                        KeyEvent.KEYCODE_POWER,
                        KeyEvent.KEYCODE_SETTINGS,
                        KeyEvent.META_SHIFT_LEFT_ON -> return true
                        KeyEvent.KEYCODE_PAGE_UP,
                        KeyEvent.KEYCODE_CHANNEL_UP,
                        KeyEvent.KEYCODE_DPAD_UP -> {
                            channelListFragment.getClickedVideoItem(true)
                            return true
                        }
                        KeyEvent.KEYCODE_PAGE_DOWN,
                        KeyEvent.KEYCODE_CHANNEL_DOWN,
                        KeyEvent.KEYCODE_DPAD_DOWN -> {
                            channelListFragment.getClickedVideoItem(false)
                            return true
                        }
                        else -> onChannelChanged(
                            channelNumber(
                                event,
                                playerFragment
                            )
                        )
                    }
                KeyEvent.ACTION_UP -> when (event.keyCode) {
                    KeyEvent.KEYCODE_MENU -> {
                        if (!channelListFragment.isDrawerOpen) {
                            channelListFragment.openDrawer()
                        } else {
                            channelListFragment.closeDrawer()
                        }
                        return true
                    }
                    KeyEvent.KEYCODE_PAGE_UP,
                    KeyEvent.KEYCODE_CHANNEL_UP,
                    KeyEvent.KEYCODE_DPAD_UP -> {
                        if (!channelListFragment.isDrawerOpen)
                            onChannelChanged(channelTune(true))
                        return true
                    }
                    KeyEvent.KEYCODE_PAGE_DOWN,
                    KeyEvent.KEYCODE_CHANNEL_DOWN,
                    KeyEvent.KEYCODE_DPAD_DOWN -> {
                        if (!channelListFragment.isDrawerOpen)
                            onChannelChanged(channelTune(false))
                        return true
                    }
                    KeyEvent.KEYCODE_DPAD_CENTER,
                    KeyEvent.KEYCODE_ENTER,
                    KeyEvent.KEYCODE_NUMPAD_ENTER -> {
                        try {
                            channelIndex = position
                            videoList[position].let { playerFragment.play(it, false) }
                            statusFragment.updateStatus(this, videoList[position])
                            channelListFragment.closeDrawer()
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return true
                    }
                    KeyEvent.META_SHIFT_LEFT_ON -> {
                        try {
                            val i = Intent()
                            i.action = "android.intent.action.LAUNCH.SETTING"
                            sendBroadcast(i)
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                        return true
                    }
                }
                else -> {
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return super.dispatchKeyEvent(event)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        when (event!!.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                Log.i(TAG, "@onTouchEvent -> action down ")
            }
            MotionEvent.ACTION_UP -> {
                Log.i(TAG, "@onTouchEvent -> action up ")
                try {
                    channelIndex = position
                    videoList.apply {
                        this[position].let {
                            playerFragment.play(it, false)
                            statusFragment.updateStatus(this@TVPlayerActivity, it)
                        }
                    }
                    channelListFragment.closeDrawer()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        return super.onTouchEvent(event)
    }

    override fun onBackPressed() {
        super.onBackPressed()
        try {
            this.finish()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onBufferChanged(buffer: Float) {
        runOnUiThread {
            if (buffer >= 100.0) {
                setCounter(0)
                progressFragment.hideProgress(true)
            } else {
                progressFragment.hideProgress(false)
            }
            Log.i(TAG, "@onBufferChanged: $buffer")
        }
    }

    override fun onChannelIndex(channelIndex: Int) {
        var monitorIndex = 0
        val tempPosition = position
        position = channelIndex
        monitorIndexTimer?.purge()
        monitorIndexTimer?.cancel()
        monitorIndexTimer = Timer()
        monitorIndexTimer?.schedule(object : TimerTask() {
            override fun run() {
                runOnUiThread {
                    try {
                        if (position == tempPosition)
                            monitorIndex++
                        if (monitorIndex >= 4) {
                            monitorIndexTimer?.purge()
                            monitorIndexTimer?.cancel()
                            channelListFragment.closeDrawer()
                            monitorIndex = 0
                        }
                        Log.d(TAG, "@onChannelIndex: monitorIndex $monitorIndex")
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }, 1000, 5000)
        Log.i(TAG, "@onChannelIndex:  ${videoList[channelIndex].name}")
    }

    override fun onChanging(seconds: Long) {
        setSeconds(seconds)
    }

    override fun onStatus(message: String?, isPlaying: Boolean) {
        try {
            if (isPlaying)
                monitorPlayerMessage = message
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
    }

    override fun onChannelChanged(`object`: Any?) {
        if (`object` != null)
            Log.d(TAG, "@onChannelChanged()")
        if (!channelListFragment.isDrawerOpen) {
            if (`object` is VideoInfo) {
                try {
                    channel = channelList?.find {
                        it.id.toInt() == `object`.channelNo
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                playerFragment.play(`object`, false)
            }
            statusFragment.updateStatus(this, `object`)
        }

    }

    override fun onError(msg: String?) {
        progressFragment.hideProgress(false)
    }

    override fun onEnd() {
        progressFragment.hideProgress(false)
        playerFragment.stop()
        playerFragment.play(playerFragment.getPath(), true)
        Log.d(TAG, "@onEnd()")
    }
    private fun checkStorageAccessPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                )
            ) {
                AlertDialog.Builder(this)
                    .setTitle("Permission Needed")
                    .setMessage("This permission is needed to access media file in your phone")
                    .setPositiveButton("OK") { _, _ ->
                        ActivityCompat.requestPermissions(
                            this,
                            arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                            1
                        )
                    }
                    .setNegativeButton(
                        "CANCEL"
                    ) { dialog, _ -> dialog.dismiss() }.show()
            } else {
                ActivityCompat.requestPermissions(
                    this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    1
                )
            }
        } else {
            // Permission has already been granted
            // Do nothing. Because if permission is already granted then files will be accessed/loaded in splash_screen_activity
        }
    }

}