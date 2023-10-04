package com.ph.bittelasia.meshtv.tv.glasshoteldemo.ui.tv.ui

import android.content.Intent
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import coil.load
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.tv.TvAdapter
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.controller.tv.TvListAdapter
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.core.BaseFragment
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.data.ProjectData
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.db.GlassDB
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.repository.Repository
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.util.Resource
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.databinding.FragmentTvBinding
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.GlassViewModel
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.utils.ViewModelFactory
import kotlinx.coroutines.launch

class Tv : BaseFragment<FragmentTvBinding>() {
    private lateinit var myViewModel: GlassViewModel
    private lateinit var tv : TvAdapter
    private lateinit var tvListCat : TvListAdapter

    override fun addContents() {
        super.addContents()
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                val data = ProjectData()
                val repo = Repository(data, GlassDB.db(requireContext()), requireContext())
                val factoryVm = ViewModelFactory(repo)
                myViewModel = ViewModelProvider(this@Tv, factoryVm)[GlassViewModel::class.java]

                launch {
                    binding.apply {
                        ivBg.load(R.drawable.newbg2)

                        tvRv.setHasFixedSize(true)
                        tvRv.layoutManager = LinearLayoutManager(
                            requireContext(),
                            LinearLayoutManager.HORIZONTAL,
                            false
                        )
                        tvCategory1.setHasFixedSize(true)
                        tvCategory2.setHasFixedSize(true)
                        tvCategory3.setHasFixedSize(true)
                        tvCategory4.setHasFixedSize(true)

                        tvCategory1.layoutManager = GridLayoutManager(requireContext(), 2)
                        tvCategory2.layoutManager = GridLayoutManager(requireContext(), 2)
                        tvCategory3.layoutManager = GridLayoutManager(requireContext(), 2)
                        tvCategory4.layoutManager = GridLayoutManager(requireContext(), 2)
                        tv = TvAdapter {
                            myViewModel.tvList.observe(viewLifecycleOwner) { dataList ->
                                if(dataList is Resource.Success && dataList.data!!.isNotEmpty()){
                                    val list = dataList.data
                                    var intent: Intent?
                                    tvListCat = TvListAdapter { tvChannel ->
                                        val id = list.indexOf(tvChannel)
                                        intent = Intent(activity, TVPlayerActivity::class.java)
                                        intent!!.putExtra("channel", id.toString())
                                        startActivity(intent)
                                    }
                                    val tvList = list.filter { c ->
                                        c.channelCategoryId == it.tvCategory
                                    }
                                    when (it.id) {
                                        "1" -> {
                                            tvCategory1.visibility = View.VISIBLE
                                            tvCategory2.visibility = View.GONE
                                            tvCategory3.visibility = View.GONE
                                            tvCategory4.visibility = View.GONE
                                            tvListCat.submitList(tvList)
                                            tvCategory1.adapter = tvListCat
                                        }
                                        "2" -> {
                                            tvCategory1.visibility = View.GONE
                                            tvCategory2.visibility = View.VISIBLE
                                            tvCategory3.visibility = View.GONE
                                            tvCategory4.visibility = View.GONE
                                            tvListCat.submitList(tvList)
                                            tvCategory2.adapter = tvListCat
                                        }
                                        "3" -> {
                                            tvCategory1.visibility = View.GONE
                                            tvCategory2.visibility = View.GONE
                                            tvCategory3.visibility = View.VISIBLE
                                            tvCategory4.visibility = View.GONE
                                            tvListCat.submitList(tvList)
                                            tvCategory3.adapter = tvListCat
                                        }
                                        "4" -> {
                                            tvCategory1.visibility = View.GONE
                                            tvCategory2.visibility = View.GONE
                                            tvCategory3.visibility = View.GONE
                                            tvCategory4.visibility = View.VISIBLE
                                            tvListCat.submitList(tvList)
                                            tvCategory4.adapter = tvListCat
                                        }
                                        else -> {
                                            tvCategory1.visibility = View.GONE
                                            tvCategory2.visibility = View.GONE
                                            tvCategory3.visibility = View.GONE
                                            tvCategory4.visibility = View.GONE
                                        }
                                    }
                                }

                            }
                        }
                        tv.submitList(data.getTv())
                        tvRv.adapter = tv
                    }
                }
                launch {
                    binding.apply {
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
    override fun getLayout() = R.layout.fragment_tv
}