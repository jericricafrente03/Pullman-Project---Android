package com.ph.bittelasia.libvlc.views.fragment

import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import coil.load
import com.ph.bittelasia.libvlc.R
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.squareup.picasso.Picasso
import java.util.*

abstract class PlayerStatusFragment : PlayerFragment() {
    private val TAG = PlayerStatusFragment::class.java.simpleName
    private var timer: Timer? = null
    private var parentLayout: ConstraintLayout? = null
    private var tvNo: TextView? = null
    private var tvTitle: TextView? = null
    private var tvStatus: TextView? = null
    private var iv_icon: ImageView? = null
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.status_view, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.setOnKeyListener(null)
        tvNo = view.findViewById(R.id.tv_no)
        tvStatus = view.findViewById(R.id.tv_status)
        tvTitle = view.findViewById(R.id.tv_name)
        iv_icon = view.findViewById(R.id.iv_icon)
        parentLayout = view.findViewById(R.id.cl_layout)
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener!!.onFragmentDetached(this)
        onFragmentListener = null
    }


    @SuppressLint("SetTextI18n")
   open fun updateStatus(activity: AppCompatActivity, source: Any?): PlayerStatusFragment {
        try {
            Log.i(TAG, "@updateStatus: displayChannel=" + displayChannel())

            if (!displayChannel()) {
                if (timer != null) {
                    timer!!.purge()
                    timer!!.cancel()
                }
                timer = Timer()
                timer?.schedule(object : TimerTask() {
                    override fun run() {
                        activity.runOnUiThread {
                            try {
                                tvNo?.text = ""
                                tvTitle?.text = ""
                                tvStatus?.text = ""
                                iv_icon?.setImageResource(0)
                                parentLayout?.visibility = View.GONE
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        timer?.purge()
                        timer?.cancel()
                    }
                }, 5000)
            }
            activity.runOnUiThread {
                try {
                    if (source != null) {
                        if (source is VideoInfo) {
                            tvNo?.text = source.channelNo.toString() + ""
                            tvTitle?.text = source.name + ""
                            tvStatus?.text = source.description + ""
                            if (iv_icon != null) {
                                val uri = Uri.parse(source.icon)
                                iv_icon?.load(uri)
                            }
                        } else if (source is String) {
                            tvNo?.text = source
                        }
                        parentLayout?.visibility = View.VISIBLE
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return this
    }

    abstract fun displayChannel(): Boolean
}