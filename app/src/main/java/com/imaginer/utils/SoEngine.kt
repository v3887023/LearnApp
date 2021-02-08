package com.imaginer.utils

import android.graphics.Bitmap
import com.zcx.luban.CompressOptions
import com.zcx.luban.engine.CompressEngine
import com.zcx.luban.source.Source
import java.io.File
import java.io.IOException

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/1/28
 */
class SoEngine : CompressEngine {
    override fun compress(
        source: Source,
        compressOptions: CompressOptions,
        destFile: File
    ): File {

        var bitmap: Bitmap?
        val code: Int
        synchronized(SoEngine::class) {
            bitmap = source.decodeAsBitmap()
            code = CompressUtils.compressBitmap(
                bitmap, 50,
                destFile.absolutePath.toByteArray(),
                true
            )
        }

        bitmap?.takeUnless { it.isRecycled }?.recycle()
        bitmap = null

        if (1 == code) {
            return destFile
        }

        throw IOException("压缩失败")
    }
}