package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.adapter

import android.graphics.Color
import android.text.Html
import android.view.*
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.request.CachePolicy
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseActivity
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.ChannelListViewItemRowBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChannelAdapter(private val cb: (Int) -> Unit) : ListAdapter<VideoInfo, ChannelAdapter.ViewHolder>(
    ChannelComparator()
) {
    var selectedPosition = 0
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ChannelListViewItemRowBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return ViewHolder(binding, cb)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)
        if (current != null) {
            holder.bind(current)
        }
    }

    inner class ViewHolder(
        private var binding: ChannelListViewItemRowBinding,
        private val cb: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(data: VideoInfo) {
            CoroutineScope(Dispatchers.Main).launch {
                launch {
                    binding.apply {
                        ivIcon.load(data.icon) {
                            memoryCachePolicy(CachePolicy.ENABLED)
                        }
                        tvChannel.text = Html.fromHtml("${data.channelNo}  ${data.name}")
                        root.setOnClickListener {
                            cb.invoke(adapterPosition)
                            it.requestFocus()
                        }
                        root.setOnHoverListener { view, motionEvent ->
                            when (motionEvent!!.actionMasked) {
                                MotionEvent.ACTION_HOVER_ENTER -> {
                                    view.isSelected = true
                                    view.requestFocus()
                                }
                                MotionEvent.ACTION_HOVER_EXIT -> {
                                    view.isSelected = false
                                }
                            }
                            false
                        }
                        root.setOnTouchListener { _, motionEvent ->
                            when (motionEvent!!.action and MotionEvent.ACTION_MASK) {
                                MotionEvent.ACTION_UP -> {
                                    binding.root.performClick()
                                }
                            }
                            false
                        }
                        root.onFocusChangeListener = View.OnFocusChangeListener { view, b ->
                            if (b) {
                                view.setBackgroundColor(Color.parseColor("#FA3E9301"))
                            } else {
                                view.setBackgroundColor(Color.TRANSPARENT)
                            }
                        }
                        root.setOnKeyListener { _, _, keyEvent ->
                            BaseActivity.Debug.log(
                                this,
                                "@dispatchKeyEvent: " + KeyEvent.keyCodeToString(keyEvent.keyCode)
                            )
                            false
                        }
                        if (adapterPosition == selectedPosition)
                            binding.root.requestFocus()
                    }
                }
            }
        }
    }

    class ChannelComparator : DiffUtil.ItemCallback<VideoInfo>() {
        override fun areItemsTheSame(
            oldItem: VideoInfo,
            newItem: VideoInfo
        ) = oldItem.channelNo == newItem.channelNo

        override fun areContentsTheSame(
            oldItem: VideoInfo,
            newItem: VideoInfo
        ) = oldItem.channelNo == newItem.channelNo
    }
}