package com.zcx.learnapp.adapter

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

abstract class BaseAdapter<T>(protected var mContext: Context, protected var mData: List<T>?) :
    RecyclerView.Adapter<ViewHolder>() {
    abstract fun getLayoutId(itemViewType: Int): Int
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.createViewHolder(mContext, parent, getLayoutId(viewType))
    }

    override fun getItemCount(): Int {
        val data = mData
        return data?.size ?: 0
    }

    fun getItem(position: Int): T? {
        val data = mData
        return if (data != null && 0 <= position && position < data.size) {
            data[position]
        } else null
    }
}