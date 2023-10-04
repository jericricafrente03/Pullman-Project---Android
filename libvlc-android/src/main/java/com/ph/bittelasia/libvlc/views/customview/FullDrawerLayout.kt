package com.ph.bittelasia.libvlc.views.customview

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import androidx.drawerlayout.widget.DrawerLayout

class FullDrawerLayout : DrawerLayout {

    constructor(context: Context?) : super(context!!) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context!!, attrs) {}

    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context!!, attrs, defStyle) {}

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        require(!(widthMode != MeasureSpec.EXACTLY || heightMode != MeasureSpec.EXACTLY)) { "DrawerLayout must be measured with MeasureSpec.EXACTLY." }
        setMeasuredDimension(widthSize, heightSize)

        // Gravity value for each drawer we've seen. Only one of each permitted.
        val foundDrawers = 0
        val childCount = childCount
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility == View.GONE) {
                continue
            }
            val lp = child.layoutParams as LayoutParams
            if (isContentView(child)) {
                // Content views get measured at exactly the layout's size.
                val contentWidthSpec = MeasureSpec.makeMeasureSpec(
                    widthSize - lp.leftMargin - lp.rightMargin, MeasureSpec.EXACTLY
                )
                val contentHeightSpec = MeasureSpec.makeMeasureSpec(
                    heightSize - lp.topMargin - lp.bottomMargin, MeasureSpec.EXACTLY
                )
                child.measure(contentWidthSpec, contentHeightSpec)
            } else if (isDrawerView(child)) {
                val childGravity = getDrawerViewGravity(child) and Gravity.HORIZONTAL_GRAVITY_MASK
                check(foundDrawers and childGravity == 0) {
                    "Child drawer has absolute gravity " +
                            gravityToString(childGravity) + " but this already has a " +
                            "drawer view along that edge"
                }
                val drawerWidthSpec = ViewGroup.getChildMeasureSpec(
                    widthMeasureSpec,
                    MIN_DRAWER_MARGIN + lp.leftMargin + lp.rightMargin,
                    lp.width
                )
                val drawerHeightSpec = ViewGroup.getChildMeasureSpec(
                    heightMeasureSpec,
                    lp.topMargin + lp.bottomMargin,
                    lp.height
                )
                child.measure(drawerWidthSpec, drawerHeightSpec)
            } else {
                throw IllegalStateException(
                    "Child " + child + " at index " + i +
                            " does not have a valid layout_gravity - must be Gravity.LEFT, " +
                            "Gravity.RIGHT or Gravity.NO_GRAVITY"
                )
            }
        }
    }

    fun isContentView(child: View): Boolean {
        return (child.layoutParams as LayoutParams).gravity == Gravity.NO_GRAVITY
    }

    fun isDrawerView(child: View): Boolean {
        val gravity = (child.layoutParams as LayoutParams).gravity
        val absGravity = Gravity.getAbsoluteGravity(
            gravity,
            child.layoutDirection
        )
        return absGravity and (Gravity.LEFT or Gravity.RIGHT) != 0
    }

    fun getDrawerViewGravity(drawerView: View): Int {
        val gravity = (drawerView.layoutParams as LayoutParams).gravity
        return Gravity.getAbsoluteGravity(gravity, drawerView.layoutDirection)
    }

    companion object {
        private const val MIN_DRAWER_MARGIN = 0 // dp
        @SuppressLint("RtlHardcoded")
        fun gravityToString(gravity: Int): String {
            if (gravity and Gravity.LEFT == Gravity.LEFT) {
                return "LEFT"
            }
            return if (gravity and Gravity.RIGHT == Gravity.RIGHT) {
                "RIGHT"
            } else Integer.toHexString(gravity)
        }
    }
}