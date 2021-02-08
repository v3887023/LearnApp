package com.imaginer.utils;

import android.graphics.Bitmap;

public class CompressUtils {

    static {
        System.loadLibrary("compress-lib");
    }

    /**
     * LibJpeg压缩
     *
     * @param bitmap           位图
     * @param quality          压缩质量 100无损压缩(容易出现比原来还大的情况...不推荐) 0完全压缩
     * @param fileNameBytes    输出文件目录
     * @param optimize         是否开启哈夫曼算法
     * @return
     */
    public static native int compressBitmap(Bitmap bitmap, int quality, byte[] fileNameBytes,
                                            boolean optimize);
}
