package com.example.learnapp.activity

import android.os.Bundle
import android.widget.ArrayAdapter
import com.example.learnapp.R
import com.example.learnapp.base.BaseActivity
import com.example.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_list_test.*

@Subject("列表测试")
class ListTestActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_list_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val list = mutableListOf<String>()
        for (i in 0..20) {
            list += "$i"
        }
        listView.adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, list)
    }
}