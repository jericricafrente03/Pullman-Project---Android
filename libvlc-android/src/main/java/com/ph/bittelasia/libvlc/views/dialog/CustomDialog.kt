package com.ph.bittelasia.libvlc.views.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.ph.bittelasia.libvlc.control.listener.OnChangeListener

abstract class CustomDialog : DialogFragment() {
    var cb: OnChangeListener? = null
    override fun onAttach(context: Context) {
        super.onAttach(context)
        cb = context as OnChangeListener
    }

    override fun onDetach() {
        super.onDetach()
        cb = null
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(setLayout(), container, false)
        setIDs(view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val displayMetrics = DisplayMetrics()
        activity!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        var height = displayMetrics.heightPixels
        var width = displayMetrics.widthPixels
        if (setPercentageWidth() > 0)
            width=((width * setPercentageWidth()).toInt())
        else
            width *= 1
        val minWidth  = width
        if(setPercentageHeight() > 0)
            height=((height * setPercentageHeight()).toInt())
        val minHeight  = height
        val cl_layout = view.findViewById<View>(setConstraintLayout()) as ConstraintLayout
        cl_layout.minWidth = minWidth.toInt()
        cl_layout.minHeight = minHeight.toInt()
        setContent()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(this.activity!!.applicationContext)
        dialog.requestWindowFeature(1)
        dialog.window!!.setFlags(1024, 1024)
        dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
        dialog.window!!.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING)
        dialog.window!!.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        return dialog
    }

    abstract fun setIDs(view: View?)
    abstract fun setContent()
    abstract fun setLayout(): Int
    abstract fun setConstraintLayout(): Int
    abstract fun setPercentageWidth(): Double
    abstract fun setPercentageHeight(): Double
    fun hideSoftKeyboard() {
        if (activity!!.currentFocus != null) {
            val inputMethodManager =
                activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(activity!!.currentFocus!!.windowToken, 0)
        }
    }

    fun showSoftKeyboard(view: View) {
        val inputMethodManager =
            activity!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        view.requestFocus()
        inputMethodManager.showSoftInput(view, 0)
    }

    fun isValid(vararg t: EditText): Boolean {
        var valid = false
        for (text in t) {
            valid = text.text.toString().isNotEmpty()
        }
        return valid
    }

    fun getText(t: EditText): String {
        return t.text.toString()
    }

    companion object {
        fun dismissAllDialogs(manager: FragmentManager) {
            val fragments = manager.fragments
                ?: return
            for (fragment in fragments) {
                if (fragment is DialogFragment) {
                    fragment.dismissAllowingStateLoss()
                }
                val childFragmentManager = fragment.childFragmentManager
                if (childFragmentManager != null) dismissAllDialogs(childFragmentManager)
            }
        }
    }
}