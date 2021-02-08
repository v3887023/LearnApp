package com.zcx.luban

import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat

/**
 * 压缩选项
 *
 * @Author: zcx
 * @CreateDate: 2021/1/20
 */
class CompressOptions {
    var maxSize: Long = -1L
    var compressFormat: CompressFormat = CompressFormat.JPEG
    var bitmapConfig: Bitmap.Config = Bitmap.Config.ARGB_8888
    var quality: Int = 100
    var useInSample: Boolean = true

    /**
     * 更新压缩选项
     *
     * @param newOptions 新的压缩选项
     */
    fun update(newOptions: CompressOptions) {
        this.maxSize = newOptions.maxSize
        this.compressFormat = newOptions.compressFormat
        this.bitmapConfig = newOptions.bitmapConfig
        this.quality = newOptions.quality
        this.useInSample = newOptions.useInSample
    }

    companion object {
        fun ofQuality(quality: Int): CompressOptions {
            return CompressOptions().apply { this.quality = quality }
        }
    }
}