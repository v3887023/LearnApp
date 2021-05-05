package com.zcx.learnapp.activity

import android.Manifest
import android.content.ContentUris
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.core.app.ActivityCompat
import com.bumptech.glide.Glide
import com.zcx.learnapp.R
import com.zcx.learnapp.adapter.ImageAdapter
import com.zcx.learnapp.adapter.ImageBo
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject
import com.imaginer.utils.SoEngine
import com.zcx.luban.CompressListener
import com.zcx.luban.Luban
import com.zcx.luban.executor.Executors
import com.zcx.luban.source.UriSource
import kotlinx.android.synthetic.main.activity_luban.*
import kotlin.concurrent.thread

/**
 * @Description:
 * @Author: zcx

 * @CreateDate: 2021/1/28
 */
@Subject("图片压缩")
class LubanActivity : BaseActivity() {
    private var lastCount = 0
    private var lastUriInfoList = mutableListOf<UriInfo>()
    private val list = mutableListOf<ImageBo>()
    private val imageAdapter = ImageAdapter(list)
    override fun getLayoutId() = R.layout.activity_luban

    private val handler = Handler(Looper.getMainLooper())
    private var lubanSuccessCount = 0
    private var soSuccessCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStateTv(STATE_INITIAL, "准备就绪")
        seekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                countTv.text = "$progress"
            }

            override fun onStartTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }
        })
        seekBar.progress = 10
        sizeRv.adapter = imageAdapter

        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE),
            0
        )

        showResolutionCb.setOnCheckedChangeListener { _, show ->
            imageAdapter.showResolution(show)
        }

        imageAdapter.setOnItemClickListener { _, _, position ->
            if (lubanBtn.isEnabled && soBtn.isEnabled) {
                val intent = Intent(this, ImageActivity::class.java).apply {
                    putExtra("source", lastUriInfoList[position].uri.toString())
                    putExtra("luban", list[position].lubanFile?.absolutePath.orEmpty())
                    putExtra("so", list[position].soFile?.absolutePath.orEmpty())
                }
                startActivity(intent)
            } else {
                "请等待压缩完成".toast()
            }
        }
    }

    private fun getUriInfos(count: Int): List<UriInfo> {
        val list = mutableListOf<UriInfo>()

        contentResolver.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.SIZE),
            null, null,
            "${MediaStore.MediaColumns.SIZE} DESC"
        )?.use { cursor ->
            var i = 0
            while (cursor.moveToNext() && i++ < count) {
                val id = cursor.getLong(0)
                val size = cursor.getLong(1)
                val uri = ContentUris.withAppendedId(
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                    id
                )

                list.add(UriInfo(uri, size))
            }
        }

        return list
    }

    data class UriInfo(val uri: Uri, val size: Long)

    fun lubanCompress(view: View) {
        setControlButtonEnable(false)
        val count = seekBar.progress
        val uris = mutableListOf<Uri>()
        val uriInfoList: List<UriInfo>

        if (lastCount == count) {
            list.forEach { bo ->
                bo.lubanFile = null
            }
            uriInfoList = lastUriInfoList
            setCostTv(lubanCostTv, 0)
        } else {
            list.clear()
            uriInfoList = getUriInfos(count)
            for (info in uriInfoList) {
                list.add(ImageBo().apply { source = UriSource(contentResolver, info.uri) })
            }
            lastUriInfoList.clear()
            this.lastUriInfoList.addAll(uriInfoList)
            lastCount = count
            setCostTv(lubanCostTv, 0)
            setCostTv(soCostTv, 0)
        }
        imageAdapter.notifyDataSetChanged()

        for (info in uriInfoList) {
            uris.add(info.uri)
        }

        val multipleThread = multipleThreadCb.isChecked

        Luban.with(this)
            .loadUris(uris)
            .fileRenameBy {
                "${it.substring(0, it.lastIndexOf('.'))}/luban_${System.currentTimeMillis()}"
            }
            .compressListener(object : CompressListener() {
                var startMillis: Long = 0
                override fun onCompressStart() {
                    startMillis = System.currentTimeMillis()
                    setStateTv(STATE_RUNNING, "Luban: 引擎压缩中")
                }

                override fun onCompressEnd() {
                    setCostTv(lubanCostTv, System.currentTimeMillis() - startMillis)
                    setControlButtonEnable(true)
                }
            })
            .executeOn(if (multipleThread) Executors.defaultExecutor() else java.util.concurrent.Executors.newSingleThreadExecutor())
            .enqueue { compressResult ->
                if (compressResult.isAllSuccessful) {
                    setStateTv(STATE_SUCCESS, "Luban: 全部压缩成功")
                    runOnUiThread {
                        for ((i, file) in compressResult.outputFileList.withIndex()) {
                            list[i].lubanFile = file
                        }
                        imageAdapter.notifyDataSetChanged()
                    }
                    lubanSuccessCount++
                    if (!isFinishing && recursiveCb.isChecked && lubanSuccessCount < 2048) {
                        Log.d("LubanActivity", "Luban 成功次数: $lubanSuccessCount")
                        handler.postDelayed({ lubanBtn.performClick() }, 500)
                    } else {
                        lubanSuccessCount = 0
                    }
                } else {
                    if (compressResult.outputFileList.isEmpty()) {
                        setStateTv(STATE_ALL_ERROR, "Luban: 全部压缩失败")
                        lubanSuccessCount = 0
                    } else {
                        setStateTv(STATE_SOME_ERROR, "Luban: 部分压缩失败")
                        lubanSuccessCount = 0
                    }
                }
            }
    }

    private fun setStateTv(state: Int, message: String = "") {
        runOnUiThread {
            stateTv.text = message
            stateTv.setTextColor(Color.WHITE)

            when (state) {
                STATE_INITIAL -> {
                    stateTv.setTextColor(Color.parseColor("#212121"))
                    controlLl.setBackgroundColor(Color.parseColor("#e3f2fd"))
                }
                STATE_RUNNING -> controlLl.setBackgroundColor(Color.parseColor("#2196f3"))
                STATE_SUCCESS -> controlLl.setBackgroundColor(Color.parseColor("#4caf50"))
                STATE_SOME_ERROR -> controlLl.setBackgroundColor(Color.parseColor("#ffc107"))
                STATE_ALL_ERROR -> controlLl.setBackgroundColor(Color.parseColor("#f44336"))
            }
        }
    }

    private fun setCostTv(textView: TextView, cost: Long) {
        runOnUiThread { textView.text = if (cost > 0) "${cost}ms" else "-" }
    }

    fun soCompress(view: View) {
        setControlButtonEnable(false)
        val count = seekBar.progress
        val uris = mutableListOf<Uri>()
        val uriInfoList: List<UriInfo>

        if (lastCount == count) {
            list.forEach { bo ->
                bo.soFile = null
            }
            uriInfoList = lastUriInfoList
            setCostTv(soCostTv, 0)
        } else {
            list.clear()
            uriInfoList = getUriInfos(count)
            for (info in uriInfoList) {
                list.add(ImageBo().apply { source = UriSource(contentResolver, info.uri) })
            }
            lastUriInfoList.clear()
            this.lastUriInfoList.addAll(uriInfoList)
            lastCount = count
            setCostTv(lubanCostTv, 0)
            setCostTv(soCostTv, 0)
        }
        imageAdapter.notifyDataSetChanged()

        for (info in uriInfoList) {
            uris.add(info.uri)
        }

        val multipleThread = multipleThreadCb.isChecked

        Luban.with(this)
            .loadUris(uris)
            .compressEngine(SoEngine())
            .fileRenameBy {
                "${it.substring(0, it.lastIndexOf('.'))}/so_${System.currentTimeMillis()}"
            }
            .compressListener(object : CompressListener() {
                var startMillis: Long = 0
                override fun onCompressStart() {
                    startMillis = System.currentTimeMillis()
                    setStateTv(STATE_RUNNING, "So: 引擎压缩中")
                }

                override fun onCompressEnd() {
                    setCostTv(soCostTv, System.currentTimeMillis() - startMillis)
                    setControlButtonEnable(true)
                }
            })
            .executeOn(if (multipleThread) Executors.defaultExecutor() else java.util.concurrent.Executors.newSingleThreadExecutor())
            .enqueue { compressResult ->
                if (compressResult.isAllSuccessful) {
                    setStateTv(STATE_SUCCESS, "So: 全部压缩成功")
                    runOnUiThread {
                        for ((i, file) in compressResult.outputFileList.withIndex()) {
                            list[i].soFile = file
                        }
                        imageAdapter.notifyDataSetChanged()
                    }
                    soSuccessCount++
                    if (!isFinishing && recursiveCb.isChecked && soSuccessCount < 2048) {
                        Log.d("LubanActivity", "So 成功次数: $soSuccessCount")
                        handler.postDelayed({ soBtn.performClick() }, 500)
                    } else {
                        soSuccessCount = 0
                    }
                } else {
                    if (compressResult.outputFileList.isEmpty()) {
                        setStateTv(STATE_ALL_ERROR, "So: 全部压缩失败")
                        soSuccessCount = 0
                    } else {
                        setStateTv(STATE_SOME_ERROR, "So: 部分压缩失败")
                        soSuccessCount = 0
                    }
                }
            }
    }

    private fun setControlButtonEnable(enable: Boolean) {
        runOnUiThread {
            runOnUiThread {
                lubanBtn.isEnabled = enable
                soBtn.isEnabled = enable
            }
        }
    }

    override fun onDestroy() {
        handler.removeCallbacksAndMessages(null)
        super.onDestroy()
    }

    companion object {
        const val STATE_INITIAL = 0
        const val STATE_RUNNING = 1
        const val STATE_SUCCESS = 2
        const val STATE_SOME_ERROR = 3
        const val STATE_ALL_ERROR = 4
    }

    fun clearCache(view: View) {
        "开始清除缓存...".toast()
        Glide.get(this).clearMemory()

        thread {
            Luban.with(this).clearCache()
            Glide.get(this).clearDiskCache()
            "缓存已清除".toast()
        }
    }
}