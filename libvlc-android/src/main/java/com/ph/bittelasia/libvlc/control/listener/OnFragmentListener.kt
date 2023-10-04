package com.ph.bittelasia.libvlc.control.listener

import androidx.annotation.NonNull
import androidx.fragment.app.Fragment

interface OnFragmentListener {
    fun onFragmentDetached(@NonNull fragment: Fragment)
}