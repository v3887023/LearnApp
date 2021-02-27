package com.zcx.luban.worker

import com.zcx.luban.CompressCallback
import com.zcx.luban.executor.Executors
import com.zcx.luban.request.MultipleRequestBuilder
import com.zcx.luban.result.MultipleCompressResult
import com.zcx.luban.result.SingleCompressResult
import com.zcx.luban.source.Source
import java.util.concurrent.CountDownLatch

/**
 * @Author: zcx
 * @CreateDate: 2021/1/21
 */
class MultipleCompressWorker(private val multipleRequestBuilder: MultipleRequestBuilder) :
    AbstractCompressWorker<MultipleRequestBuilder>(multipleRequestBuilder) {

    private val sources = multipleRequestBuilder.sources

    /**
     * 同步压缩
     */
    fun execute(): MultipleCompressResult {
        multipleRequestBuilder.compressListener?.onCompressStart()

        val multipleCompressResult = MultipleCompressResult()

        for (source in sources) {
            val result = compressSource(source)
            multipleCompressResult.composeWith(result)
        }

        multipleRequestBuilder.compressListener?.onCompressEnd()

        return multipleCompressResult
    }

    /**
     * 异步压缩
     */
    fun enqueue(callback: CompressCallback<MultipleCompressResult>) {
        val executor = multipleRequestBuilder.executor ?: Executors.defaultExecutor()

        Executors.workerExecutor().execute {
            val latch = CountDownLatch(sources.size)

            val jobs = mutableListOf<CompressJob>()
            // 把多个图片来源分成单个的压缩任务进行
            sources.forEach { jobs.add(CompressJob(it, latch)) }

            multipleRequestBuilder.compressListener?.onCompressStart()

            // 多线程压缩
            jobs.forEach { executor.execute(it) }

            // 等待子任务完成
            latch.await()

            // 子任务压缩完成后，组合压缩结果
            val multipleCompressResult = MultipleCompressResult()
            jobs.forEach { multipleCompressResult.composeWith(it.compressResult) }

            multipleRequestBuilder.compressListener?.onCompressEnd()

            callback.onFinish(multipleCompressResult)
        }
    }

    /**
     * 将批量压缩请求分成单个的压缩任务单独压缩，使用 [CountDownLatch] 控制并发
     */
    private inner class CompressJob(
        private val source: Source,
        private val latch: CountDownLatch
    ) : Runnable {

        /**
         * 保存单个压缩结果
         */
        lateinit var compressResult: SingleCompressResult
            private set

        override fun run() {
            compressResult = compressSource(source)
            latch.countDown()
        }
    }
}