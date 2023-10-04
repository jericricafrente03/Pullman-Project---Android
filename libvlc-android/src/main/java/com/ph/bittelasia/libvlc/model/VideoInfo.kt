package com.ph.bittelasia.libvlc.model

import java.io.Serializable

class VideoInfo() : Serializable, Comparable<VideoInfo> {
    var channelNo: Int = 0
    var categoryID: Int = 0
    var path: String? = null
    var name: String? = null
    var description: String? = null
    var icon: String? = null
    var isEnabled: Boolean=  false

    override fun toString(): String {
        return ("VideoInfo [channelNo=" + channelNo
                + ", categoryID=" + categoryID
                + ", path=" + path
                + ", name=" + name
                + ", description=" + description
                + ", icon=" + icon
                + ", enabled=" + isEnabled
                + "]")
    }

    override fun compareTo(other: VideoInfo): Int {
        return channelNo - other.channelNo
    }
}