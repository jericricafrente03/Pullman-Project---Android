package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.fragment

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.adapter.ChannelAdapter
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.util.toFile
import com.ph.bittelasia.libvlc.control.listener.OnChangeListener
import com.ph.bittelasia.libvlc.model.VideoInfo
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.ChannelListLayoutBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*

class TVChannelListFragment : BaseFragment<ChannelListLayoutBinding>() {
    override fun getLayout() = R.layout.channel_list_layout

    private var hideTimer: Timer ?=null
    private var hideTimerTask: TimerTask ?=null
    private var onChangeListener: OnChangeListener ?=null
    private var channelAdapter: ChannelAdapter?=null
    private var smoothScroller: LinearSmoothScroller ?=null
    var videoList = listOf<VideoInfo>()
    var isDrawerOpen = true

    private var position : Int = 0
        set(value) {
            field = if (value < videoList.size && value >= 0) value else if (value < 0) videoList.size - 1 else 0
        }

    override fun addContents() {
        super.addContents()
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                hideTimer = Timer()
                channelAdapter = ChannelAdapter {
                    onChangeListener?.onChannelIndex(it)
                    onChangeListener?.onChannelChanged(videoList[it])
                    closeDrawer()
                }
                channelAdapter?.submitList(videoList.sortedBy { it.channelNo })
                smoothScroller = object : LinearSmoothScroller(requireContext()) {
                    override fun calculateDtToFit(
                        viewStart: Int,
                        viewEnd: Int,
                        boxStart: Int,
                        boxEnd: Int,
                        snapPreference: Int
                    ): Int {
                        return (boxStart + (boxEnd - boxStart) / 2) - (viewStart + (viewEnd - viewStart) / 2)
                    }
                }
                binding.rvCategory.apply {
                    adapter = channelAdapter
                    layoutManager = LinearLayoutManager(requireContext())
                }
                openDrawer()
            }
        }
    }
    fun openDrawer(){
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                try {
                    isDrawerOpen = true
                    binding.drawerLayout.visibility = View.VISIBLE
                    hideTimer?.cancel()
                    hideTimer?.purge()
                    hideTimerTask?.cancel()
                    hideTimer = Timer()
                    hideTimer?.schedule(object : TimerTask() {
                        override fun run() {
                            launch(Dispatchers.Main) {
                                closeDrawer()
                            }
                        }
                    }.also { hideTimerTask = it }, 5000)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            launch{
                for (videoInfo in videoList) {
                    if(videoInfo.path.isNullOrEmpty())
                        videoInfo.path?.toFile(requireContext()){}
                }
            }
        }
    }
    fun closeDrawer(){
        CoroutineScope(Dispatchers.Main).launch {
            try {
                isDrawerOpen = false
                binding.drawerLayout.visibility = View.GONE
            }catch (e : Exception){
                e.printStackTrace()
            }
        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        onChangeListener = context as OnChangeListener
    }
    override fun onDetach() {
        super.onDetach()
        onChangeListener = null
    }
    fun getClickedVideoItem(isUp: Boolean) {
        CoroutineScope(Dispatchers.Main).launch {
            launch {
                try {
                    if (isUp) position-- else position++
                    smoothScroller?.targetPosition = position
                    binding.rvCategory.layoutManager?.startSmoothScroll(smoothScroller)
                    channelAdapter?.selectedPosition = position
                    channelAdapter?.notifyItemChanged(position)
                    onChangeListener?.onChannelIndex(position)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }
    interface ChannelChangeCallBack {
        fun onChannelIndex(channelIndex: Int)
    }
}