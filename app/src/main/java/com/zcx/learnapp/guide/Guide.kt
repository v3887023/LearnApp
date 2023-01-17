package com.zcx.learnapp.guide

import android.app.Activity
import android.content.Context
import android.graphics.*
import android.view.Gravity
import android.view.View
import android.view.View.OnAttachStateChangeListener
import android.view.ViewGroup
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import android.widget.PopupWindow
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import java.lang.ref.WeakReference


class Guide private constructor(activity: Activity, list: List<GuidePage>) {
    private val activityWeakReference = WeakReference(activity)
    private val guideController = object : IGuideController {
        override fun dismiss() {
            popupWindow.dismiss()
        }

        override fun next() {
            list[index].close()

            index++
            highlightLayout.removeAllViews()

            if (index < list.size) {
                val nextGuidePage = list[index]
                nextGuidePage.configure(highlightLayout, this).display()
            } else {
                dismiss()
            }
        }
    }

    private val lifecycleObserver = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
        fun onDestroy() {
            guideController.dismiss()
        }
    }

    companion object {
        val DEFAULT_BACKGROUND_COLOR = Color.parseColor("#80000000")
    }

    init {
        if (activity is ComponentActivity) {
            activity.lifecycle.addObserver(lifecycleObserver)
        }
    }

    private var popupWindow: PopupWindow = PopupWindow(MATCH_PARENT, MATCH_PARENT)
    private val list = ArrayList<GuidePage>(list)
    private var backgroundColor: Int = DEFAULT_BACKGROUND_COLOR
    private var index = 0

    private val highlightLayout = HighlightLayout(activity)

    fun show() {
        if (list.isEmpty() || isShowing()) {
            return
        }

        highlightLayout.setBackgroundColor(backgroundColor)
        highlightLayout.setOnClickListener {
            val guidePage = list[index]
            if (guidePage.isOutsideTouchable()) {
                guideController.next()
            }
        }

        val firstGuidePage = list.first()
        firstGuidePage.configure(highlightLayout, guideController).display()

        popupWindow.isClippingEnabled = false
        popupWindow.contentView = highlightLayout
        popupWindow.setOnDismissListener {
            val activity = activityWeakReference.get()
            if (activity is ComponentActivity) {
                activity.lifecycle.removeObserver(lifecycleObserver)
            }

            if (index < list.size) {
                list[index].close()
            }
        }

        val firstHighlightedView = firstGuidePage.highlightedView
        firstHighlightedView?.post {
            popupWindow.showAtLocation(firstHighlightedView, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    fun isShowing(): Boolean {
        return popupWindow.isShowing
    }

    class Builder(val activity: Activity) {
        private val list = ArrayList<GuidePage>()
        private var backgroundColor = DEFAULT_BACKGROUND_COLOR

        fun addPage(page: GuidePage): Builder {
            this.list.add(page)
            return this
        }

        fun backgroundColor(color: Int) {
            this.backgroundColor = color
        }

        fun build(): Guide {
            return Guide(activity, list)
        }
    }
}

class GuidePage private constructor() {
    var highlightedView: View? = null
        private set

    private var highlightLayout: HighlightLayout? = null
    private var guideController: IGuideController? = null
    private val highlightOptions = HighlightOptions.ofRectangle()

    private var guideView: IGuideView? = null

    private val location = IntArray(2)
    private var lastLeft = 0
    private var lastTop = 0

    private val layoutListener = ViewTreeObserver.OnGlobalLayoutListener {
        val highlightedView = this.highlightedView ?: return@OnGlobalLayoutListener

        val location = this.location
        highlightedView.getLocationInWindow(location)

        if (location[0] == lastLeft && location[1] == lastTop) {
            return@OnGlobalLayoutListener
        }

        lastLeft = location[0]
        lastTop = location[1]

        highlightLayout?.removeAllViews()

        doDisplay(highlightedView)
    }

    fun configure(highlightLayout: HighlightLayout, guideController: IGuideController): GuidePage {
        this.highlightLayout = highlightLayout
        this.guideController = guideController
        return this
    }

    fun display() {
        val highlightedView = this.highlightedView ?: return

//        highlightedView.viewTreeObserver.addOnGlobalLayoutListener(layoutListener)

        doDisplay(highlightedView)
    }

    private fun doDisplay(highlightedView: View) {
        val highlightLayout = this.highlightLayout ?: return
        val rectF = getRectF(highlightedView)
        highlightLayout.rectF = rectF
        highlightLayout.applyHighlightOptions(highlightOptions)

        val guideView = this.guideView
        if (guideView != null) {
            guideView.guideController = guideController

            val view = guideView.getView(highlightLayout.context, highlightLayout)
            if (view != null) {
                view.addOnAttachStateChangeListener(object : OnAttachStateChangeListener {
                    override fun onViewAttachedToWindow(view: View) {
                        val lp = view.layoutParams as ViewGroup.MarginLayoutParams
                        val location = guideView.getLocation(view, rectF)
                        lp.leftMargin = location[0]
                        lp.topMargin = location[1]

                        view.layoutParams = lp
                    }

                    override fun onViewDetachedFromWindow(p0: View) {

                    }
                })
                highlightLayout.addView(view, guideView.getLayoutParams())
            }
        }
    }

    fun close() {
        val highlightedView = this.highlightedView ?: return

        highlightedView.viewTreeObserver.removeOnGlobalLayoutListener(layoutListener)
    }

    fun isOutsideTouchable(): Boolean {
        return guideView?.isOutsideTouchable() ?: false
    }

    private fun getRectF(view: View): RectF {
        val location = IntArray(2)
        view.getLocationInWindow(location)

        val paddings = highlightOptions.paddings

        val viewWidth = view.width
        val viewHeight = view.height
        var left = (location[0] - paddings[0]).toFloat()
        var top = (location[1] - paddings[1]).toFloat()
        var width = viewWidth
        var height = viewHeight

        if (highlightOptions.isTrimToSquare && viewWidth != viewHeight) {
            if (viewWidth > viewHeight) {
                width = viewHeight
                left += (viewWidth - width) / 2
            } else {
                height = viewWidth
                top += (viewHeight - height) / 2
            }
        }

        return RectF(left, top, left + width + paddings[2], top + height + paddings[3])
    }

    class Builder {
        private var highlightedView: View? = null
        private var guideView: IGuideView? = null
        private val highlightOption = HighlightOptions.ofRectangle()

        fun highlight(view: View): Builder {
            this.highlightedView = view
            return this
        }

        fun highlightOptions(highlightOptions: HighlightOptions): Builder {
            this.highlightOption.apply(highlightOptions)
            return this
        }

        fun guideView(guideView: IGuideView): Builder {
            this.guideView = guideView
            return this
        }

        fun build(): GuidePage {
            return GuidePage().apply {
                this.highlightedView = this@Builder.highlightedView
                this.guideView = this@Builder.guideView
                this.highlightOptions.apply(this@Builder.highlightOption)
            }
        }
    }
}

class HighlightOptions private constructor() {
    val paddings: IntArray = IntArray(4)
    var shape: Int = SHAPE_RECTANGLE
    var isTrimToSquare = false

    companion object {
        const val SHAPE_RECTANGLE = 1
        const val SHAPE_OVAL = 2

        fun ofRectangle(): HighlightOptions {
            return HighlightOptions().apply { shape = SHAPE_RECTANGLE }
        }

        fun ofOval(): HighlightOptions {
            return HighlightOptions().apply { shape = SHAPE_OVAL }
        }
    }

    fun padding(left: Int = 0, top: Int = 0, right: Int = 0, bottom: Int = 0): HighlightOptions {
        paddings[0] = left
        paddings[1] = top
        paddings[2] = right
        paddings[3] = bottom
        return this
    }

    fun padding(padding: Int): HighlightOptions {
        paddings[0] = padding
        paddings[1] = padding
        paddings[2] = padding
        paddings[3] = padding
        return this
    }

    fun trimToSquare(): HighlightOptions {
        this.isTrimToSquare = true
        return this
    }


    fun apply(anotherOptions: HighlightOptions) {
        this.paddings[0] = anotherOptions.paddings[0]
        this.paddings[1] = anotherOptions.paddings[1]
        this.paddings[2] = anotherOptions.paddings[2]
        this.paddings[3] = anotherOptions.paddings[3]

        this.shape = anotherOptions.shape
        this.isTrimToSquare = anotherOptions.isTrimToSquare
    }
}

class HighlightLayout(context: Context) : FrameLayout(context) {
    private val paint = Paint()
    private val highlightOptions = HighlightOptions.ofRectangle()

    init {
        paint.isAntiAlias = true
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
    }

    var rectF: RectF = RectF()

    override fun onDraw(canvas: Canvas) {
        when (highlightOptions.shape) {
            HighlightOptions.SHAPE_RECTANGLE -> {
                canvas.drawRect(rectF, paint)
            }

            HighlightOptions.SHAPE_OVAL -> {
                canvas.drawOval(rectF, paint)
            }
        }
    }

    fun applyHighlightOptions(options: HighlightOptions) {
        this.highlightOptions.apply(options)
        invalidate()
    }
}

interface IGuideController {
    fun dismiss()

    fun next()
}

interface IGuideView {

    fun isOutsideTouchable(): Boolean = false

    fun getLocation(view: View, highlightRectF: RectF): IntArray

    fun getView(context: Context, parent: ViewGroup): View?

    fun getLayoutParams(): ViewGroup.LayoutParams {
        return ViewGroup.LayoutParams(WRAP_CONTENT, WRAP_CONTENT)
    }

    var guideController: IGuideController?
}

abstract class AbstractGuideView : IGuideView {
    override var guideController: IGuideController? = null

}

class TestGuideView : AbstractGuideView() {
    private var view: View? = null

    override fun getLocation(view: View, highlightRectF: RectF): IntArray {
        val x = highlightRectF.left.toInt()
        val y = highlightRectF.top.toInt() - 100

        return intArrayOf(x, y)
    }

    override fun getView(context: Context, parent: ViewGroup): View {
        val view = view
        if (view != null) {
            return view
        }

        return TextView(context).apply {
            text = "这是文本"

            setTextColor(Color.WHITE)
        }.apply { this@TestGuideView.view = this }
    }

    override fun isOutsideTouchable() = true
}