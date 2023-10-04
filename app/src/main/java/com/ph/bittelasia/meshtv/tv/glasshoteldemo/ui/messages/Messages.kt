package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.messages

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.message.MessageAdapter
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data.ProjectData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.FragmentMessagesBinding
import kotlinx.coroutines.launch

class Messages :  BaseFragment<FragmentMessagesBinding>() {
    private lateinit var mess : MessageAdapter
        override fun addContents() {
        super.addContents()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val data = ProjectData()
                launch {
                    binding.apply {
                        ivAmenities.load(R.drawable.amenitiescover)
                        ivSearch.load(R.drawable.sbarmsg1)
                        ivNav.load(R.drawable.navmore)
                        val typeface = ResourcesCompat.getFont(requireContext(), R.font.dn)
                        tc1.typeface = typeface
                        tc2.typeface = typeface
                        tc3.typeface = typeface
                        messRv.setHasFixedSize(true)
                        messRv.layoutManager = LinearLayoutManager(requireContext())
                        mess = MessageAdapter(data.getMessage()) {
                            binding.data = it
                        }
                        messRv.adapter = mess
                        launch {
                            tvBack.setOnFocusChangeListener { _, b ->
                                if (b) {
                                    val anim: Animation =
                                        AnimationUtils.loadAnimation(context, R.anim.slideup)
                                    tvBack.startAnimation(anim)
                                } else {
                                    tvBack.clearAnimation()
                                }
                            }
                            tvBack.setOnClickListener {
                                activity?.onBackPressed()
                            }
                        }
                    }
                }
            }
        }
    }
    override fun getLayout() = R.layout.fragment_messages
}