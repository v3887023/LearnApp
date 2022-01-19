package com.zcx.learnapp.activity

import android.os.Bundle
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.learnapp.guide.Guide
import com.zcx.learnapp.guide.GuidePage
import com.zcx.learnapp.guide.HighlightOptions
import com.zcx.learnapp.guide.TestGuideView
import com.zcx.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_guide.*
import kotlinx.android.synthetic.main.fragment_test.*

@Subject("引导")
class GuideActivity : BaseActivity() {
    companion object {
        private const val TAG = "GuideActivity"
    }

    override fun getLayoutId() = R.layout.activity_guide

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeTv.post {
            Guide.Builder(this)
                .addPage(
                    GuidePage.Builder()
                        .highlight(homeTv)
                        .guideView(TestGuideView())
                        .build()
                )
                .addPage(
                    GuidePage.Builder()
                        .highlight(contentTv)
                        .highlightOptions(HighlightOptions.ofOval().padding(10).trimToSquare())
                        .guideView(TestGuideView())
                        .build()
                )
                .build()
                .show()
        }
    }
}