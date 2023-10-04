package com.ph.bittelasia.libvlc.control.listener

import com.ph.bittelasia.libvlc.model.VideoInfo

/**
 * Interface class that has the following methods
 * @author Steward M. Apostol
 * @since 2020-05-16
 */
interface OnPlayerListener {
    fun playerLoad(message: String)
    fun playerStopped(videoInfo: VideoInfo?, message: String)
}