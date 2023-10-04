package com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.main

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.navigation.Navigation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.bumptech.glide.Glide
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.MainUiLayoutBinding
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MainUi

class MainAdapter: ListAdapter<MainUi, MainAdapter.MainViewHolder>(MainUiListComparator()) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainViewHolder {
        val binding = MainUiLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MainViewHolder(binding)
    }
    override fun onBindViewHolder(holder: MainViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }
    inner class MainViewHolder(private val binding: MainUiLayoutBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(ui: MainUi) {
            binding.apply {
                parentIcon.setOnFocusChangeListener { _, b ->
                    if (b){
                        parentIcon.setBackgroundResource(R.drawable.icon_hover)
                    }else{
                        parentIcon.setBackgroundResource(0)
                    }
                }
                parentIcon.setOnClickListener {
                    when(ui.id){
                        1 ->  Navigation.findNavController(it).navigate(R.id.action_mainPage_to_tv)
                        2 ->  Navigation.findNavController(it).navigate(R.id.action_mainPage_to_messages)
                        3 ->  Navigation.findNavController(it).navigate(R.id.action_mainPage_to_amenities)
                    }
                }
            }
            binding.ui = ui
        }
    }
    class MainUiListComparator : DiffUtil.ItemCallback<MainUi>() {
        override fun areItemsTheSame(oldItem: MainUi, newItem: MainUi) =
            oldItem.id == newItem.id

        override fun areContentsTheSame(oldItem: MainUi, newItem: MainUi) =
            oldItem == newItem
    }
}
