package com.example.learnapp.activity

import android.view.View
import com.example.learnapp.R
import com.example.learnapp.base.BaseActivity
import com.example.learnapp.base.dialog.*
import com.example.lib_annotations.Subject

@Subject("对话框", "打造一个通用的对话框")
class DialogTestActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_dialog_test

    fun showDialog(view: View) {
        DialogBuilder(this)
            .addPart(TitlePart("这是标题"))
            .addPart(ContentPart("这是内容"))
            .addPart(HorizontalLinePart(3))
            .addPart(ButtonPart())
            .build()
            .show()
    }
}