package com.ph.bittelasia.meshtv.tv.glasshoteldemo.database.util

import android.animation.TimeInterpolator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.AnimatorUpdateListener
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.text.TextPaint
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import com.ph.bittelasia.meshtv.tv.glasshoteldemo.R

class MarqueeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {
    private var mPaint: TextPaint? = null
    private var mText: String? = null
    private var mSpeed = 0
    private var mCurLeft = 0f
    private var mLastLeft = 0f
    private var mAnimator: ValueAnimator? = null
    private var mLength = 0f
    private var mIsRunning = false
    private var mSpacing = 0
    private var mInterpolator: TimeInterpolator? = null
    private fun init(context: Context, attrs: AttributeSet?) {
        mPaint = TextPaint()
        mPaint!!.isAntiAlias = true
        mLastLeft = 0f
        mCurLeft = mLastLeft
        val a = context.obtainStyledAttributes(attrs, R.styleable.MarqueeView)
        mSpeed = a.getDimensionPixelSize(R.styleable.MarqueeView_speed, 100)
        val textColor = a.getColor(R.styleable.MarqueeView_text_color, Color.BLACK)
        val textSize = a.getDimensionPixelSize(R.styleable.MarqueeView_text_size, 24)
        val shadowColor = a.getColor(R.styleable.MarqueeView_text_shadowColor, Color.TRANSPARENT)
        val radius = a.getFloat(R.styleable.MarqueeView_txt_radius, 0f)
        val dx = a.getFloat(R.styleable.MarqueeView_txt_dx, 0f)
        val dy = a.getFloat(R.styleable.MarqueeView_txt_dy, 0f)
        mSpacing = a.getDimensionPixelOffset(R.styleable.MarqueeView_txt_spacing, 10)
        a.recycle()
        mPaint!!.textSize = textSize.toFloat()
        mPaint!!.color = textColor
        mPaint!!.setShadowLayer(radius, dx, dy, shadowColor)
    }

    private fun ensureAnimator() {
        mAnimator = ValueAnimator.ofFloat(0f, 1f)
        mAnimator?.interpolator = if (mInterpolator == null) LinearInterpolator() else mInterpolator
        mAnimator?.repeatCount = ValueAnimator.INFINITE
        mAnimator?.repeatMode = ValueAnimator.RESTART
        mAnimator?.addUpdateListener(AnimatorUpdateListener { valueAnimator ->
            val factor = valueAnimator.animatedFraction
            mCurLeft = mLastLeft - factor * mLength
            if (mCurLeft < -mLength) {
                mCurLeft += mLength
            }
            translationX = mCurLeft
        })
    }

    //make the view right outside of its parent about the length of the text
    fun setText(text: String?) {
        mText = text
        mLength = mPaint!!.measureText(mText) + mSpacing
        val lp = layoutParams as FrameLayout.LayoutParams
        lp.rightMargin = (-mLength).toInt()
        layoutParams = lp
        invalidate()
    }

    fun setInterpolator(interpolator: TimeInterpolator?) {
        mInterpolator = interpolator
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (TextUtils.isEmpty(mText) || mLength == 0f) return
        var left = 0f
        while (left < width) {
            canvas.drawText(mText!!, left, paddingTop - mPaint!!.ascent(), mPaint!!)
            left += mLength
        }
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        // only make the view wrap txt
        val height = (mPaint!!.descent() - mPaint!!.ascent()).toInt() + paddingBottom + paddingTop
        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), height)
    }

    fun start() {
        if (mAnimator == null) {
            ensureAnimator()
        }
        if (mIsRunning) return
        mIsRunning = true
        mAnimator!!.duration = (mLength * 1000 / mSpeed).toLong()
        mAnimator!!.start()
    }

    fun pause() {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            if (mCurLeft <= -mLength) {
                mCurLeft += mLength
            }
            mLastLeft = mCurLeft
            translationX = mCurLeft
        }
        mIsRunning = false
    }

    fun stop() {
        if (mAnimator != null) {
            mAnimator!!.cancel()
            mCurLeft = 0f
            mLastLeft = mCurLeft
            translationX = 0f
            mAnimator = null
        }
        mIsRunning = false
    }

    init {
        init(context, attrs)
    }
}