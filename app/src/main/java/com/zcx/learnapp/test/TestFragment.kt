package com.zcx.learnapp.test

import android.view.View
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseFragment

class TestFragment: BaseFragment() {
    override fun getLayoutId() = R.layout.fragment_test

    override fun initViews(view: View) {

    }

    override fun getStatusBarColor(): Int {
        return resources.getColor(android.R.color.holo_blue_light)
    }
}