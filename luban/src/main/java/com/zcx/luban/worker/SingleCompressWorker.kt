package com.yunji.imaginer.luban.worker

import com.zcx.luban.CompressCallback
import com.zcx.luban.executor.Executors
import com.zcx.luban.request.SingleRequestBuilder
import com.zcx.luban.result.SingleCompressResult

/**
 * 执行单个压缩操作的类
 *
 * @Author: zcx
 * @CreateDate: 2021/1/21
 */
class SingleCompressWorker(private val singleRequestBuilder: SingleRequestBuilder) :
    AbstractCompressWorker<SingleRequestBuilder>(singleRequestBuilder) {

    /**
     * 同步压缩
     */
    fun execute(): SingleCompressResult {
        singleRequestBuilder.compressListener?.onCompressStart()

        return compressSource(singleRequestBuilder.source).also {
            singleRequestBuilder.compressListener?.onCompressEnd()
        }
    }

    /**
     * 异步压缩
     */
    fun enqueue(callback: CompressCallback<SingleCompressResult>) {
        val executor = singleRequestBuilder.executor ?: Executors.defaultExecutor()
        executor.execute {
            val compressResult = execute()
            callback.onFinish(compressResult)
        }
    }
}