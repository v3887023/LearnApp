package com.example.learnapp.activity

import android.os.Bundle
import com.example.learnapp.R
import com.example.learnapp.base.BaseActivity
import com.example.lib_annotations.Subject

@Subject("自定义阶梯 View")
class LevelViewActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_level_view

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
}