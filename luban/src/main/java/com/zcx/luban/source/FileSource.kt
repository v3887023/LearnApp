package com.zcx.luban.source

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.exifinterface.media.ExifInterface
import java.io.File
import java.util.*

/**
 * File 形式的图片文件来源
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 */
class FileSource(private val file: File) : Source {
    override fun getName(): String {
        return file.name
    }

    override fun getLength(): Long {
        return file.length()
    }

    override fun decodeAsBitmap(options: BitmapFactory.Options?): Bitmap? {
        return BitmapFactory.decodeFile(file.absolutePath, options)
    }

    override fun getExifInterface(): ExifInterface? {
        var exifInterface: ExifInterface? = null
        val name = file.name.lowercase(Locale.getDefault())
        val isJpgFile = name.contains(".jpeg") || name.contains(".jpg")
        if (isJpgFile) {
            exifInterface = ExifInterface(file.absolutePath)
        }

        return exifInterface
    }
}