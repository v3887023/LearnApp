package com.example.learnapp.activity

import android.os.Bundle
import android.widget.Toast
import com.example.learnapp.R
import com.example.learnapp.base.BaseActivity

class BlankActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_blank

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Toast.makeText(this, "Success！", Toast.LENGTH_SHORT).show()
    }
}