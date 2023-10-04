package com.ph.bittelasia.libvlc.control.listener

interface OnPermissionListener {
    fun onPermissionGranted()
    fun onPermissionDenied()
    fun onPermissionAlreadyGranted()
}