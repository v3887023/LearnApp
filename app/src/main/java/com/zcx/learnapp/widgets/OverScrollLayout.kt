package com.zcx.learnapp.widgets

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.ViewCompat
import androidx.customview.widget.ViewDragHelper
import kotlin.math.abs

class OverScrollLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {
    private lateinit var mViewDragHelper: ViewDragHelper
    private val mCallback: ViewDragHelper.Callback = object : ViewDragHelper.Callback() {
        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            return true
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val lp = releasedChild.layoutParams as MarginLayoutParams
            mViewDragHelper.smoothSlideViewTo(releasedChild, lp.leftMargin, lp.topMargin)
            ViewCompat.postInvalidateOnAnimation(this@OverScrollLayout)
        }

        override fun getViewHorizontalDragRange(child: View): Int {
            return abs(child.width)
        }

        override fun getViewVerticalDragRange(child: View): Int {
            return -1
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return child.left + dx / 2
        }
    }
    private var mOverScrollCallback: OverScrollCallback? = null
    private var mLastX = 0f
    private var mCallbackHandled = false

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        var shouldIntercept = true
        val firstChild = getChildAt(0)
        if (firstChild != null) {
            val x = ev.rawX
            when (ev.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    mCallbackHandled = false
                    mLastX = x
                }
                MotionEvent.ACTION_MOVE -> {
                    val offset = (x - mLastX).toInt()
                    mLastX = x
                    val canScrollHorizontal = firstChild.canScrollHorizontally(-offset)
                    shouldIntercept = !canScrollHorizontal
                }
                else -> {
                }
            }
        }
        if (shouldIntercept) {
            try {
                shouldIntercept = mViewDragHelper.shouldInterceptTouchEvent(ev)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        return shouldIntercept
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val firstChild = getChildAt(0)
        if (firstChild != null) {
            if (event.actionMasked == MotionEvent.ACTION_MOVE) {
                val offset = event.rawX - mLastX
                val canScrollHorizontal = firstChild.canScrollHorizontally((-offset).toInt())
                if (!canScrollHorizontal) {
                    if (!mCallbackHandled && mOverScrollCallback != null) {
                        if (mOverScrollCallback!!.onHorizontalOverScroll(firstChild.x)) {
                            mCallbackHandled = true
                            mViewDragHelper.cancel()
                            return true
                        }
                    }
                }
            }
        }
        try {
            mViewDragHelper.processTouchEvent(event)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return true
    }

    override fun computeScroll() {
        if (mViewDragHelper.continueSettling(true)) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    override fun requestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {

    }

    fun setOverScrollCallback(overScrollCallback: OverScrollCallback?) {
        mOverScrollCallback = overScrollCallback
    }

    abstract class OverScrollCallback {
        fun onHorizontalOverScroll(offset: Float): Boolean {
            return false
        }

        fun onVerticalOverScroll(offset: Float): Boolean {
            return false
        }
    }

    init {
        mViewDragHelper = ViewDragHelper.create(this, 1.0f, mCallback)
    }
}