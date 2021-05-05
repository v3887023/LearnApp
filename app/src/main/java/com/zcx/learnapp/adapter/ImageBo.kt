package com.zcx.learnapp.adapter

import android.graphics.BitmapFactory
import com.zcx.luban.source.Source
import java.io.File
import java.io.Serializable

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/1/28
 */
class ImageBo : Serializable {
    var source: Source? = null
        set(value) {
            originResolution = RESOLUTION_INITIAL
            field = value
        }
    var lubanFile: File? = null
        set(value) {
            lubanResolution = RESOLUTION_INITIAL
            field = value
        }
    var soFile: File? = null
        set(value) {
            soResolution = RESOLUTION_INITIAL
            field = value
        }

    private var originResolution: String = RESOLUTION_INITIAL
    private var lubanResolution: String = RESOLUTION_INITIAL
    private var soResolution: String = RESOLUTION_INITIAL

    fun getOriginResolution(): String {
        var originResolution = this.originResolution
        if (originResolution == RESOLUTION_INITIAL) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val source = this.source

            originResolution = if (source == null) {
                "-"
            } else {
                try {
                    source.decodeAsBitmap(options)
                    "${options.outWidth}x${options.outHeight}"
                } catch (e: Exception) {
                    "解析出错"
                }
            }
            this.originResolution = originResolution
        }

        return originResolution
    }

    fun getOriginSize(): Long {
        return source?.getLength() ?: SIZE_INITIAL
    }

    fun getLubanResolution(): String {
        var lubanResolution = this.lubanResolution
        if (lubanResolution == RESOLUTION_INITIAL) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val lubanFile = this.lubanFile

            lubanResolution = if (lubanFile == null) {
                "-"
            } else {
                try {
                    BitmapFactory.decodeFile(lubanFile.absolutePath, options)
                    "${options.outWidth}x${options.outHeight}"
                } catch (e: Exception) {
                    "解析出错"
                }
            }
            this.lubanResolution = lubanResolution
        }

        return lubanResolution
    }

    fun getLubanSize(): Long {
        return lubanFile?.length() ?: SIZE_INITIAL
    }

    fun getSoResolution(): String {
        var soResolution = this.soResolution
        if (soResolution == RESOLUTION_INITIAL) {
            val options = BitmapFactory.Options()
            options.inJustDecodeBounds = true
            val soFile = this.soFile

            soResolution = if (soFile == null) {
                "-"
            } else {
                try {
                    BitmapFactory.decodeFile(soFile.absolutePath, options)
                    "${options.outWidth}x${options.outHeight}"
                } catch (e: Exception) {
                    "解析出错"
                }
            }
            this.soResolution = soResolution
        }

        return soResolution
    }

    fun getSoSize(): Long {
        return soFile?.length() ?: SIZE_INITIAL
    }

    companion object {
        const val SIZE_INITIAL = -1L
        const val SIZE_ERROR = -2L
        const val RESOLUTION_INITIAL = ""
    }
}