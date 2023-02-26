package com.zcx.luban.request

import com.zcx.luban.CompressCallback
import com.zcx.luban.result.SingleCompressResult
import com.zcx.luban.source.Source
import com.zcx.luban.worker.SingleCompressWorker

/**
 * 构建单个压缩请求的类
 *
 * @Author: zcx
 * @CreateDate: 2021/1/21
 *
 * @param source 待压缩的图片来源
 */
class SingleRequestBuilder(val source: Source) : BaseRequestBuilder<SingleRequestBuilder>() {

    /**
     * 同步压缩
     *
     * @return 返回单个压缩结果
     */
    fun execute(): SingleCompressResult {
        return SingleCompressWorker(this).execute()
    }

    /**
     * 异步压缩
     * @param callback 压缩回调，会带上单个压缩结果
     */
    fun enqueue(callback: CompressCallback<SingleCompressResult>) {
        SingleCompressWorker(this).enqueue(callback)
    }
}