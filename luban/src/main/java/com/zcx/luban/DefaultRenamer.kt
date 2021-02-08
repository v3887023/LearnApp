package com.zcx.luban

/**
 * 默认的重命名类
 *
 * @Author: zcx
 * @CreateDate: 2021/1/20
 */
class DefaultRenamer : Renamer {
    /**
     * 使用当前的时间戳进行重命名，忽略原本的名字
     */
    override fun renameFrom(origin: String): String {
        return "${System.nanoTime()}"
    }
}