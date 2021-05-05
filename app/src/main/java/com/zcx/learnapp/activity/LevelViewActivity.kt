package com.zcx.learnapp.activity

import android.os.Bundle
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject

@Subject("自定义阶梯 View")
class LevelViewActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_level_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}