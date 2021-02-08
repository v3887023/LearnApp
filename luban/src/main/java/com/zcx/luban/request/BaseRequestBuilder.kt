package com.zcx.luban.request

import com.zcx.luban.CompressListener
import com.zcx.luban.CompressOptions
import com.zcx.luban.DefaultRenamer
import com.zcx.luban.Renamer
import com.zcx.luban.engine.CompressEngine
import com.zcx.luban.engine.LubanEngine
import java.io.File
import java.util.concurrent.Executor

/**
 * 构建压缩请求的基类。提供压缩引擎、压缩质量、压缩选项等配置
 *
 * @Author: zcx
 * @CreateDate: 2021/1/21
 */
open class BaseRequestBuilder<T : BaseRequestBuilder<T>> {
    /**
     * 压缩引擎，默认为鲁班引擎 [LubanEngine]
     */
    internal var compressEngine: CompressEngine = LubanEngine()
        private set

    /**
     * 压缩成功时文件的输出目录
     */
    internal var outputDirectory: File? = null
        private set

    /**
     * 压缩成功时，使用到的文件重命名接口
     */
    internal var renamer: Renamer = DefaultRenamer()
        private set

    /**
     * 压缩选项
     */
    internal var compressOptions: CompressOptions = CompressOptions()
        private set

    /**
     * 异步压缩使用的线程池
     */
    internal var executor: Executor? = null
        private set

    /**
     * 压缩监听
     */
    internal var compressListener: CompressListener? = null
        private set

    /**
     * 配置压缩引擎
     */
    fun compressEngine(compressEngine: CompressEngine): T {
        this.compressEngine = compressEngine
        return self()
    }

    /**
     * 配置压缩质量
     */
    fun quality(quality: Int): T {
        return compressOptions(CompressOptions.ofQuality(quality))
    }

    /**
     * 配置压缩选项
     */
    fun compressOptions(compressOptions: CompressOptions): T {
        this.compressOptions.update(compressOptions)
        return self()
    }

    /**
     * 配置压缩成功时文件的输出目录
     */
    fun outputAt(outputDirectory: File): T {
        this.outputDirectory = outputDirectory
        return self()
    }

    /**
     * 配置文件重命名操作
     */
    fun fileRenameBy(renamer: Renamer): T {
        this.renamer = renamer
        return self()
    }

    /**
     * 配置线程池
     */
    fun executeOn(executor: Executor): T {
        this.executor = executor
        return self()
    }

    /**
     * 配置压缩监听
     */
    fun compressListener(compressListener: CompressListener): T {
        this.compressListener = compressListener
        return self()
    }

    @Suppress("UNCHECKED_CAST")
    private fun self(): T {
        return this as T
    }
}