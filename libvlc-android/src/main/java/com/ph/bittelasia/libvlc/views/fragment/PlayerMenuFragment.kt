package com.ph.bittelasia.libvlc.views.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.GridView
import com.ph.bittelasia.libvlc.R
import com.ph.bittelasia.libvlc.control.adapter.VLCListAdapter
import com.ph.bittelasia.libvlc.model.Menu
import java.util.*

abstract class PlayerMenuFragment : PlayerFragment() {
    //----------------------------------------------------------------------------------------------
    //------------------------------Instance--------------------------------------------------------
    var menus: ArrayList<Menu>? = null

    //-------------------------------View-----------------------------------------------------------
    private var gridView: GridView? = null
        private set

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==============================LifeCycle=======================================================
    //----------------------------------------------------------------------------------------------
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.player_menu_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            draw(view)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onDetach() {
        super.onDetach()
        onFragmentListener!!.onFragmentDetached(this)
        onFragmentListener = null
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //====================================Method====================================================
    //----------------------------------------------------------------------------------------------
    @Throws(Exception::class)
    fun draw(view: View) {
        gridView = view.findViewById(R.id.gv_list)
        if (adapter != null && gridView!=null) {
            gridView?.adapter = adapter
            gridView?.onItemClickListener =
                AdapterView.OnItemClickListener { adapterView, view, _, _ ->
                    adapterView.isSelected = true
                }
            gridView?.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    adapterView: AdapterView<*>?,
                    view: View,
                    i: Int,
                    l: Long
                ) {
                }

                override fun onNothingSelected(adapterView: AdapterView<*>?) {}
            }
            gridView?.setOnHoverListener(View.OnHoverListener { view, _ ->
                view.requestFocus()
                false
            })
            adapter!!.notifyDataSetChanged()
        }
    }

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //================================Abstract Methods==============================================
    //----------------------------------------------------------------------------------------------
    //----------------------------------------------------------------------------------------------
    @get:Throws(Exception::class)
    abstract val adapter: VLCListAdapter?

    //==============================================================================================
    companion object {
        //==============================Variable========================================================
        //------------------------------Constant--------------------------------------------------------
        private val TAG = PlayerMenuFragment::class.java.simpleName
    }
}