package com.zcx.luban.engine

import com.zcx.luban.CompressOptions
import com.zcx.luban.source.Source
import java.io.File

/**
 * 图片压缩引擎接口
 *
 * @Author: zcx
 * @CreateDate: 2021/1/20
 */
interface CompressEngine {

    /**
     * 图片压缩操作。
     *
     *
     *
     * @param source          压缩来源
     * @param compressOptions 压缩选项
     * @param destFile        生成的目标文件
     */
    @Throws(Exception::class)
    fun compress(source: Source, compressOptions: CompressOptions, destFile: File): File
}