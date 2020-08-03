package com.example.learnapp

import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.example.lib_annotations.entity.SubjectEntity

class SubjectAdapter(subjectEntities: List<SubjectEntity>) :
    BaseQuickAdapter<SubjectEntity, BaseViewHolder>(R.layout.item_subject, subjectEntities) {

    override fun convert(helper: BaseViewHolder, item: SubjectEntity) {
        helper.setText(R.id.tv_title, item.title)
        helper.setText(R.id.tv_description, item.description)
        helper.setVisible(R.id.tv_tag, item.isTest)
    }
}