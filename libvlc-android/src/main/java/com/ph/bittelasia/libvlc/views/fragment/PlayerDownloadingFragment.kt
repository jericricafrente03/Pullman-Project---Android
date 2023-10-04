package com.ph.bittelasia.libvlc.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

abstract class PlayerDownloadingFragment : PlayerFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(setLayout(), container, false)
    }

    abstract fun setLayout(): Int
}