package com.zcx.learnapp.base.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.Gravity
import android.view.WindowManager

class BaseDialog(context: Context) : Dialog(context) {
    override fun onStart() {
        window?.attributes?.apply {
            width = WindowManager.LayoutParams.MATCH_PARENT
            height = WindowManager.LayoutParams.WRAP_CONTENT
            gravity = Gravity.CENTER
        }

        window?.setBackgroundDrawable(GradientDrawable().apply {
            cornerRadius = 48f
            setColor(Color.WHITE)
        })

        super.onStart()
    }
}