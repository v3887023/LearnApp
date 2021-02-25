package com.example.learnapp.base

import android.app.Activity
import android.os.Bundle
import android.view.Gravity
import android.widget.Toast
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

    fun String.toast() {
        runOnUiThread {
            Toast.makeText(this@BaseActivity, this, Toast.LENGTH_SHORT)
                .apply { setGravity(Gravity.CENTER, 0, 0) }
                .show()
        }
    }
}