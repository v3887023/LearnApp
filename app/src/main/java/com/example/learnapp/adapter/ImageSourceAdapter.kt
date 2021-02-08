package com.example.learnapp.adapter

import com.bm.library.PhotoView
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.learnapp.R

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/2/5
 */
class ImageSourceAdapter(list: List<SourceBo>) :
    BaseQuickAdapter<SourceBo, BaseViewHolder>(R.layout.item_image_source, list) {
    override fun convert(helper: BaseViewHolder, item: SourceBo) {
        val photoView = helper.getView<PhotoView>(R.id.photoView)
        photoView.enable()

        Glide.with(mContext).load(item.path).into(photoView)
        helper.setText(R.id.nameTv, item.name)
    }
}