package com.zcx.luban.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface

/**
 * 空的图片来源，提供默认实现
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 */
object EmptySource : Source {
    override fun getName() = ""

    override fun getLength() = 0L

    override fun decodeAsBitmap(options: BitmapFactory.Options?): Bitmap? = null

    override fun getExifInterface(): ExifInterface? = null
}