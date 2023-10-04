package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.amenities

import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.amenities.AmenitiesAdapter
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data.ProjectData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.FragmentAmenitiesBinding
import kotlinx.coroutines.launch

class Amenities : BaseFragment<FragmentAmenitiesBinding>() {
    private lateinit var amenAdapter : AmenitiesAdapter
    override fun addContents() {
        super.addContents()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val data = ProjectData()
                launch {
                    binding.apply {
                        ivAmenities.load(R.drawable.amenitiescover)
                        rvAmenities.setHasFixedSize(true)
                        rvAmenities.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        amenAdapter = AmenitiesAdapter {
                            tvCaption.text = it.caption
                        }
                        amenAdapter.submitList(data.getAmenities())
                        rvAmenities.adapter = amenAdapter

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
    override fun getLayout() = R.layout.fragment_amenities
}