package com.zcx.luban;

/**
 * 重命名的接口
 *
 * @Author: zcx
 * @CreateDate: 2021/1/28
 */
public interface Renamer {
    /**
     * 重命名方法，给定原来的名字 origin，进行重命名操作，并返回新的名字
     *
     * @param origin 原名
     * @return 新的名字
     */
    String renameFrom(String origin);
}
