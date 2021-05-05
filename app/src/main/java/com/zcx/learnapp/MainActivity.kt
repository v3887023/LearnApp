package com.zcx.learnapp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.zcx.SubjectProvider
import com.zcx.learnapp.adapter.SubjectAdapter
import com.zcx.learnapp.base.BaseActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {
    private lateinit var subjectAdapter: SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val subjectEntities = SubjectProvider.getSubjectEntities()
        subjectAdapter = SubjectAdapter(subjectEntities)
        rv_subject.layoutManager = LinearLayoutManager(this)
        rv_subject.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { _, _, position ->
            startActivity(Intent(this, Class.forName(subjectEntities[position].className)))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main
}