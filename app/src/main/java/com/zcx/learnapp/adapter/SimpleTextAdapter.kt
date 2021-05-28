package com.zcx.learnapp.adapter

import android.content.Context
import android.widget.TextView
import com.zcx.learnapp.R

class SimpleTextAdapter(private val context: Context, private val data: List<String>) :
    BaseAdapter<String>(context, data) {

    override fun getLayoutId(itemViewType: Int) = R.layout.item_text

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.getView<TextView>(R.id.textTv)?.text = getItem(position)
    }
}