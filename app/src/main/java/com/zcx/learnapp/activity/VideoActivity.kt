package com.zcx.learnapp.activity

import android.content.ContentUris
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.provider.MediaStore
import android.util.Size
import android.widget.ImageView
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import kotlin.concurrent.thread

class VideoActivity : BaseActivity() {
    private lateinit var thumbIv: ImageView
    override fun getLayoutId() = R.layout.activity_video

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        thumbIv = findViewById(R.id.thumbIv)

        thread {
            getVideoThumbnail()
        }
    }

    private fun getVideoThumbnail() {
        val cursor = contentResolver.query(
            MediaStore.Video.Media.EXTERNAL_CONTENT_URI,
            arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.DATA),
            null,
            null,
            null
        )

        cursor?.use {
            if (it.moveToNext()) {
                val id = it.getLong(0)
                val path = it.getString(1)
                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    val uri =
                        ContentUris.withAppendedId(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, id)
                    try {
                        contentResolver.loadThumbnail(uri, Size(400, 400), CancellationSignal())
                    } catch (e: Exception) {
                        e.printStackTrace()
                        null
                    }

                } else {
                    BitmapFactory.decodeFile(path)
                }

                if (bitmap != null) {
                    runOnUiThread { thumbIv.setImageBitmap(bitmap) }
                }
            }
        }
    }

    data class Video(val id: Long, val path: String, val uri: Uri)
}