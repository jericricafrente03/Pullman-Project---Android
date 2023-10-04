package com.ph.bittelasia.libvlc.views.fragment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.ph.bittelasia.libvlc.control.annotation.UpdateContents
import com.ph.bittelasia.libvlc.control.listener.OnChangeListener
import com.ph.bittelasia.libvlc.control.listener.OnFragmentListener

abstract class PlayerFragment : Fragment() {
    //==================================Variable====================================================
    //----------------------------------Instance----------------------------------------------------
    var onChangeListener: OnChangeListener? = null
    var onFragmentListener: OnFragmentListener? = null

    var isHasControl = false
    var isRemoved = false

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeObject(this)
    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        onChangeListener = context as OnChangeListener
        onFragmentListener = context as OnFragmentListener
        isRemoved = false
    }

    override fun onDetach() {
        super.onDetach()
        onChangeListener = null
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //----------------------------------------------------------------------------------------------
    open fun <T : PlayerFragment?> attachSelf(fragment: T, containerID : Int, tag: String) {
        try {
            activity?.runOnUiThread {
                try {
                    requireActivity().supportFragmentManager.beginTransaction()
                        .replace(
                            containerID,
                            fragment!!,
                            tag
                        )
                        .commitAllowingStateLoss()
                    isRemoved = false
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun removeSelf(TAG: String?) {
        try {
            activity?.runOnUiThread {
                try {
                    val fragment = activity?.supportFragmentManager?.findFragmentByTag(TAG)
                    if (fragment != null) {
                        activity?.supportFragmentManager?.beginTransaction()?.remove(fragment)?.commitAllowingStateLoss()
                    }
                    isRemoved = true
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun <T : PlayerFragment?> showSelf( fragment: T) {
        try {
            activity?.runOnUiThread {
                try {
                   requireActivity().supportFragmentManager.beginTransaction()
                        .show(fragment!!)
                        .commitAllowingStateLoss()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    open fun hideSelf(TAG: String?) {
        try {
            activity?.runOnUiThread {
                try {
                    val fragment = requireActivity().supportFragmentManager.findFragmentByTag(TAG)
                    if (fragment != null) {
                        requireActivity().supportFragmentManager.beginTransaction()
                            .hide(fragment)
                            .commitAllowingStateLoss()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    @Throws(Exception::class)
    private fun initializeObject(`object`: Any) {
        val clazz: Class<*> = `object`.javaClass
        for (method in clazz.declaredMethods) {
            if (method.isAnnotationPresent(UpdateContents::class.java)) {
                method.isAccessible = true
                method.invoke(`object`)
            }
        }
    }

    //==============================================================================================
}