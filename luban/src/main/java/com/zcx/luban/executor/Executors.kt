package com.zcx.luban.executor

import java.util.concurrent.ArrayBlockingQueue
import java.util.concurrent.Executor
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * 鲁班压缩工具默认用到的线程池
 *
 * @Description:
 * @Author: zcx
 * @CreateDate: 2021/1/25
 */
class Executors {
    companion object {
        private var DEFAULT_EXECUTOR: Executor? = null
        private var WORKER_EXECUTOR: Executor? = null

        fun defaultExecutor(): Executor {
            // TODO 如何配置线程池
            return DEFAULT_EXECUTOR ?: ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() + 1,
                10,
                60L,
                TimeUnit.SECONDS,
                ArrayBlockingQueue(50),
                DefaultThreadFactory("Luban"),
                ThreadPoolExecutor.DiscardPolicy()
            ).also { DEFAULT_EXECUTOR = it }
        }

        internal fun workerExecutor(): Executor {
            // TODO 如何配置线程池
            return WORKER_EXECUTOR ?: ThreadPoolExecutor(
                Runtime.getRuntime().availableProcessors() + 1,
                10,
                60L,
                TimeUnit.SECONDS,
                ArrayBlockingQueue(50),
                DefaultThreadFactory("LubanWorker"),
                ThreadPoolExecutor.DiscardPolicy()
            ).also { WORKER_EXECUTOR = it }
        }
    }
}