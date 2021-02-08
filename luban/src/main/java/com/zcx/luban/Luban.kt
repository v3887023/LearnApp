package com.zcx.luban

import android.content.Context
import android.net.Uri
import android.util.DisplayMetrics
import android.view.WindowManager
import com.zcx.luban.request.MultipleRequestBuilder
import com.zcx.luban.request.SingleRequestBuilder
import com.zcx.luban.source.FileSource
import com.zcx.luban.source.Source
import com.zcx.luban.source.UriSource
import java.io.File

/**
 * 鲁班图片压缩工具
 *
 * @Author: zcx
 * @CreateDate: 2021/1/20
 */
class Luban private constructor(private val context: Context) {
    private var quality: Int = calculateQuality()
    private val defaultOutputDirectory = getDefaultOutputDirectory()

    /**
     * 压缩单个文件，图片来源为 [File] 类型
     */
    fun loadFile(file: File): SingleRequestBuilder {
        return SingleRequestBuilder(FileSource(file)).quality(quality)
            .outputAt(defaultOutputDirectory)
    }

    /**
     * 压缩单个文件，图片来源为 [Uri] 类型
     */
    fun loadUri(uri: Uri): SingleRequestBuilder {
        return SingleRequestBuilder(UriSource(context.contentResolver, uri)).quality(quality)
            .outputAt(defaultOutputDirectory)
    }

    /**
     * 批量压缩文件，图片来源为 [File] 类型
     */
    fun loadFiles(files: List<File>): MultipleRequestBuilder {
        val sources = mutableListOf<Source>()
        for (file in files) {
            sources.add(FileSource(file))
        }

        return MultipleRequestBuilder(sources).quality(quality)
            .outputAt(defaultOutputDirectory)
    }

    /**
     * 批量压缩文件，图片来源为 [Uri] 类型
     */
    fun loadUris(uris: List<Uri>): MultipleRequestBuilder {
        val sources = mutableListOf<Source>()
        for (uri in uris) {
            sources.add(UriSource(context.contentResolver, uri))
        }

        return MultipleRequestBuilder(sources).quality(quality)
            .outputAt(defaultOutputDirectory)
    }

    /**
     * 压缩单个文件
     */
    fun loadSource(source: Source): SingleRequestBuilder {
        return SingleRequestBuilder(source).quality(quality).outputAt(defaultOutputDirectory)
    }

    /**
     * 批量压缩文件
     */
    fun loadSources(sources: List<Source>): MultipleRequestBuilder {
        return MultipleRequestBuilder(sources).quality(quality).outputAt(defaultOutputDirectory)
    }

    fun clearCache() {
        getDefaultOutputDirectory().deleteRecursively()
    }

    companion object {

        // 常用压缩比
        private const val QUALITY_LOW: Int = 60
        private const val QUALITY_DEFAULT: Int = 66
        private const val QUALITY_HIGH: Int = 82
        private const val QUALITY_VERY_HIGH: Int = 88
        private const val QUALITY_SUPER_HIGH: Int = 94

        /**
         * 默认缓存目录名
         */
        private const val DIRECTORY_CACHE = "luban_cache"

        /**
         * 鲁班压缩工具入口
         */
        @JvmStatic
        fun with(context: Context): Luban {
            return Luban(context)
        }
    }

    /**
     * 获取默认的输出路径
     */
    private fun getDefaultOutputDirectory(): File {
        val cacheDir = context.externalCacheDir ?: context.cacheDir
        return File(cacheDir, DIRECTORY_CACHE)
    }

    private fun calculateQuality(): Int {
        val dm = DisplayMetrics()
        (context.getSystemService(Context.WINDOW_SERVICE) as WindowManager).defaultDisplay.getMetrics(
            dm
        )

        val density = dm.density

        return if (density > 3f) {
            QUALITY_LOW
        } else if (density > 2.5f && density <= 3f) {
            QUALITY_DEFAULT
        } else if (density > 2f && density <= 2.5f) {
            QUALITY_HIGH
        } else if (density > 1.5f && density <= 2f) {
            QUALITY_VERY_HIGH
        } else {
            QUALITY_SUPER_HIGH
        }
    }
}