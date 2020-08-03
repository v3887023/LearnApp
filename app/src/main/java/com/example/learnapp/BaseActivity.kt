package com.example.learnapp

import android.app.Activity
import android.os.Bundle
import butterknife.ButterKnife
import butterknife.Unbinder

abstract class BaseActivity : Activity() {
    private lateinit var unbinder: Unbinder

    abstract fun getLayoutId(): Int

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        unbinder = ButterKnife.bind(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (::unbinder.isInitialized) {
            unbinder.unbind()
        }
    }
}