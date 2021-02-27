package com.zcx.luban.worker

import android.graphics.Bitmap
import com.zcx.luban.request.BaseRequestBuilder
import com.zcx.luban.result.SingleCompressResult
import com.zcx.luban.source.Source
import java.io.File

/**
 * 执行压缩操作的基类
 *
 * @Author: zcx
 * @CreateDate: 2021/2/3
 */
abstract class AbstractCompressWorker<T : BaseRequestBuilder<T>>(private val requestBuilder: T) {

    /**
     * 获取代表目标生成文件的 File 对象，使用 [Renamer] 的实现类进行文件重命名
     */
    private fun getDestFile(source: Source): File {
        val renamer = requestBuilder.renamer
        val outputDirectory = requestBuilder.outputDirectory

        // 拼接新的文件名
        val newFilename = "${renamer.renameFrom(source.getName())}.${getExtension()}"

        return File(outputDirectory, newFilename)
    }

    /**
     * 根据压缩格式，获取待生成文件的拓展名
     */
    private fun getExtension(): String {
        val compressFormat = requestBuilder.compressOptions.compressFormat

        // TODO 适配 Android 11 时，这里注释的代码要放开
//        if (Build.VERSION.SDK_INT >= 30 &&
//                (compressFormat == Bitmap.CompressFormat.WEBP_LOSSY || compressFormat == Bitmap.CompressFormat.WEBP_LOSSLESS)
//        ) {
//            return "webp"
//        }

        return when (compressFormat) {
            Bitmap.CompressFormat.JPEG -> "jpg"
            Bitmap.CompressFormat.PNG -> "png"
            Bitmap.CompressFormat.WEBP -> "webp"
            else -> ""
        }
    }

    /**
     * 压缩单个图片来源
     */
    protected fun compressSource(source: Source): SingleCompressResult {
        return try {
            val compressEngine = requestBuilder.compressEngine
            val options = requestBuilder.compressOptions

            val outputFile = compressEngine.compress(source, options, getDestFile(source))

            SingleCompressResult.success(outputFile)
        } catch (e: Exception) {
            SingleCompressResult.error(source, e)
        }
    }
}