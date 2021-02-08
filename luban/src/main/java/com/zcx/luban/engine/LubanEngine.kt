package com.zcx.luban.engine

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import androidx.exifinterface.media.ExifInterface
import com.zcx.luban.CompressOptions
import com.zcx.luban.source.Source
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import kotlin.math.ceil
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow

/**
 * 鲁班压缩引擎
 *
 * @Author: zcx
 * @CreateDate: 2021/1/20
 */
class LubanEngine : CompressEngine {

    @Throws(Exception::class)
    override fun compress(source: Source, compressOptions: CompressOptions, destFile: File): File {
        var fileLength = source.getLength()
        val maxSize = compressOptions.maxSize

        if (maxSize >= fileLength) {
            // 文件大小小于给定的最大值，改变压缩参数，保持采样率和质量，转格式
            compressOptions.apply {
                useInSample = false
                quality = 100
            }
        }

        fileLength = maxSize

        // 解析Bitmap
        val options = BitmapFactory.Options()
        // 不加载进内存
        options.inJustDecodeBounds = true
        // 默认采样率 采样率是2的次幂
        var inSampleSize = 1
        options.inSampleSize = inSampleSize
        // 不加载进内存解析一次 获取宽高
        source.decodeAsBitmap(options)
        // 解析出宽高
        val width = options.outWidth
        val height = options.outHeight

        // 计算采样率
        val useInSample = compressOptions.useInSample
        if (useInSample) {
            inSampleSize = computeInSampleSize(width, height)
            options.inSampleSize = inSampleSize
        }

        val compressConfig = compressOptions.bitmapConfig
        // 指定图片 ARGB 或者 RGB
        options.inPreferredConfig = compressConfig

        // 内存不足情况
        val isAlpha = compressConfig == Bitmap.Config.ARGB_8888
        val compressFormat = compressOptions.compressFormat
        if (!hasEnoughMemory(width / inSampleSize, height / inSampleSize, isAlpha)) {
            // 内存不足使用
            options.inPreferredConfig =
                if (compressFormat == Bitmap.CompressFormat.PNG) Bitmap.Config.ARGB_4444 else Bitmap.Config.RGB_565

            // 减低像素，减低内存
            if (!hasEnoughMemory(width / inSampleSize, height / inSampleSize, false)) {
                // 并且重新计算采样率
                inSampleSize = computeInSampleSize(width, height)
                options.inSampleSize = inSampleSize

                if (!hasEnoughMemory(width / inSampleSize, height / inSampleSize, false)) {
                    throw RuntimeException("image length is too large")
                }
            }
        }

        // 加载入内存中
        options.inJustDecodeBounds = false
        // 此处OOM
        var bitmap: Bitmap = source.decodeAsBitmap(options)
            ?: throw IOException("文件不存在或文件不能被解码成一个 Bitmap") // 解析出错
        // 精准缩放压缩
        bitmap = if (useInSample) {
            // 旋转默认角度
            transformBitmap(source, bitmap, 1f)
        } else {
            val scale: Float = calculateScaleSize(width, height)
            transformBitmap(source, bitmap, scale)
        }

        // 获取解析流
        val stream = ByteArrayOutputStream()

        // 压缩开始
        var quality = compressOptions.quality
        bitmap.compress(compressFormat, quality, stream)
        // 无损不支持压缩
        if (compressFormat != Bitmap.CompressFormat.PNG && fileLength > 0) {
            //耗时由此处触发
            while (stream.size() / 1024 > fileLength && quality > 6) {
                stream.reset()
                quality -= 6
                bitmap.compress(compressFormat, quality, stream)
            }
        }
        // 标记释放
        bitmap.recycle()

        // 输出文件
        val parent = destFile.parentFile?.apply { mkdirs() }
        val tempFile = File(parent, "${destFile.name}.temp")
        val fos = FileOutputStream(tempFile)
        stream.writeTo(fos)
        fos.flush()
        fos.close()
        stream.close()

        tempFile.renameTo(destFile)
        tempFile.delete()

        return destFile
    }

    /**
     * 判断内存是否足够 32 位每个像素占用 4 字节
     */
    private fun hasEnoughMemory(width: Int, height: Int, isAlpha32: Boolean): Boolean {
        val runtime = Runtime.getRuntime()
        val free = runtime.maxMemory() - runtime.totalMemory() + runtime.freeMemory()
        val allocation = width * height shl if (isAlpha32) 2 else 1
        return allocation < free
    }

    /**
     * 采样率。核心算法
     */
    private fun computeInSampleSize(width: Int, height: Int): Int {
        var w = width
        var h = height

        w = if (w and 1 == 1) w + 1 else w
        h = if (h and 1 == 1) h + 1 else h

        w = if (w > h) h else w
        h = if (w > h) w else h

        val scale = w.toDouble() / h

        return when {
            0.5625 < scale && scale <= 1 -> {
                when {
                    h < 1664 -> 1
                    h in 1664..4989 -> 2
                    h in 4990..10239 -> 4
                    else -> h / 1280
                }
            }
            0.5 < scale && scale <= 0.5625 -> {
                if (h <= 1280) 1 else h / 1280
            }
            else -> {
                ceil(h / (1280.0 / scale)).toInt()
            }
        }
    }

    /**
     * 精确缩放
     */
    private fun calculateScaleSize(width: Int, height: Int): Float {
        var scale = 1f
        val max = max(width, height)
        val min = min(width, height)
        val ratio = min / (max.toFloat())
        if (ratio >= 0.5f) {
            if (max > SCALE_REFERENCE_WIDTH) scale = SCALE_REFERENCE_WIDTH / (max.toFloat())
        } else {
            val multiple = max / min
            if (multiple < 10) {
                if (min > LIMITED_WIDTH && (1f - ratio / 2f) * min > LIMITED_WIDTH) {
                    scale = 1f - ratio / 2f
                }
            } else {
                val arg = multiple.toDouble().pow(2).toInt()
                scale = 1f - arg / LIMITED_WIDTH + if (multiple > 10) 0.01f else 0.03f
                if (min * scale < MIN_WIDTH) {
                    scale = 1f
                }
            }
        }
        return scale
    }

    /**
     * 缩放 旋转bitmap scale =1f不缩放
     */
    private fun transformBitmap(source: Source, bitmap: Bitmap, scale: Float): Bitmap {
        //过滤无用的压缩
        var bmp = bitmap
        val srcExif = source.getExifInterface()
        if (srcExif == null && scale == 1f) return bmp

        val matrix = Matrix()
        if (srcExif != null) {
            val orientation = srcExif.getAttributeInt(
                ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_NORMAL
            )
            val angle: Int = when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> 90
                ExifInterface.ORIENTATION_ROTATE_180 -> 180
                ExifInterface.ORIENTATION_ROTATE_270 -> 270
                else -> 0
            }
            matrix.postRotate(angle.toFloat())
        }

        if (scale != 1f) {
            matrix.setScale(scale, scale)
        }

        try {
            val converted = Bitmap.createBitmap(bmp, 0, 0, bmp.width, bmp.height, matrix, true)
            if (!bitmap.sameAs(converted)) {
                bmp = converted
            }
        } catch (error: OutOfMemoryError) {
            System.gc()
            System.runFinalization()
        }

        return bmp
    }

    companion object {
        private const val SCALE_REFERENCE_WIDTH: Float = 1280f
        private const val LIMITED_WIDTH: Float = 1000f
        private const val MIN_WIDTH: Float = 640f
    }
}