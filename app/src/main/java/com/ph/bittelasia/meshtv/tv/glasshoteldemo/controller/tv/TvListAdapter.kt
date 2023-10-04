package com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.tv

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.TvCatLayoutBinding
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.model.ChannelData

class TvListAdapter (private val cb : (ChannelData) -> Unit): ListAdapter<ChannelData, TvListAdapter.TvListViewHolder>(TvComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvListViewHolder {
        val binding = TvCatLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvListViewHolder(binding, cb)
    }
    override fun onBindViewHolder(holder: TvListViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
    inner class TvListViewHolder(private val binding : TvCatLayoutBinding, private val cb: (ChannelData) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tvList : ChannelData){
            binding.apply {
                root.setOnClickListener {
                    cb.invoke(tvList)
                }
                parentLayout.setOnFocusChangeListener { _, b ->
                    if (b){
                        val anim: Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_in_tv)
                        binding.root.startAnimation(anim)
                        anim.fillAfter = true
                    }else{
                        val anim =AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_out_tv)
                        binding.root.startAnimation(anim)
                        anim.fillAfter = true
                    }
                }
            }
            binding.channel = tvList
        }
    }
    class TvComparator : DiffUtil.ItemCallback<ChannelData>(){
        override fun areItemsTheSame(oldItem: ChannelData, newItem: ChannelData) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: ChannelData, newItem: ChannelData) =
            oldItem == newItem
    }
}