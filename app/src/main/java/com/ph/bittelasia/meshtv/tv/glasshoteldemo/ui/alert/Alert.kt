package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.alert

import android.media.MediaPlayer
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.FragmentAlertBinding
import kotlinx.coroutines.*


class Alert : BaseFragment<FragmentAlertBinding>() {
    private var mediaPlayer: MediaPlayer?=null

    override fun addContents() {
        super.addContents()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                binding.apply {
                    broadcast.setText("THIS IS A TEST OF EMERGENCY ALERT SYSTEM. THIS IS A TEST ONLY THIS IS A TEST OF EMERGENCY ALERT SYSTEM. THIS IS A TEST ONLY")
                    broadcast.start()
                    play()
                }
            }
        }
    }
    private fun play() {
        mediaPlayer = MediaPlayer.create(requireContext(), R.raw.alarm)
        mediaPlayer!!.isLooping = true
        mediaPlayer!!.start()
    }
    override fun onDestroy() {
        super.onDestroy()
        mediaPlayer?.stop()
    }
    override fun getLayout(): Int = R.layout.fragment_alert
}