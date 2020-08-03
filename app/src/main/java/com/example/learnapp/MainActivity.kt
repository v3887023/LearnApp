package com.example.learnapp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.SubjectProvider
import com.example.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_main.*

@Subject("主页")
class MainActivity : BaseActivity() {
    private lateinit var subjectAdapter: SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val subjectEntities = SubjectProvider.getSubjectEntities()
        subjectAdapter = SubjectAdapter(subjectEntities)
        rv_subject.layoutManager = LinearLayoutManager(this)
        rv_subject.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { adapter, view, position ->
            startActivity(Intent(this, Class.forName(subjectEntities[position].className)))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main
}