package com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils

import android.net.Uri
import android.widget.ImageView
import androidx.databinding.BindingAdapter
import coil.load

class ViewBinding {
    companion object {
        @BindingAdapter(value = ["app:imageDefault"], requireAll = false)
        @JvmStatic
        fun ImageView.imageSelection(imageDefault : String?){
            load(Uri.parse(imageDefault))
        }
    }
}