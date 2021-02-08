package com.zcx.luban.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface

/**
 * 图片来源的抽象。图片来源可能有 File、Uri 或者 流 等。
 * 目前提供三种实现，有 File 的 [FileSource]，Uri 的 [UriSource] 以及一个空实现 [EmptySource]。
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 */
interface Source {
    /**
     * 图片名称
     */
    fun getName(): String

    /**
     * 图片大小，单位为字节
     */
    fun getLength(): Long

    /**
     * 将该图片来源解码成一个 Bitmap 对象并返回。
     *
     * @param options 解码选项
     * @return 解码成功的 Bitmap 对象，可为 null
     */
    @Throws(Exception::class)
    fun decodeAsBitmap(options: BitmapFactory.Options? = null): Bitmap?

    /**
     * 获取 Exif 信息。如果该图片来源不支持，则为 null
     */
    fun getExifInterface(): ExifInterface?
}