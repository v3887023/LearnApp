package com.zcx.learnapp.widgets

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View

class TouchableView @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var lastX: Int = 0
    private var lastY: Int = 0

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val x = event.x.toInt()
        val y = event.y.toInt()
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                lastX = x
                lastY = y
            }

            MotionEvent.ACTION_MOVE -> {
                val offsetX = lastX - x
                val offsetY = lastY - y
                scrollBy(offsetX, offsetY)

                lastX = x
                lastY = y
            }

            MotionEvent.ACTION_UP -> {

            }
        }

        return true
    }

    private val paint = Paint().apply { color = Color.WHITE }

    private val gestureDetector = GestureDetector(context, GestureListener())

    private class GestureListener : GestureDetector.OnGestureListener {
        override fun onDown(e: MotionEvent?): Boolean {
            return false
        }

        override fun onShowPress(e: MotionEvent?) {

        }

        override fun onSingleTapUp(e: MotionEvent?): Boolean {
            return false
        }

        override fun onScroll(
            e1: MotionEvent?,
            e2: MotionEvent?,
            distanceX: Float,
            distanceY: Float
        ): Boolean {
            return false
        }

        override fun onLongPress(e: MotionEvent?) {

        }

        override fun onFling(
            e1: MotionEvent?,
            e2: MotionEvent?,
            velocityX: Float,
            velocityY: Float
        ): Boolean {
            return false
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawRect(0f, 0f, measuredWidth.toFloat(), measuredHeight.toFloat(), paint)
    }
}