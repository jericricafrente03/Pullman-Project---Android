package com.ph.bittelasia.libvlc.views.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import com.ph.bittelasia.libvlc.R
import com.ph.bittelasia.libvlc.control.annotation.PlayerSettings
import com.ph.bittelasia.libvlc.model.ScaleType
import com.ph.bittelasia.libvlc.model.VideoInfo
import org.videolan.libvlc.LibVLC
import org.videolan.libvlc.Media
import org.videolan.libvlc.MediaPlayer
import org.videolan.libvlc.util.VLCVideoLayout
import java.util.*
import kotlin.math.roundToInt

 abstract  class PlayerVLCFragment : PlayerFragment() {
    //----------------------------------------------------------------------------------------------
    //-----------------------------------------Instance---------------------------------------------
    private var playerTimer: Timer? = null
    private var delayTimer: Timer? = null
    private var playerTask: TimerTask? = null
    private var mVideoLayout: VLCVideoLayout? = null
    private var playerFrame: FrameLayout? = null
    private var mLibVLC: LibVLC? = null
    private var mMediaPlayer: MediaPlayer? = null
    private var scaleType: MediaPlayer.ScaleType? = null
    private var media: Media? = null
    private var playerSettings: PlayerSettings? = null
    private var position: Long = 0
    var delaySeconds: Long = 1300
    var uriPath: String? = null

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //========================================LifeCycle=============================================
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.surface_layout, container, false)
    }

    @SuppressLint("LongLogTag")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            Log.i(TAG, "@onViewCreated: start")
            initialize()
            mVideoLayout = view.findViewById(R.id.video_layout)
            playerFrame = view.findViewById(R.id.player_frame)
            if (onChangeListener == null) {
                throw RuntimeException("$TAG: OnChangeListener is not implemented")
            }
            Log.i(TAG, "@onViewCreated: end")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener!!.onFragmentDetached(this)
        onFragmentListener = null
    }

    override fun onDestroy() {
        super.onDestroy()
        try {
            mMediaPlayer!!.release()
            mLibVLC!!.release()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onResume() {
        super.onResume()
        try {
            mMediaPlayer!!.attachViews(mVideoLayout!!, null, ENABLE_SUBTITLES, USE_TEXTURE_VIEW)
            try {
                val fd = requireActivity().contentResolver.openFileDescriptor(Uri.parse(getPath()), "r")
                media = Media(mLibVLC, fd!!.fileDescriptor)
                mMediaPlayer!!.media = media
                mMediaPlayer!!.updateVideoSurfaces()
                mMediaPlayer!!.videoScale = scaleType().also { scaleType = it }
                media!!.setHWDecoderEnabled(true, true)
                preventDeadLock(playerSettings!!, media!!)
                media!!.release()
                uriPath = getPath()
            } catch (e: Exception) {
                e.printStackTrace()
            }
            mMediaPlayer!!.play()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onStop() {
        super.onStop()
        try {
            mMediaPlayer!!.stop()
            mMediaPlayer!!.detachViews()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==================================Method======================================================
    //----------------------------------------------------------------------------------------------
    @Synchronized
    fun play(source: Any?, isReplay: Boolean) {
        try {
            if(source!=null) {
                if (playerTimer == null) playerTimer = Timer()
                playerTask?.cancel()
                delayTimer?.cancel()
                delayTimer?.purge()
                playerTimer?.schedule(object : TimerTask() {
                    override fun run() {
                        activity?.runOnUiThread(Runnable {
                            if (mMediaPlayer != null) {
                                try {
                                    if (source is VideoInfo) {
                                        if(source.path!=null)
                                        if (uriPath?.contains(source.path!!) == true && !isReplay)
                                            return@Runnable
                                        uriPath = source.path
                                    } else if (source is String) {
                                        if (uriPath?.contains(source) == true && !isReplay) return@Runnable
                                        uriPath = source
                                    }
                                    stop()
                                    initialize()
                                    mMediaPlayer?.attachViews(
                                        mVideoLayout!!,
                                        null,
                                        ENABLE_SUBTITLES,
                                        USE_TEXTURE_VIEW
                                    )
                                    Log.i(TAG, "@attachingViews()")
                                    val fd = activity!!.contentResolver.openFileDescriptor(Uri.parse(uriPath), "r")
                                    media = Media(mLibVLC, fd!!.fileDescriptor)
                                    preventDeadLock(playerSettings!!, media!!)
                                    mMediaPlayer?.media = media
                                    mMediaPlayer?.updateVideoSurfaces()
                                    mMediaPlayer?.videoScale =
                                        scaleType().also { scaleType = it }
                                    media?.release()
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                if (playerSettings != null)
                                    try {
                                        if (playerSettings?.enableDelay == true) {
                                            onChangeListener?.onError("delaying video..")
                                            if (delayTimer != null) {
                                                delayTimer?.cancel()
                                                delayTimer?.purge()
                                                delayTimer = null
                                            }
                                            delayTimer = Timer()
                                            delayTimer?.schedule(object : TimerTask() {
                                                override fun run() {
                                                    try {
                                                        mMediaPlayer?.play()
                                                        playerTask?.cancel()
                                                        delayTimer?.cancel()
                                                        delayTimer?.purge()
                                                    } catch (e: Exception) {
                                                        e.printStackTrace()
                                                    }
                                                }
                                            }, delaySeconds, 10)
                                        } else {
                                            mMediaPlayer?.play()
                                            playerTask?.cancel()
                                            Log.i(TAG, "@play()")
                                        }
                                    }catch (e: Exception){
                                        e.printStackTrace()
                                    }

                            }
                        })
                    }
                }.also { playerTask = it }, 500)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Synchronized
    fun stop() {
        try {
            if (mMediaPlayer != null) {
                if(mMediaPlayer!!.isPlaying)
                    mMediaPlayer!!.stop()
                mMediaPlayer!!.detachViews()
                mMediaPlayer!!.release()
                if(mLibVLC!=null)
                   mLibVLC!!.release()
                Log.i(TAG, "@stop()")
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun initialize() {
        playerSettings = javaClass.getAnnotation(PlayerSettings::class.java)
        if (playerSettings == null) {
            throw RuntimeException("$TAG: PlayerSettings annotation is required")
        }
        val args = ArrayList<String>()
        if (playerSettings?.preventDeadLock == true) {
            args.add("--no-sub-autodetect-file")
            args.add("--swscale-mode=0")
            args.add("--network-caching=100")
            args.add("--no-drop-late-frames")
            args.add("--no-skip-frames")
        }
        args.add("-vvv")
        mLibVLC = LibVLC(requireContext(), args)
        mMediaPlayer = MediaPlayer(mLibVLC)
        mMediaPlayer?.setEventListener { event ->
            try {
                if (playerSettings?.showStatus == true)
                when (event.type) {
                    MediaPlayer.Event.MediaChanged -> onChangeListener!!.onStatus("@MediaChanged",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.Opening -> onChangeListener!!.onStatus(
                        "@Opening",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.Buffering -> onChangeListener!!.onBufferChanged(
                        event.buffering.roundToInt().toFloat()
                    )
                    MediaPlayer.Event.Playing -> onChangeListener!!.onStatus(
                        "@Playing",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.Paused -> onChangeListener!!.onStatus(
                        "@Paused",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.Stopped -> onChangeListener!!.onEnd()
                    MediaPlayer.Event.EndReached -> onChangeListener!!.onStatus(
                        "@EndReached",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.EncounteredError -> onChangeListener!!.onError(
                        "@EncounteredError:  $uriPath"
                    )
                    MediaPlayer.Event.TimeChanged -> {
                        position = event.timeChanged / 1000L
                        onChangeListener!!.onChanging(position)
                    }
                    MediaPlayer.Event.PositionChanged -> onChangeListener!!.onStatus(
                        Calendar.getInstance().time.toString() + " => " + uriPath,
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.SeekableChanged -> onChangeListener!!.onStatus(
                        "@SeekableChanged",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.LengthChanged -> onChangeListener!!.onStatus(
                        "@LengthChanged",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.Vout -> onChangeListener!!.onStatus(
                        "@Vout",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.ESAdded -> onChangeListener!!.onStatus(
                        "@ESAdded",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.ESDeleted -> onChangeListener!!.onStatus(
                        "@ESDeleted",
                        mMediaPlayer!!.isPlaying
                    )
                    MediaPlayer.Event.ESSelected -> onChangeListener!!.onStatus(
                        "@ESSelected",
                        mMediaPlayer!!.isPlaying
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        Log.i(TAG, "@initializing()")
    }

    @Synchronized
    fun refresh() {
        try {
            if (mMediaPlayer != null) {
                mMediaPlayer!!.updateVideoSurfaces()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun preventDeadLock(setting: PlayerSettings, media: Media) {
        try {
            if (setting.preventDeadLock) {
                media.addOption(":network-caching=100")
                media.addOption(":clock-jitter=0")
                media.addOption(":clock-synchro=0")
            }
            Log.i(TAG, "@preventDeadLock()")
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun scaleType(): MediaPlayer.ScaleType {
        return if (playerSettings != null) when (playerSettings!!.scaleType) {
            ScaleType.SURFACE_4_3 -> MediaPlayer.ScaleType.SURFACE_4_3
            ScaleType.SURFACE_16_9 -> MediaPlayer.ScaleType.SURFACE_16_9
            ScaleType.SURFACE_BEST_FIT -> MediaPlayer.ScaleType.SURFACE_BEST_FIT
            ScaleType.SURFACE_ORIGINAL -> MediaPlayer.ScaleType.SURFACE_ORIGINAL
            ScaleType.SURFACE_FIT_SCREEN -> MediaPlayer.ScaleType.SURFACE_FIT_SCREEN
            else -> MediaPlayer.ScaleType.SURFACE_FILL
        } else MediaPlayer.ScaleType.SURFACE_FILL
    }

    fun getmLibVLC(): LibVLC? {
        return mLibVLC
    }

    fun getmVideoLayout(): VLCVideoLayout? {
        return mVideoLayout
    }

    fun getmMediaPlayer(): MediaPlayer? {
        return mMediaPlayer
    }



    abstract fun getPath(): String
    //----------------------------------------------------------------------------------------------

    //==============================================================================================
    companion object {
        //=========================================Variable=============================================
        //-----------------------------------------Constant---------------------------------------------
        const val TAG = "VLC/PlayerFragment"
        private const val USE_TEXTURE_VIEW = false
        private const val ENABLE_SUBTITLES = true
    }
}