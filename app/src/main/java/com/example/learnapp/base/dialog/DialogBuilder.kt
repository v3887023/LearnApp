package com.example.learnapp.base.dialog

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import com.example.learnapp.R

class DialogBuilder(context: Context) {
    private val contentView = LinearLayout(context).apply {
        orientation = LinearLayout.VERTICAL
        layoutParams = ViewGroup.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
    }

    private val dialog = BaseDialog(context).apply {
        setContentView(contentView)
    }

    fun addPart(dialogPart: DialogPart): DialogBuilder {
        contentView.addView(dialogPart.onCreateView(contentView.context, contentView))
        return this
    }

    fun build(): Dialog {
        return dialog
    }
}

interface DialogPart {
    fun onCreateView(context: Context, parent: ViewGroup): View
}

abstract class BaseDialogPart : DialogPart {
    @LayoutRes
    protected open var layoutId: Int = 0

    override fun onCreateView(context: Context, parent: ViewGroup): View {
        var view = createView(context, parent)
        if (view == null) {
            view = LayoutInflater.from(parent.context)
                .inflate(layoutId, parent, false)
        }

        return view!!.apply { initView(this) }
    }

    open fun createView(context: Context, parent: ViewGroup): View? {
        return null
    }

    open fun initView(view: View) {

    }
}

class TitlePart(private val title: String) : BaseDialogPart() {
    override var layoutId = R.layout.dialog_part_title

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.titleTv).text = title
    }
}

class ContentPart(private val content: String) : BaseDialogPart() {
    override var layoutId = R.layout.dialog_part_content

    override fun initView(view: View) {
        view.findViewById<TextView>(R.id.contentTv).text = content
    }
}

class HorizontalLinePart(
    private val dp: Int = 1,
    private val lineColor: Int = Color.parseColor("#EEEEEE")
) :
    BaseDialogPart() {

    override fun createView(context: Context, parent: ViewGroup): View {
        return View(context)
    }

    override fun initView(view: View) {
        view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dp)

        view.background = GradientDrawable().apply {
            setColor(lineColor)
        }
    }
}

class ButtonPart : BaseDialogPart() {
    override var layoutId = R.layout.dialog_part_button

    override fun initView(view: View) {

    }
}