package com.zcx.luban.source

import android.content.ContentResolver
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.exifinterface.media.ExifInterface
import java.io.FileDescriptor
import java.util.*

/**
 * Uri 形式的图片来源
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 */
class UriSource(private val contentResolver: ContentResolver, private val uri: Uri) : Source {

    /**
     * 获取给定 [uri] 的文件描述符
     */
    private fun getFileDescriptor(): FileDescriptor? {
        return contentResolver.openAssetFileDescriptor(uri, "r")?.fileDescriptor
    }

    /**
     * 使用懒加载获取并缓存 [uri] 的信息
     */
    private val uriInfo: UriInfo by lazy {
        val projection = arrayOf(MediaStore.MediaColumns.DISPLAY_NAME, MediaStore.MediaColumns.SIZE)

        contentResolver.query(uri, projection, null, null, null)?.use {
            if (it.moveToNext()) {
                return@lazy with(it) {
                    UriInfo(getString(0), getLong(1))
                }
            }
        }

        UriInfo.EMPTY
    }

    override fun getName(): String {
        return uriInfo.name
    }

    override fun getLength(): Long {
        return uriInfo.length
    }

    override fun decodeAsBitmap(options: BitmapFactory.Options?): Bitmap? {
        val fileDescriptor = getFileDescriptor() ?: return null
        return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options)
    }

    override fun getExifInterface(): ExifInterface? {
        val fileDescriptor = getFileDescriptor() ?: return null

        var exifInterface: ExifInterface? = null
        val name = uriInfo.name.lowercase(Locale.getDefault())
        val isJpgFile = name.contains(".jpeg") || name.contains(".jpg")
        if (isJpgFile) {
            exifInterface = ExifInterface(fileDescriptor)
        }

        return exifInterface
    }

    /**
     * Uri 信息类，保存 uri 对应文件的文件名和文件大小
     */
    private class UriInfo(val name: String, val length: Long) {
        companion object {
            val EMPTY = UriInfo("", 0L)
        }
    }
}