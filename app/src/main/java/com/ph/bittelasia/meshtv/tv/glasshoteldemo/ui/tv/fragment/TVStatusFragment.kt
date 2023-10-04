package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment

import com.ph.bittelasia.libvlc.views.fragment.PlayerStatusFragment

class TVStatusFragment: PlayerStatusFragment() {

    override fun displayChannel(): Boolean {
        return false
    }
}