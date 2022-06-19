package com.zcx.learnapp.activity

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject

@Subject("生命周期")
class LifecycleActivity : BaseLifecycleActivity() {
    private var name: String = "A"
    private var showButton = true

    companion object {
        private const val KEY_NAME = "name"
        private const val KEY_SHOW_BUTTON = "show_button"
    }

    override fun getLayoutId() = R.layout.activity_lifecycle

    override fun onCreate(savedInstanceState: Bundle?) {
        intent?.let {
            name = it.getStringExtra(KEY_NAME) ?: "A"
            showButton = it.getBooleanExtra(KEY_SHOW_BUTTON, true)
        }

        super.onCreate(savedInstanceState)

        findViewById<TextView>(R.id.nameTv)?.text = name

        findViewById<View>(R.id.button)?.apply {
            visibility = if (showButton) View.VISIBLE else View.GONE

            setOnClickListener {
                val intent = Intent(this@LifecycleActivity, LifecycleActivity::class.java)
                intent.putExtra(KEY_NAME, "B")
                intent.putExtra(KEY_SHOW_BUTTON, false)
                startActivity(intent)
            }
        }
    }

    override fun getName() = name
}

abstract class BaseLifecycleActivity : BaseActivity() {
    companion object {
        private const val TAG = "Lifecycle"
    }

    abstract fun getName(): String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "${getName()} onCreate")
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "${getName()} onStart")
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "${getName()} onResume")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "${getName()} onPause")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "${getName()} onStop")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "${getName()} onDestroy")
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.d(TAG, "${getName()} onSaveInstanceState")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        Log.d(TAG, "${getName()} onRestoreInstanceState")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "${getName()} onRestart")
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        Log.d(TAG, "${getName()} onConfigurationChanged")
    }
}

