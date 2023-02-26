package com.zcx.learnapp.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zcx.learnapp.R
import com.zcx.learnapp.adapter.SimpleTextAdapter
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject

@Subject("列表测试")
class ListTestActivity : BaseActivity() {
    private lateinit var itemRv: RecyclerView
    override fun getLayoutId() = R.layout.activity_list_test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        itemRv = findViewById(R.id.itemRv)

        itemRv.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
        val list = ArrayList<String>().apply {
            repeat(20) {
                add("$it")
            }
        }
        itemRv.adapter = SimpleTextAdapter(this, list)
    }
}