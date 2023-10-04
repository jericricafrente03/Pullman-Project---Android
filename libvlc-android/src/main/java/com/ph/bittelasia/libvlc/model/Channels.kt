package com.ph.bittelasia.libvlc.model

import android.os.Parcel
import android.os.Parcelable
import java.util.*

class Channels : Parcelable {
    var list: ArrayList<VideoInfo>?
        private set

    constructor(list: ArrayList<VideoInfo>?) {
        this.list = list
    }

    protected constructor(`in`: Parcel) {
        list = `in`.readArrayList(null) as ArrayList<VideoInfo>?
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(parcel: Parcel, i: Int) {
        parcel.writeList(list)
    }

    fun sort(): Channels {
        list?.sortWith(compareBy { it.channelNo })
        return this
    }

    companion object {
        val CREATOR: Parcelable.Creator<Channels?> = object : Parcelable.Creator<Channels?> {
            override fun createFromParcel(`in`: Parcel): Channels {
                return Channels(`in`)
            }

            override fun newArray(size: Int): Array<Channels?> {
                return arrayOfNulls(size)
            }
        }
    }
}