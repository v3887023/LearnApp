package com.zcx.learnapp.widgets

import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import com.zcx.learnapp.R

class ProgressView2 @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var progressDrawable: Drawable? = null
    private var progressBackgroundDrawable: Drawable? = null
    private var textColor: Int = 0
    var textSize: Float = 33f
    var typeFace: Typeface? = null
    private val textPaint = Paint()
    private val progressPaint = Paint()
    var progress: Float = 0f
        set(value) {
            field = when {
                value < 0 -> 0f
                value > 1 -> 1f
                else -> value
            }

            invalidate()
        }
    private var textPaddingHorizontal = 0f

    init {
        parseAttributeSet(context, attrs)
        initPaints()
    }

    private fun parseAttributeSet(context: Context, attrs: AttributeSet?) {
        if (attrs == null) return

        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ProgressView2)

        progressBackgroundDrawable =
            typedArray.getDrawable(R.styleable.ProgressView2_pv_progressBackgroundDrawable)
        if (progressBackgroundDrawable == null) {
            progressBackgroundDrawable = GradientDrawable().apply {
                setColor(Color.parseColor("#E9E9E9"))
                cornerRadius = 27f
            }
        }

        progressDrawable = typedArray.getDrawable(R.styleable.ProgressView2_pv_progressDrawable)
        if (progressDrawable == null) {
            progressDrawable = GradientDrawable().apply {
                setColor(Color.parseColor("#E9E9E9"))
                colors = intArrayOf(Color.parseColor("#FFCCA4"), Color.parseColor("#FFC394"))
                cornerRadius = 27f
            }
        }

        progress = typedArray.getFloat(R.styleable.ProgressView2_pv_progress, 0f)
        textPaddingHorizontal =
            typedArray.getDimension(R.styleable.ProgressView2_pv_textPaddingHorizontal, 9f)

        textColor =
            typedArray.getColor(R.styleable.ProgressView2_pv_textColor, Color.parseColor("#A04A08"))

        typedArray.recycle()
    }

    private fun initPaints() {
        textPaint.apply {
            color = textColor
            textSize = this@ProgressView2.textSize
            typeface = this@ProgressView2.typeFace
        }

        progressPaint.apply {
            color = Color.parseColor("#E9E9E9")
            isAntiAlias = true
        }
    }

    private fun getProgressWidth(): Int {
        return ((width - paddingLeft - paddingRight) * progress).toInt()
    }

    private fun getProgressHeight(): Int {
        return height - paddingTop - paddingBottom
    }

    override fun onDraw(canvas: Canvas) {
        drawProgressBackground(canvas)
        drawProgress(canvas)
        drawText(canvas)
    }

    private fun drawProgressBackground(canvas: Canvas) {
        val drawable = progressBackgroundDrawable ?: return

        drawable.setBounds(paddingLeft, paddingTop, width - paddingRight, height - paddingBottom)
        drawable.draw(canvas)
    }

    private val rectF = RectF()
    private val progressPath = Path()
    private val tempPath = Path()
    private val rectF2 = RectF()

    private fun drawProgress(canvas: Canvas) {
        val left = paddingLeft.toFloat()
        val top = paddingTop.toFloat()

        progressPath.reset()
        tempPath.reset()

        val progressWidth = getProgressWidth()
        val progressBarWidth = width - paddingLeft - paddingRight
        rectF.set(left, top, (left + progressBarWidth), (top + getProgressHeight()))

        val offsetX = (progressBarWidth - progressWidth).toFloat()
        rectF2.set(rectF)
        rectF2.offset(-offsetX, 0f)

        progressPath.addRoundRect(rectF, 27f, 27f, Path.Direction.CW)
        tempPath.addRoundRect(rectF2, 27f, 27f, Path.Direction.CW)
        progressPath.op(tempPath, Path.Op.INTERSECT)

        progressPaint.shader = LinearGradient(
            rectF.left, rectF.top, rectF2.right, rectF2.bottom,
            intArrayOf(Color.parseColor("#FFCCA4"), Color.parseColor("#FFC394")),
            null, Shader.TileMode.CLAMP
        )

        canvas.drawPath(progressPath, progressPaint)
    }

    private fun drawText(canvas: Canvas) {
        val progressText = "${(100 * progress).toInt()}%"
        val textWidth = textPaint.measureText(progressText)
        val left = paddingLeft
        val progressRight = left + getProgressWidth()
        val padding = textPaddingHorizontal
        var textLeft = progressRight - padding - textWidth
        val minTextLeft = (left + padding)
        if (textLeft < minTextLeft) {
            textLeft = minTextLeft
        }

        val fontMetrics = textPaint.fontMetrics
        val textHeight = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
        val y = (paddingTop + getProgressHeight() / 2).toFloat() -
                fontMetrics.ascent + fontMetrics.leading - textHeight / 2

        canvas.drawText(progressText, textLeft, y, textPaint)
    }
}