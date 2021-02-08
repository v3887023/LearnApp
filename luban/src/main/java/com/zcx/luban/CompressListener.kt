package com.zcx.luban

/**
 * 压缩监听
 *
 * @Author: zcx
 * @CreateDate: 2021/1/28
 */
abstract class CompressListener {
    /**
     * 压缩开始
     */
    open fun onCompressStart() {}

    /**
     * 压缩结束
     */
    open fun onCompressEnd() {}
}