package com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.message

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.item.MessagesItem
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.MessageLayoutBinding

class MessageAdapter (private val list : List<MessagesItem>, val cb: (MessagesItem) -> Unit): ListAdapter<MessagesItem, MessageAdapter.MessageViewHolder>(MessComparator()){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageAdapter.MessageViewHolder {
        val binding = MessageLayoutBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MessageViewHolder(binding, cb)
    }
    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        val currentItem = list[position]
        holder.bind(currentItem)
    }
    override fun getItemCount() = list.size

    inner class MessageViewHolder(private val binding: MessageLayoutBinding, val cb: (MessagesItem) -> Unit) : RecyclerView.ViewHolder(binding.root) {
        fun bind(mes: MessagesItem) {
            binding.mess = mes
            Glide.with(binding.root).load(mes.messageIcon).into(binding.ivMessage)

            binding.apply {
                root.setOnClickListener {
                    cb.invoke(mes)
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
                if (mes.status == "1"){
                    status.visibility = View.VISIBLE
                }else{
                    status.visibility = View.INVISIBLE
                }
            }
        }
    }

    class MessComparator : DiffUtil.ItemCallback<MessagesItem>() {
        override fun areItemsTheSame(oldItem: MessagesItem, newItem: MessagesItem) =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: MessagesItem, newItem: MessagesItem) =
            oldItem == newItem

    }


}