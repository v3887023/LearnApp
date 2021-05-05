package com.zcx.learnapp.adapter

import android.graphics.Color
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.BaseViewHolder
import com.zcx.learnapp.R

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/1/28
 */
class ImageAdapter(data: List<ImageBo>) :
    BaseQuickAdapter<ImageBo, BaseViewHolder>(R.layout.item_image, data) {
    companion object {

        const val ZERO = 0L
        const val KB = 1024L
        const val MB = 1024 * KB

        val COLOR_HIGHLIGHT = Color.parseColor("#e3f2fd")
        const val COLOR_WHITE = Color.WHITE

        const val COLOR_TEXT_NORMAL = Color.DKGRAY
        const val COLOR_TEXT_ERROR = Color.RED
    }

    private var showResolution = false

    override fun convert(helper: BaseViewHolder, item: ImageBo) {
        val position = helper.adapterPosition
        helper.itemView.setBackgroundColor(if (position and 1 == 1) COLOR_HIGHLIGHT else COLOR_WHITE)

        val originSize = item.getOriginSize()
        val lubanSize = item.getLubanSize()
        val soSize = item.getSoSize()
        helper.setText(R.id.originSizeTv, formatSize(originSize))

        helper.setTextColor(
            R.id.lubanSizeTv,
            if (lubanSize == ImageBo.SIZE_ERROR) COLOR_TEXT_ERROR else COLOR_TEXT_NORMAL
        )

        helper.setTextColor(
            R.id.soSizeTv,
            if (soSize == ImageBo.SIZE_ERROR) COLOR_TEXT_ERROR else COLOR_TEXT_NORMAL
        )

        helper.setText(
            R.id.lubanSizeTv,
            "${formatSize(lubanSize)}|${getPercent(lubanSize, originSize)}"
        )
        helper.setText(
            R.id.soSizeTv,
            "${formatSize(soSize)}|${getPercent(soSize, originSize)}"
        )

        if (showResolution) {
            helper.setText(R.id.originResolutionTv, item.getOriginResolution())
            helper.setText(R.id.lubanResolutionTv, item.getLubanResolution())
            helper.setText(R.id.soResolutionTv, item.getSoResolution())
            helper.setGone(R.id.originResolutionTv, true)
            helper.setGone(R.id.lubanResolutionTv, true)
            helper.setGone(R.id.soResolutionTv, true)
        } else {
            helper.setGone(R.id.originResolutionTv, false)
            helper.setGone(R.id.lubanResolutionTv, false)
            helper.setGone(R.id.soResolutionTv, false)
        }
    }

    private fun getPercent(divisor: Long, divider: Long): String {
        return String.format("%.1f%%", divisor * 100.0 / divider)
    }

    private fun formatSize(byteCount: Long): String {
        return when (byteCount) {
            ImageBo.SIZE_INITIAL -> "-"
            ImageBo.SIZE_ERROR -> "失败"
            in ZERO until KB -> "${byteCount}B"
            in KB until MB -> String.format("%7.1fKB", byteCount.toDouble() / KB)
            else -> String.format("%7.1fMB", byteCount.toDouble() / MB)
        }
    }

    fun showResolution(show: Boolean) {
        this.showResolution = show
        notifyDataSetChanged()
    }
}