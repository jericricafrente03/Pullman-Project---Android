package com.ph.bittelasia.meshtv.tv.glasshoteldemo.core

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R


abstract class BaseFragment<F : ViewDataBinding> : Fragment() {
    private var _binding : F?=null
    val binding : F get() = _binding!!

    override fun onAttach(context: Context) {
        super.onAttach(context)
        BaseActivity.Debug.log(this, "@onAttach")
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DataBindingUtil.inflate(inflater, getLayout(), container, false)
        binding.lifecycleOwner = viewLifecycleOwner
        BaseActivity.Debug.log(this, "@onCreateView")
        return binding.root
    }
    override fun onStart() {
        super.onStart()
        addContents()
        BaseActivity.Debug.log(this, "@onStart")
    }
    override fun onDestroy() {
        super.onDestroy()
        BaseActivity.Debug.log(this, "@onDestroy")
        _binding = null
    }
    override fun onDetach() {
        super.onDetach()
        BaseActivity.Debug.log(this, "@onDetach")
        _binding = null
    }
    abstract fun getLayout() : Int
    open fun addContents(){}
}