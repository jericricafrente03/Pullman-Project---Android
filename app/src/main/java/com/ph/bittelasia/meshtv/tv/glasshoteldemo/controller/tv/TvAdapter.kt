package com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.tv

import android.graphics.Color
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.TvItem
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.TvlistLayoutBinding

class TvAdapter (val cb : (TvItem) -> Unit): ListAdapter<TvItem, TvAdapter.TvViewHolder>(TvComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TvViewHolder {
        val binding = TvlistLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TvViewHolder(binding, cb)
    }
    override fun onBindViewHolder(holder: TvViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }
    inner class TvViewHolder(private val binding : TvlistLayoutBinding, val cb: (TvItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(tv : TvItem){
            binding.apply {
                root.setOnClickListener {
                    cb.invoke(tv)
                }
                parent.setOnFocusChangeListener { _, b ->
                    if (b){
                        cb.invoke(tv)
                    }
                }
            }
            binding.tv = tv
        }
    }
    class TvComparator : DiffUtil.ItemCallback<TvItem>(){
        override fun areItemsTheSame(oldItem: TvItem, newItem: TvItem) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: TvItem, newItem: TvItem) =
            oldItem == newItem
    }
}