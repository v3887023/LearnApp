package com.zcx.luban.result

import com.zcx.luban.source.EmptySource
import com.zcx.luban.source.Source
import java.io.File

/**
 * 单个压缩结果
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 *
 * @param isSuccessful  是否压缩成功
 * @param outputFile  压缩成功时的输出文件，失败时则为 null
 * @param errorSource 压缩失败时带回的图片来源，成功时则为 [EmptySource]
 * @param exception   压缩失败时捕获到的异常，成功时则为 null
 */
class SingleCompressResult private constructor(
    val isSuccessful: Boolean = true,
    val outputFile: File? = null,
    val errorSource: Source = EmptySource,
    val exception: Exception? = null
) {

    companion object {
        /**
         * 单个压缩成功
         * @param outputFile 压缩完成后输出的文件
         */
        fun success(outputFile: File): SingleCompressResult {
            return SingleCompressResult(isSuccessful = true, outputFile = outputFile)
        }

        /**
         * 单个压缩失败
         *
         * @param errorSource 压缩失败的图片来源
         * @param exception 压缩失败时捕获的异常
         */
        fun error(errorSource: Source, exception: Exception? = null): SingleCompressResult {
            return SingleCompressResult(
                isSuccessful = false,
                errorSource = errorSource,
                exception = exception
            )
        }
    }
}