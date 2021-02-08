package com.zcx.luban.request

import com.zcx.luban.CompressCallback
import com.zcx.luban.result.MultipleCompressResult
import com.zcx.luban.source.Source
import com.zcx.luban.worker.MultipleCompressWorker

/**
 * 构建批量压缩的类
 *
 * @Author: zcx
 * @CreateDate: 2021/1/21
 *
 * @param sources 待压缩的图片来源列表
 */
class MultipleRequestBuilder(val sources: List<Source>) :
    BaseRequestBuilder<MultipleRequestBuilder>() {

    /**
     * 同步压缩
     *
     * @return 返回批量压缩结果
     */
    fun execute(): MultipleCompressResult {
        return MultipleCompressWorker(this).execute()
    }

    /**
     * 异步压缩
     * @param callback 压缩回调，会带上批量压缩结果
     */
    fun enqueue(callback: CompressCallback<MultipleCompressResult>) {
        MultipleCompressWorker(this).enqueue(callback)
    }
}