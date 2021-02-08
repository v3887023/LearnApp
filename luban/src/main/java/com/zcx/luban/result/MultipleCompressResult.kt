package com.zcx.luban.result

import com.zcx.luban.source.Source
import java.io.File

/**
 * 批量压缩结果
 *
 * @Author: zcx
 * @CreateDate: 2021/1/27
 */
class MultipleCompressResult {
    /**
     * 是否全部压缩成功
     */
    var isAllSuccessful: Boolean = true

    /**
     * 压缩成功生成的文件列表
     */
    val outputFileList: MutableList<File> = mutableListOf()

    /**
     * 压缩失败的图片来源列表
     */
    val errorSourceList: MutableList<Source> = mutableListOf()

    /**
     * 压缩失败捕获到的异常，可为空
     */
    var exception: Exception? = null

    /**
     * 组合单次压缩结果
     */
    fun composeWith(singleCompressResult: SingleCompressResult) {
        if (singleCompressResult.isSuccessful) {
            singleCompressResult.outputFile?.let { outputFileList += it }
        } else {
            errorSourceList += singleCompressResult.errorSource
            exception = singleCompressResult.exception
            isAllSuccessful = false
        }
    }
}