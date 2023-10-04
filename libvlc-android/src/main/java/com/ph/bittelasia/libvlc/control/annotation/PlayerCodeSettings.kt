package com.ph.bittelasia.libvlc.control.annotation

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@kotlin.annotation.Retention(AnnotationRetention.RUNTIME)
annotation class PlayerCodeSettings(
        val channelUPCodes : IntArray,
        val channelDownCodes : IntArray,
        val channelOKCodes : IntArray,
        val channelMenuCode : Int
)
