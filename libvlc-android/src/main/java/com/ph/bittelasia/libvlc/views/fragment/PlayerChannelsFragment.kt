package com.ph.bittelasia.libvlc.views.fragment

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ListView
import com.ph.bittelasia.libvlc.R
import com.ph.bittelasia.libvlc.control.adapter.VLCListAdapter
import com.ph.bittelasia.libvlc.control.exception.PlayerException
import com.ph.bittelasia.libvlc.model.VideoInfo
import java.util.*

@SuppressLint("LongLogTag")
abstract class PlayerChannelsFragment : PlayerFragment() {
    //-----------------------------------Getter-----------------------------------------------------
    //------------------------------------View------------------------------------------------------
    var drawerList: ListView? = null
        private set

    //----------------------------------Instance----------------------------------------------------
    private var openTimer: Timer? = null
    private var closeTimer: Timer? = null
    private var hideTimer: Timer? = null
    private var hideTimerTask: TimerTask? = null
    private var openTimerTask: TimerTask? = null
    private var closeTimerTask: TimerTask? = null
    private var indexPosition = 0
    private var shortAnimationDuration = 0

    var videoList: ArrayList<VideoInfo?>? = null
    var isDrawerOpen = false



    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==================================LifeCycle===================================================
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View?
    {
        return inflater.inflate(R.layout.list_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            draw(view)
        } catch (e: PlayerException) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener!!.onFragmentDetached(this)
        onFragmentListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.e(TAG, "@onDestroyView()")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.e(TAG, "@onDestroy()")
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //===================================Method=====================================================
    fun draw(v: View) {
        drawerList = v.findViewById(R.id.left_drawer)
        shortAnimationDuration = resources.getInteger(android.R.integer.config_shortAnimTime)
        if (drawerList != null) {
            if (adapter != null) {
                drawerList!!.adapter = adapter
                drawerList!!.visibility = View.GONE
                Log.d(TAG, "@Successfully drawn")
            }
        } else {
            Log.e(TAG, "@Drawer list is null")
        }
    }

    fun openDrawer(position: Int) {
        try {
            if (openTimer == null) openTimer = Timer()
            if (openTimerTask != null) openTimerTask!!.cancel()
            openTimer!!.schedule(object : TimerTask() {
                override fun run() {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            try {
                                if (drawerList != null) {
                                    if (drawerList!!.visibility == View.GONE) {
                                        drawerList!!.alpha = 0f
                                        drawerList!!.visibility = View.VISIBLE
                                        drawerList!!.animate()
                                            .alpha(1f)
                                            .setDuration(shortAnimationDuration.toLong())
                                            .setListener(null)
                                        indexPosition = position
                                        drawerList!!.setSelection(position)
                                        drawerList!!.smoothScrollToPosition(position)
                                        drawerList!!.adapter.getView(position, null, drawerList)
                                            .requestFocus()
                                        isDrawerOpen = true
                                        openTimerTask!!.cancel()
                                    }
                                }
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                    }
                }
            }.also { openTimerTask = it }, 200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun closeDrawer() {
        try {
            if (closeTimer == null) closeTimer = Timer()
            if (closeTimerTask != null) closeTimerTask!!.cancel()
            closeTimer!!.schedule(object : TimerTask() {
                override fun run() {
                    if (activity != null) {
                        activity!!.runOnUiThread {
                            if (drawerList!!.visibility == View.VISIBLE) {
                                drawerList!!.alpha = 0f
                                drawerList!!.visibility = View.VISIBLE
                                drawerList!!.animate()
                                    .alpha(0f)
                                    .setDuration(shortAnimationDuration.toLong())
                                    .setListener(object : AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation: Animator) {
                                            drawerList!!.visibility = View.GONE
                                        }
                                    })
                                isDrawerOpen = false
                            }
                        }
                    }
                }
            }.also { closeTimerTask = it }, 200)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun getClickedVideoItem(isUp: Boolean) {
        if (activity != null) if (hideTimer != null) {
            hideTimer!!.cancel()
            hideTimer!!.purge()
        }
        requireActivity().runOnUiThread(Runnable {
            try {
                if (!isUp) {
                    indexPosition++
                    //     1 < 5
                    if (indexPosition < videoList!!.size) {
                        drawerList!!.setItemChecked(indexPosition, true)
                    } else {
                        indexPosition = 0
                        drawerList!!.setItemChecked(indexPosition, true)
                    }
                } else {
                    indexPosition--
                    // -1 > 0
                    if (indexPosition >= 0) {
                        drawerList!!.setItemChecked(indexPosition, true)
                    } else {
                        indexPosition = videoList!!.size
                        drawerList!!.setItemChecked(indexPosition, true)
                    }
                }
                Log.i(TAG, "@getClickedVideoItem: $indexPosition")
                drawerList!!.setSelection(indexPosition)

                // getDrawerList().smoothScrollToPosition(indexPosition);
                onChangeListener?.onChannelIndex(indexPosition)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
        hideTimer = Timer()
        hideTimer!!.schedule(object : TimerTask() {
            override fun run() {
                closeDrawer()
            }
        }.also { hideTimerTask = it }, 5000)
    }

    //----------------------------------------------------------------------------------------------
    //-----------------------------------Setter-----------------------------------------------------
    fun setIndexPosition(indexPosition: Int) {
        this.indexPosition = indexPosition
    }

    fun setVideoList(videoList: ArrayList<VideoInfo?>?): PlayerChannelsFragment {
        this.videoList = videoList
        return this
    }

    fun getIndexPosition(): Int {
        return indexPosition
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==============================Abstract Methods================================================
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @get:Throws(PlayerException::class)
    abstract val adapter: VLCListAdapter?

    //==============================================================================================
    companion object {
        //==================================Variable====================================================
        //----------------------------------Constant----------------------------------------------------
        private const val TAG = "VLC/PlayerChannelsFragment"
    }
}