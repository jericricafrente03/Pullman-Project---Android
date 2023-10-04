package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment

import com.ph.bittelasia.libvlc.control.annotation.PlayerSettings
import com.ph.bittelasia.libvlc.model.ScaleType
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.ph.bittelasia.libvlc.views.fragment.PlayerVLCFragment

@PlayerSettings(
    scaleType = ScaleType.SURFACE_FILL,
    preventDeadLock = true,
    enableDelay = false,
    showStatus = true
)
class TVPlayerFragment: PlayerVLCFragment() {

    private var source: Any? = null

    override fun getPath(): String {
        var uri = "udp://@238.0.0.5:1234"
        if (source != null) {
            if (source is String)
                uri = source as String
            else if (source is VideoInfo)
                uri = (source as VideoInfo).path!!
        }
        return uri
    }
}