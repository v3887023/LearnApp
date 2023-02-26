package com.zcx.learnapp

import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zcx.SubjectProvider
import com.zcx.learnapp.adapter.SubjectAdapter
import com.zcx.learnapp.base.BaseActivity

class MainActivity : BaseActivity() {
    private lateinit var subjectRv: RecyclerView

    private lateinit var subjectAdapter: SubjectAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        subjectRv = findViewById(R.id.subjectRv)

        val subjectEntities = SubjectProvider.getSubjectEntities()
        subjectAdapter = SubjectAdapter(subjectEntities)
        subjectRv.layoutManager = LinearLayoutManager(this)
        subjectRv.adapter = subjectAdapter
        subjectAdapter.setOnItemClickListener { _, _, position ->
            startActivity(Intent(this, Class.forName(subjectEntities[position].className)))
        }
    }

    override fun getLayoutId(): Int = R.layout.activity_main
}