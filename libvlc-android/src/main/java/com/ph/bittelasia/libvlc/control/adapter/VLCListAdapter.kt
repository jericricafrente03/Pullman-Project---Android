package com.ph.bittelasia.libvlc.control.adapter

import android.view.View
import android.widget.BaseAdapter
import com.ph.bittelasia.libvlc.control.exception.PlayerException
import com.ph.bittelasia.libvlc.control.listener.OnListClickListener
import com.ph.bittelasia.libvlc.model.VideoInfo
import java.util.*

abstract class VLCListAdapter : BaseAdapter() {

    //===========================================Variable===========================================
    //------------------------------------------Instance-------------------------------------------
    var clickedListener: OnListClickListener? = null
    var objects: ArrayList<VideoInfo?>? = null
    private var filteredObjects: ArrayList<VideoInfo?>? = null

    //----------------------------------------------------------------------------------------------
    //-------------------------------------------Setter---------------------------------------------
    var parent: View? = null

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //===========================================Methods============================================
    //-------------------------------------------Getter---------------------------------------------
    var selectedItem = 0

    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //==========================================BaseAdapter=========================================
    //----------------------------------------------------------------------------------------------
    override fun getCount(): Int {
        return objects!!.size
    }

    override fun getItem(i: Int): VideoInfo? {
        return objects!![i]
    }

    override fun getItemId(i: Int): Long {
        return i.toLong()
    }

    private fun setObjects(objects: ArrayList<VideoInfo?>?, performSort: Boolean) {
        try {
            if (performSort) {
                this.objects = sortObjects(objects)
                filteredObjects = sortObjects(objects)
            } else {
                this.objects = objects
                filteredObjects = objects
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @Throws(PlayerException::class)
    fun sort(list: ArrayList<VideoInfo?>?): VLCListAdapter {
        if (list == null)
            throw PlayerException("ListAdapter:  list must not be null")
        list.sortWith(compareBy {it?.channelNo})
        setObjects(list, true)
        return this
    }
    //----------------------------------------------------------------------------------------------
    //-------------------------------------Filter Lists---------------------------------------------
    /**
     * Method to Filter Objects based on attributes assigned
     * @param field attribute of Object
     * @param performSort is boolean, if you want to perform sorting of objects
     * @param <T> is the Object to assign when performing filter
    </T> */
    fun <T> perFormFiltering(field: CharSequence?, performSort: Boolean) {
        var field = field
        try {
            var filters: ArrayList<VideoInfo?>? = ArrayList()
            if (field != null && field.isNotEmpty()) {
                field = field.toString().toLowerCase(Locale.ROOT)
                for (i in filteredObjects!!.indices) {
                    val `object` = filteredObjects!![i] as VideoInfo
                    filterObjects(filters, `object`, field)
                }
            } else {
                filters = filteredObjects
            }
            setObjects(filters, performSort)
            notifyDataSetChanged()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    /**
     * Method to Sort List by Fields
     * @param list is the Lists of the Objects
     */
    fun performSort(list: ArrayList<VideoInfo?>?) {
        try {
            setObjects(list, true)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
    //===================================Abstract Methods===========================================
    //----------------------------------------------------------------------------------------------
    /**
     * Method to filter the  of Objects
     * @param filters is the List of Objects
     * @param object an Object to Assign in filtering the ArrayList of objects
     * @param charSequence is the Sequence of character
     * @param <VideoInfo> is any type of Object
    </T> */
    abstract fun <VideoInfo> filterObjects(
        filters: ArrayList<VideoInfo?>?,
        `object`: Any?,
        charSequence: CharSequence?
    )
    //----------------------------------------------------------------------------------------------
    /**
     * Method to sort object
     * @param objects is the List of Objects
     * @param <T> is any type of objects
     * @return
    </T> */
    abstract fun <VideoInfo> sortObjects(objects: ArrayList<VideoInfo?>?): ArrayList<VideoInfo?>?
    //----------------------------------------------------------------------------------------------
    //==============================================================================================
}