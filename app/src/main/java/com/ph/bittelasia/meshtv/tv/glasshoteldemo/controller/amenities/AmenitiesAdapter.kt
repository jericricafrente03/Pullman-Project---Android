package com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.amenities

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.AmenitiesItem
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.AmenitiesLayoutBinding

class AmenitiesAdapter(val cb : (AmenitiesItem) -> Unit ) : ListAdapter<AmenitiesItem, AmenitiesAdapter.AmenitiesVH>(AmenitiesComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AmenitiesVH {
        val binding = AmenitiesLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return AmenitiesVH(binding,cb)
    }
    override fun onBindViewHolder(holder: AmenitiesVH, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
    inner class AmenitiesVH(val binding : AmenitiesLayoutBinding, val cb : (AmenitiesItem) -> Unit) : RecyclerView.ViewHolder(binding.root){
        fun bind(amenities: AmenitiesItem){
            binding.apply {
                root.setOnClickListener {
                    cb.invoke(amenities)
                }
                binding.root.setOnFocusChangeListener { _, b ->
                    if (b){
                        cb.invoke(amenities)
                        val anim: Animation = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_in_tv)
                        root.startAnimation(anim)
                        anim.fillAfter = true
                    }else{
                        val anim = AnimationUtils.loadAnimation(binding.root.context, R.anim.scale_out_tv)
                        root.startAnimation(anim)
                        anim.fillAfter = true
                    }
                }
            }
            binding.amenities = amenities
        }
    }
    class AmenitiesComparator : DiffUtil.ItemCallback<AmenitiesItem>() {
        override fun areItemsTheSame(oldItem: AmenitiesItem, newItem: AmenitiesItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: AmenitiesItem, newItem: AmenitiesItem) =
            oldItem == newItem
    }
}