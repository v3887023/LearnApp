package com.example.learnapp.widgets

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.example.learnapp.BuildConfig
import com.example.learnapp.R
import java.util.*

class SaleProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private val px: Int
    private val mRectF: RectF = RectF()
    private var mProgressNormalDrawable: Drawable? = null
    private var mProgressDisabledDrawable: Drawable? = null
    private var mSecondProgressNormalDrawable: Drawable? = null
    private var mSecondProgressDisabledDrawable: Drawable? = null
    private var mProgressBarHeight = 0
    private var mSaleProgressData: SaleProgressData? =
        null
    private var mMaxLevelCount = 0
    private var mExtraSpace = 0
    private var mTargetTextPaint: Paint? = null
    private var mAwardTextPaint: Paint? = null
    private var mAwardTextBgPaint: Paint? = null
    private var mPointOuterPaint: Paint? = null
    private var mPointInnerPaint: Paint? = null
    private var mNeedExtraSpace = false
    private var mStartIndex = 0
    private var mEndIndexExclusive = 0
    private var mOuterPointRadius = 0f
    private var mInnerPointRadius = 0f
    private var mAwardTextPadding = 0
    private var mProgressBarPaddingTop = 0
    private var mProgressBarPaddingBottom = 0
    private var mProgressNormalColor = 0
    private var mProgressDisabledColor = 0
    private var mSecondProgressNormalColor = 0
    private var mSecondProgressDisabledColor = 0
    private var mInnerPointNormalColor = 0
    private var mOuterPointDisabledColor = 0
    private var mAwardTextNormalColor = 0
    private var mAwardTextBgNormalColor = 0
    private var mAwardTextBgDisabledColor = 0
    private var mTargetTextColor = 0
    private var mProgressPaddingLeft = 0
    private var mProgressPaddingRight = 0

    init {
        val scale = context.resources.displayMetrics.density
        px = (scale + 0.5f).toInt()
        initColors()
        parseAttributeSet(context, attrs, defStyleAttr)
        initDrawables()
        initPaints(context)
        initValues()
        if (BuildConfig.DEBUG) {
            initWithFakeData()
        }
    }

    private fun initColors() {
        mProgressNormalColor = Color.parseColor("#FFD48E")
        mProgressDisabledColor = Color.parseColor("#F2F2F2")
        mSecondProgressNormalColor = Color.parseColor("#FF3A45")
        mSecondProgressDisabledColor = Color.parseColor("#AAAAAA")
        mInnerPointNormalColor = Color.WHITE
        mOuterPointDisabledColor = Color.parseColor("#CCCCCC")
        mAwardTextNormalColor = Color.WHITE
        mAwardTextBgNormalColor = Color.parseColor("#FF7400")
        mAwardTextBgDisabledColor = Color.parseColor("#C0C0C0")
        mTargetTextColor = Color.parseColor("#AAAAAA")
    }

    private fun parseAttributeSet(context: Context, attrs: AttributeSet?, defStyleAttr: Int) {
        if (attrs != null) {
            val a: TypedArray =
                context.obtainStyledAttributes(attrs, R.styleable.SaleProgressView, defStyleAttr, 0)
            val progressDrawable: Drawable? =
                a.getDrawable(R.styleable.SaleProgressView_spv_progressDrawable)
            if (progressDrawable != null) {
                mProgressNormalDrawable = progressDrawable
            }
            val secondProgressDrawable: Drawable? =
                a.getDrawable(R.styleable.SaleProgressView_spv_secondProgressDrawable)
            if (secondProgressDrawable != null) {
                mSecondProgressNormalDrawable = secondProgressDrawable
            }
            mProgressBarHeight = a.getDimension(
                R.styleable.SaleProgressView_spv_progressBarHeight,
                (4 * px).toFloat()
            ).toInt()
            mMaxLevelCount = a.getInt(R.styleable.SaleProgressView_spv_maxLevelCount, 4)
            mExtraSpace =
                a.getDimension(R.styleable.SaleProgressView_spv_extraSpace, (16 * px).toFloat())
                    .toInt()
            mProgressPaddingLeft =
                a.getDimension(R.styleable.SaleProgressView_spv_progressPaddingLeft, 0f).toInt()
            mProgressPaddingRight =
                a.getDimension(R.styleable.SaleProgressView_spv_progressPaddingRight, 0f).toInt()
            a.recycle()
        }
    }

    private fun initDrawables() {
        val radius = 12 * px
        var d = GradientDrawable()
        d.setCornerRadius(radius.toFloat())
        d.setColor(mProgressNormalColor)
        mProgressNormalDrawable = d
        d = GradientDrawable()
        d.setCornerRadius(radius.toFloat())
        d.setColor(mProgressDisabledColor)
        mProgressDisabledDrawable = d
        d = GradientDrawable()
        d.setCornerRadius(radius.toFloat())
        d.setColor(mSecondProgressNormalColor)
        mSecondProgressNormalDrawable = d
        d = GradientDrawable()
        d.setCornerRadius(radius.toFloat())
        d.setColor(mSecondProgressDisabledColor)
        mSecondProgressDisabledDrawable = d
    }

    private fun initPaints(context: Context) {
        var paint = Paint()
        val resources = context.resources
        paint.textSize = (12 * px).toFloat()
        paint.color = mTargetTextColor
        mTargetTextPaint = paint
        paint = Paint()
        paint.color = mAwardTextNormalColor
        paint.textSize = (11 * px).toFloat()
        mAwardTextPaint = paint
        paint = Paint()
        mAwardTextBgPaint = paint
        paint = Paint()
        paint.isAntiAlias = true
        mPointOuterPaint = paint
        paint = Paint()
        paint.color = mInnerPointNormalColor
        paint.isAntiAlias = true
        mPointInnerPaint = paint
    }

    private fun initValues() {
        mOuterPointRadius = (4 * px).toFloat()
        mInnerPointRadius = 1.8f * px
        mAwardTextPadding = 2 * px
        mProgressBarPaddingTop = 8 * px
        mProgressBarPaddingBottom = 8 * px
    }

    private fun initWithFakeData() {
        val levelList: MutableList<SaleProgressData.Level> = ArrayList()
        levelList.add(SaleProgressData.Level(100, 10))
        levelList.add(SaleProgressData.Level(200, 20))
        levelList.add(SaleProgressData.Level(300, 30))
        levelList.add(SaleProgressData.Level(400, 40))
        levelList.add(SaleProgressData.Level(500, 50))
        mSaleProgressData = SaleProgressData(400.0, SaleProgressData.TYPE_SALES, levelList)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        if (mSaleProgressData == null) {
            return
        }

        // TODO 测量代码待优化
        val widthMode = MeasureSpec.getMode(widthMeasureSpec)
        val widthSize = MeasureSpec.getSize(widthMeasureSpec)
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        val heightSize = MeasureSpec.getSize(heightMeasureSpec)
        val width = 400
        val height = (paddingTop + getTextHeight(mAwardTextPaint) +
                mAwardTextPadding * 2 + mProgressBarPaddingTop +
                mProgressBarHeight + mProgressBarPaddingBottom +
                getTextHeight(mTargetTextPaint) + paddingBottom).toInt()
        if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, height)
        } else if (layoutParams.width == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(width, heightSize)
        } else if (layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
            setMeasuredDimension(widthSize, height)
        }
    }

    private fun getTextHeight(paint: Paint?): Float {
        val fontMetrics = paint!!.fontMetrics
        return fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
    }

    override fun onDraw(canvas: Canvas) {
        val saleProgressData: SaleProgressData =
            mSaleProgressData
                ?: return
        val levelList: List<SaleProgressData.Level> = saleProgressData.levelList
        if (levelList == null || levelList.isEmpty()) {
            return
        }
        computeBeforeDrawing(saleProgressData)
        drawTopText(canvas)
        drawProgressBar(canvas)
        drawSecondProgressBar(canvas)
        drawPoint(canvas)
        drawBottomText(canvas)
    }

    private fun computeBeforeDrawing(saleProgressData: SaleProgressData) {
        val levelList = saleProgressData.levelList
        val size = levelList.size
        val maxLevelCount = mMaxLevelCount
        val current = saleProgressData.current
        if (size > maxLevelCount) {
            var i = 0
            while (i < size && levelList[i].target <= current) {
                i++
            }
            mEndIndexExclusive =
                if (i < maxLevelCount) maxLevelCount else if (i < size) i + 1 else i
            mStartIndex = mEndIndexExclusive - maxLevelCount
        } else {
            mStartIndex = 0
            mEndIndexExclusive = size
        }
        mNeedExtraSpace = mEndIndexExclusive < size
    }

    private fun drawTopText(canvas: Canvas) {
        val saleProgressData: SaleProgressData =
            mSaleProgressData
                ?: return
        val levelList: List<SaleProgressData.Level> = saleProgressData.levelList
        val viewRight = right - paddingRight
        val progressBarWidth = progressBarWidth
        val levelCount = mEndIndexExclusive - mStartIndex
        val eachWidth = progressBarWidth * 1f / levelCount
        var currentX = paddingLeft + mProgressPaddingLeft + eachWidth
        val awardTextPaint = mAwardTextPaint
        val fontMetrics = awardTextPaint!!.fontMetrics
        val textHeight = getTextHeight(awardTextPaint)
        val textPadding = mAwardTextPadding
        val textTop = (paddingTop + textPadding).toFloat()
        val textBottom = textTop + textHeight
        val bgCorner = (4 * px).toFloat()
        mAwardTextBgPaint!!.color =
            if (isEnabled) mAwardTextBgNormalColor else mAwardTextBgDisabledColor
        var i = mStartIndex
        val end = mEndIndexExclusive
        while (i < end) {
            val awardText = "奖￥${levelList[i].award}"
            val textWidth = awardTextPaint.measureText(awardText)
            var textLeft = currentX - textWidth / 2 - mOuterPointRadius
            var textRight = textLeft + textWidth
            if (textRight > viewRight) {
                textRight = (viewRight - textPadding).toFloat()
                textLeft = textRight - textWidth
            }
            mRectF.set(
                textLeft - textPadding,
                textTop - textPadding,
                textRight + textPadding,
                textBottom + textPadding
            )
            canvas.drawRoundRect(mRectF, bgCorner, bgCorner, mAwardTextBgPaint!!)
            canvas.drawText(
                awardText,
                textLeft,
                textTop - fontMetrics.ascent + fontMetrics.leading,
                mAwardTextPaint!!
            )
            currentX += eachWidth
            i++
        }
        canvas.translate(0f, textBottom + textPadding + mProgressBarPaddingTop)
    }

    private fun drawProgressBar(canvas: Canvas) {
        val drawable: Drawable? =
            if (isEnabled) mProgressNormalDrawable else mProgressDisabledDrawable
        if (drawable != null) {
            drawable.setBounds(
                paddingLeft + mProgressPaddingLeft,
                0,
                measuredWidth - paddingRight - mProgressPaddingRight,
                mProgressBarHeight
            )
            drawable.draw(canvas)
        }
    }

    private fun drawSecondProgressBar(canvas: Canvas) {
        val saleProgressData: SaleProgressData =
            mSaleProgressData
                ?: return
        val levelList: List<SaleProgressData.Level> = saleProgressData.levelList
        val progressBarWidth = progressBarWidth
        val levelCount = mEndIndexExclusive - mStartIndex
        val eachWidth = progressBarWidth / levelCount
        var right = paddingLeft + mProgressPaddingLeft
        var i = mStartIndex
        while (i < mEndIndexExclusive && saleProgressData.current > levelList[i].target) {
            i++
        }
        right += (i - mStartIndex) * eachWidth
        val currentTarget = if (i == 0) 0 else levelList[i - 1].target
        if (saleProgressData.current != currentTarget.toDouble() && i < mEndIndexExclusive) {
            val nextTarget = levelList[i].target
            val interval = nextTarget - currentTarget
            val percent: Double = (saleProgressData.current - currentTarget) / interval
            if (percent > 0) {
                right += (eachWidth * percent).toInt()
            }
        }
        val drawable: Drawable? =
            if (isEnabled) mSecondProgressNormalDrawable else mSecondProgressDisabledDrawable
        if (drawable != null) {
            drawable.setBounds(paddingLeft + mProgressPaddingLeft, 0, right, mProgressBarHeight)
            drawable.draw(canvas)
        }
    }

    private fun drawPoint(canvas: Canvas) {
        val saleProgressData: SaleProgressData =
            mSaleProgressData
                ?: return
        val levelList: List<SaleProgressData.Level> = saleProgressData.levelList
        val progressBarWidth = progressBarWidth
        val levelCount = mEndIndexExclusive - mStartIndex
        val eachWidth = progressBarWidth * 1f / levelCount
        var currentX = paddingLeft + mProgressPaddingLeft + eachWidth
        val halfProgressHeight = mProgressBarHeight * 1f / 2
        var i = mStartIndex
        val end = mEndIndexExclusive
        while (i < end) {
            if (saleProgressData.current >= levelList[i].target) {
                mPointOuterPaint!!.color =
                    if (isEnabled) mSecondProgressNormalColor else mOuterPointDisabledColor
            } else {
                mPointOuterPaint!!.color =
                    if (isEnabled) mProgressNormalColor else mOuterPointDisabledColor
            }
            canvas.drawCircle(
                currentX - mOuterPointRadius,
                halfProgressHeight,
                mOuterPointRadius,
                mPointOuterPaint!!
            )
            canvas.drawCircle(
                currentX - mOuterPointRadius,
                halfProgressHeight,
                mInnerPointRadius,
                mPointInnerPaint!!
            )
            currentX += eachWidth
            i++
        }
        canvas.translate(0f, (mProgressBarHeight + mProgressBarPaddingBottom).toFloat())
    }

    private fun drawBottomText(canvas: Canvas) {
        val saleProgressData: SaleProgressData =
            mSaleProgressData
                ?: return
        val levelList: List<SaleProgressData.Level> = saleProgressData.levelList
        val viewRight = right - paddingRight
        val progressBarWidth = progressBarWidth
        val levelCount = mEndIndexExclusive - mStartIndex
        val eachWidth = progressBarWidth * 1f / levelCount
        val paddingLeft = paddingLeft
        var currentX = (paddingLeft + mProgressPaddingLeft).toFloat()
        val fontMetrics = mAwardTextPaint!!.fontMetrics
        val title = getTitleText(saleProgressData.type)
        var textWidth = mTargetTextPaint!!.measureText(title)
        var textLeft = currentX - textWidth / 2
        if (textLeft < paddingLeft) {
            textLeft = paddingLeft.toFloat()
        }
        canvas.drawText(
            title,
            textLeft,
            -fontMetrics.ascent + fontMetrics.leading,
            mTargetTextPaint!!
        )
        currentX += eachWidth
        var i = mStartIndex
        val end = mEndIndexExclusive
        while (i < end) {
            val targetText = getTargetText(saleProgressData.type, levelList[i].target)
            textWidth = mTargetTextPaint!!.measureText(targetText)
            textLeft = currentX - textWidth / 2 - mOuterPointRadius
            val textRight = textLeft + textWidth
            if (textRight > viewRight) {
                textLeft = viewRight - textWidth
            }
            canvas.drawText(
                targetText,
                textLeft,
                -fontMetrics.ascent + fontMetrics.leading,
                mTargetTextPaint!!
            )
            currentX += eachWidth
            i++
        }
    }

    private val progressBarWidth: Int
        private get() {
            val availableWidth =
                measuredWidth - paddingLeft - paddingRight - mProgressPaddingLeft - mProgressPaddingRight
            return if (mNeedExtraSpace) availableWidth - mExtraSpace else availableWidth
        }

    fun setSaleProgressData(saleProgressData: SaleProgressData?) {
        mSaleProgressData = saleProgressData
        invalidate()
    }

    private fun getTitleText(type: Int): String {
        return when (type) {
            SaleProgressData.TYPE_SALES -> "销售额"
            SaleProgressData.TYPE_SALE_AMOUNT -> "销量"
            else -> ""
        }
    }

    private fun getTargetText(type: Int, target: Int): String {
        return when (type) {
            SaleProgressData.TYPE_SALES -> target.toString() + "元"
            SaleProgressData.TYPE_SALE_AMOUNT -> target.toString() + "组"
            else -> ""
        }
    }

    class SaleProgressData(
        val current: Double,
        /**
         * 类型： 1 - 销售额，2 - 销量
         */
        val type: Int,
        val levelList: List<Level>
    ) {

        class Level(val target: Int, val award: Int)
        companion object {
            const val TYPE_SALES = 1
            const val TYPE_SALE_AMOUNT = 2
        }
    }
}