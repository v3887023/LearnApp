package com.zcx.learnapp.activity

import android.view.View
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.learnapp.base.dialog.*
import com.zcx.lib_annotations.Subject

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