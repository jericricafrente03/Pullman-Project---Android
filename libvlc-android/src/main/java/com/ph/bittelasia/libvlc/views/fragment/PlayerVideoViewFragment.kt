package com.ph.bittelasia.libvlc.views.fragment

import android.media.MediaPlayer
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.MediaController
import android.widget.VideoView
import androidx.annotation.UiThread
import com.ph.bittelasia.libvlc.R
import java.util.*
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit

abstract class PlayerVideoViewFragment : PlayerFragment(), MediaPlayer.OnCompletionListener,
    MediaPlayer.OnErrorListener, MediaPlayer.OnPreparedListener {
    //------------------------------View------------------------------------------------------------
    private var videoView: VideoView? = null
    private var mediaControls: MediaController? = null

    //-----------------------------Instance---------------------------------------------------------
    private var counter = 0
    var name: String? = null
    var path: String?=null

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //=================================LifeCycle====================================================
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.video_view_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        draw(view)
        videoPosition
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener!!.onFragmentDetached(this)
        onFragmentListener = null
    }

    override fun onResume() {
        super.onResume()
        try {
            val uri = Uri.parse(path)
            videoView!!.setVideoURI(uri)
            videoView!!.requestFocus()
            videoView!!.start()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //=============================MediaPlayerListeners=============================================
    //----------------------------------------------------------------------------------------------
    override fun onError(mp: MediaPlayer, what: Int, extra: Int): Boolean {
        return true
    }

    override fun onCompletion(mp: MediaPlayer) {
        mp.isPlaying
    }

    override fun onPrepared(mp: MediaPlayer) {
        mp.isLooping = true
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //===================================Method=====================================================
    //----------------------------------------------------------------------------------------------
    fun draw(view: View) {
        try {
            videoView = view.findViewById(R.id.vv_video)
            videoView?.setOnCompletionListener(this)
            videoView?.setOnErrorListener(this)
            videoView?.setOnPreparedListener(this)
            if (mediaControls == null) {
                mediaControls = MediaController(context)
                mediaControls!!.setAnchorView(videoView)
            }
            if (isHasControl) {
                videoView?.setMediaController(mediaControls)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @get:UiThread
    val videoPosition: Unit
        get() {
            try {
                val mScheduledExecutorService: ScheduledExecutorService =
                    ScheduledThreadPoolExecutor(1)
                mScheduledExecutorService.scheduleWithFixedDelay({
                    videoView!!.post {
                        if (videoView != null) {
                            val mCurrentposition = videoView!!.currentPosition
                            if (onChangeListener != null) {
                                if (mCurrentposition == 0) {
                                    onChangeListener!!.onError(Calendar.getInstance().time.toString() + "; position -> " + mCurrentposition + ", uri = " + path + ", title = " + name)
                                    counter++
                                    if (counter >= 3) onChangeListener!!.onEnd()
                                } else {
                                    counter = 0
                                    onChangeListener!!.onStatus(
                                        Calendar.getInstance().time.toString() + "; position -> " + mCurrentposition + ", uri = " + path + ", title = " + name,
                                        videoView!!.isPlaying
                                    )
                                }
                            }
                        }
                    }
                }, 3000, 1000, TimeUnit.MILLISECONDS)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================

    companion object {
        //=============================Variable=========================================================
        //-----------------------------Constant---------------------------------------------------------
        private const val TAG = "VLC/PlayerVideoViewFragment"
    }
}