package com.zcx.luban.executor

import java.util.concurrent.ThreadFactory
import java.util.concurrent.atomic.AtomicInteger

/**
 * 默认的线程工厂类
 */
class DefaultThreadFactory @JvmOverloads constructor(name: String = "") : ThreadFactory {

    companion object {
        private val poolNumber = AtomicInteger(1)
    }

    private val group: ThreadGroup?
    private val threadNumber = AtomicInteger(1)
    private val namePrefix: String

    init {
        val s = System.getSecurityManager()
        group = if (s != null) s.threadGroup else Thread.currentThread().threadGroup
        namePrefix = "$name-${poolNumber.getAndIncrement()}-thread-"
    }

    override fun newThread(r: Runnable): Thread {
        val t = Thread(group, r, namePrefix + threadNumber.getAndIncrement())

        if (t.isDaemon) {
            t.isDaemon = false
        }

        if (t.priority != Thread.NORM_PRIORITY) {
            t.priority = Thread.NORM_PRIORITY
        }

        return t
    }
}