package com.zcx.luban;

/**
 * 压缩结果回调
 *
 * @Author: zcx
 * @CreateDate: 2021/2/3
 */
public interface CompressCallback<R> {
    /**
     * 压缩结束时，会回调这个方法，返回压缩结果
     *
     * @param compressResult 压缩结果
     */
    void onFinish(R compressResult);
}
