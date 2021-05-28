package com.zcx.learnapp.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.util.Linkify
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnLongClickListener
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

class ViewHolder(private val mContext: Context, private val convertView: View) :
    RecyclerView.ViewHolder(
        convertView
    ) {
    private val mViews: SparseArray<View?> = SparseArray()

    /**
     * 通过viewId获取控件
     *
     * @param viewId
     * @return
     */
    fun <T : View?> getView(viewId: Int): T? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as T?
    }

    fun getTextView(viewId: Int): TextView? {
        var view = mViews[viewId]
        if (view == null) {
            view = convertView.findViewById(viewId)
            mViews.put(viewId, view)
        }
        return view as TextView?
    }
    /****以下为辅助方法 */
    /**
     * 设置TextView的值
     *
     * @param viewId
     * @param text
     * @return
     */
    fun setText(viewId: Int, text: String?): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = text
        return this
    }

    fun setText(viewId: Int, id: Int): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.setText(id)
        return this
    }

    fun setText(viewId: Int, id: CharSequence?): ViewHolder {
        val tv = getView<TextView>(viewId)!!
        tv.text = id
        return this
    }

    fun setViewVisibility(viewId: Int, type: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        when (type) {
            0 -> view.visibility = View.VISIBLE
            1 -> view.visibility = View.INVISIBLE
            2 -> view.visibility = View.GONE
            else -> {
            }
        }
        return this
    }

    fun setImageResource(viewId: Int, resId: Int): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageResource(resId)
        return this
    }

    fun setImageBitmap(viewId: Int, bitmap: Bitmap?): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageBitmap(bitmap)
        return this
    }

    fun setImageDrawable(viewId: Int, drawable: Drawable?): ViewHolder {
        val view = getView<ImageView>(viewId)!!
        view.setImageDrawable(drawable)
        return this
    }

    fun setBackgroundColor(viewId: Int, color: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundColor(color)
        return this
    }

    fun setBackgroundRes(viewId: Int, backgroundRes: Int): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setBackgroundResource(backgroundRes)
        return this
    }

    fun setTextColor(viewId: Int, textColor: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(textColor)
        return this
    }

    fun setTextColorRes(viewId: Int, textColorRes: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setTextColor(mContext.resources.getColor(textColorRes))
        return this
    }

    fun setTextBackgroundDrawable(viewId: Int, textBackground: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        view.setBackgroundDrawable(mContext.resources.getDrawable(textBackground))
        return this
    }

    fun setAlpha(viewId: Int, value: Float): ViewHolder {
        getView<View>(viewId)!!.alpha = value
        return this
    }

    fun setVisible(viewId: Int, visible: Boolean): ViewHolder {
        val view = getView<View>(viewId)!!
        view.visibility = if (visible) View.VISIBLE else View.GONE
        return this
    }

    fun linkify(viewId: Int): ViewHolder {
        val view = getView<TextView>(viewId)!!
        Linkify.addLinks(view, Linkify.ALL)
        return this
    }

    fun setTypeface(typeface: Typeface?, vararg viewIds: Int): ViewHolder {
        for (viewId in viewIds) {
            val view = getView<TextView>(viewId)!!
            view.typeface = typeface
            view.paintFlags = view.paintFlags or Paint.SUBPIXEL_TEXT_FLAG
        }
        return this
    }

    fun setProgress(viewId: Int, progress: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.progress = progress
        return this
    }

    fun setProgress(viewId: Int, progress: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        view.progress = progress
        return this
    }

    fun setMax(viewId: Int, max: Int): ViewHolder {
        val view = getView<ProgressBar>(viewId)!!
        view.max = max
        return this
    }

    fun setRating(viewId: Int, rating: Float): ViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.rating = rating
        return this
    }

    fun setRating(viewId: Int, rating: Float, max: Int): ViewHolder {
        val view = getView<RatingBar>(viewId)!!
        view.max = max
        view.rating = rating
        return this
    }

    fun setTag(viewId: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.tag = tag
        return this
    }

    fun setTag(viewId: Int, key: Int, tag: Any?): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setTag(key, tag)
        return this
    }

    fun setChecked(viewId: Int, checked: Boolean): ViewHolder {
        val view: Checkable = getView(viewId)!!
        view.isChecked = checked
        return this
    }

    /**
     * 关于事件的
     */
    fun setOnClickListener(
        viewId: Int,
        listener: View.OnClickListener?
    ): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnClickListener(listener)
        return this
    }

    fun setOnTouchListener(
        viewId: Int,
        listener: OnTouchListener?
    ): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnTouchListener(listener)
        return this
    }

    fun setOnLongClickListener(
        viewId: Int,
        listener: OnLongClickListener?
    ): ViewHolder {
        val view = getView<View>(viewId)!!
        view.setOnLongClickListener(listener)
        return this
    }

    /**
     * 添加控件
     */
    fun addFrameLayout(viewId: Int, v: View?, type: Int): ViewHolder {
        val view = getView<FrameLayout>(viewId)!!
        view.addView(v, type)
        // view.addView(v,type);
        return this
    }

    companion object {
        fun createViewHolder(context: Context, itemView: View): ViewHolder {
            return ViewHolder(context, itemView)
        }

        @JvmStatic
        fun createViewHolder(
            context: Context,
            parent: ViewGroup?, layoutId: Int
        ): ViewHolder {
            val itemView = LayoutInflater.from(context).inflate(
                layoutId, parent,
                false
            )
            return ViewHolder(context, itemView)
        }
    }

}