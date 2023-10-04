package com.ph.bittelasia.libvlc.control.annotation

import com.ph.bittelasia.libvlc.model.ScaleType

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PlayerSettings(
    val scaleType: ScaleType,
    val preventDeadLock: Boolean,
    val enableDelay: Boolean,
    val showStatus: Boolean
)