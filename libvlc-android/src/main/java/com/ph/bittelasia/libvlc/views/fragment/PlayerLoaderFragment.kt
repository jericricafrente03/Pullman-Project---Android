package com.ph.bittelasia.libvlc.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.view.isVisible
import com.ph.bittelasia.libvlc.R

abstract class PlayerLoaderFragment : PlayerFragment() {

    private val TAG = "VLC/PlayerLoaderFragment"

    private var container: LinearLayout? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.loading_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnKeyListener(null)
        container = view.findViewById(R.id.ll_loading)
        isRemoved = false
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener?.onFragmentDetached(this)
        onFragmentListener = null
        isRemoved = true
    }

    fun hideProgress(hide: Boolean) {
        requireActivity().runOnUiThread {
            if (container != null) {
                if (hide) {
                    if(container?.isVisible == true)
                      container?.visibility = View.GONE
                } else {
                    if(container?.isVisible == false)
                      container?.visibility = View.VISIBLE
                }
            }
        }
    }
}