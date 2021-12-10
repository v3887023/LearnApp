package com.zcx.learnapp.activity

import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.widget.FrameLayout
import android.widget.PopupWindow
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_guide.*
import java.util.*

@Subject("引导")
class GuideActivity : BaseActivity() {
    companion object {
        private const val TAG = "GuideActivity"
    }

    private var heightLightLayout: HeightLightLayout? = null

    override fun getLayoutId() = R.layout.activity_guide

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeTv.viewTreeObserver.addOnGlobalLayoutListener {
            val heightLightLayout = this.heightLightLayout
            if (heightLightLayout != null) {
                val location = IntArray(2)

                Log.d(TAG, "left: ${homeTv.left}, top: ${homeTv.top}")

                homeTv.getLocationInWindow(location)
                Log.d(TAG, "onConfigurationChanged location: ${location.contentToString()}")
                val rect = Rect(
                    location[0],
                    location[1],
                    location[0] + homeTv.width,
                    location[1] + homeTv.height
                )
                heightLightLayout.rect = rect
                heightLightLayout.invalidate()
            }
        }

        homeTv.post {
            val location = IntArray(2)
            homeTv.getLocationInWindow(location)

            Log.d(TAG, "location: ${location.contentToString()}")
            val rect = Rect(
                location[0],
                location[1],
                location[0] + homeTv.width,
                location[1] + homeTv.height
            )
            Log.d(TAG, "rect = $rect")

            PopupWindow(-1, -1)
                .apply {
                    isClippingEnabled = false
                    contentView =
                        HeightLightLayout(this@GuideActivity).apply {
                            this.rect.set(rect)
                            setBackgroundColor(
                                Color.parseColor(
                                    "#80000000"
                                )
                            )
                        }.apply { heightLightLayout = this }
                    contentView.setOnClickListener { dismiss() }
                }
                .showAtLocation(homeTv, Gravity.NO_GRAVITY, 0, 0)
        }
    }

    class HeightLightLayout(context: Context) : FrameLayout(context) {
        private val paint = Paint()

        init {
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.CLEAR)
        }

        var rect: Rect = Rect()

        override fun onDraw(canvas: Canvas) {
            canvas.drawRect(rect, paint)
        }
    }
}