package com.example.learnapp.activity

import android.content.ContentValues
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.core.net.toFile
import com.example.learnapp.R
import com.example.learnapp.base.BaseActivity
import com.example.lib_annotations.Subject
import kotlinx.android.synthetic.main.activity_android_q.*
import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.net.URL
import kotlin.concurrent.thread

@Subject(title = "Android 10")
class AndroidQActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_android_q

    fun startActivity(view: View) {
        Handler(Looper.getMainLooper()).postDelayed({
            startActivity(Intent(this@AndroidQActivity, BlankActivity::class.java))
            Log.d("AndroidQActivity", "打开页面")
        }, 3000)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val externalFilesDir = getExternalFilesDir("")
        val externalStorageDirectory = Environment.getExternalStorageDirectory()
        externalStorageDirectory.exists()
    }

    fun writeFile(view: View) {
        val dir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)
        val file = File(dir, "test.txt")
        file.outputStream().bufferedWriter().use {
            it.write("Hello world!")
        }
    }

    fun readFile(view: View) {

    }

    fun invokeIllegalApi(view: View) {
        val toast = Toast.makeText(this, "Not OK", Toast.LENGTH_SHORT)

        val declaredField = toast::class.java.getDeclaredField("mTN")

        toast.show()
    }

    fun safeInvokeIllegalApi(view: View) {
        val toast = Toast.makeText(this, "OK", Toast.LENGTH_SHORT)

        try {
            val declaredField = toast::class.java.getDeclaredField("mTN")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        toast.show()
    }

    fun writeInternalStorage(view: View) {
        val file = File(filesDir, FILE_NAME)
        toast(if (file.exists()) "yes" else "no")

        if (file.exists()) {
            file.delete()
        }

        file.outputStream().use { fos ->
            fos.writer().use {
                it.write("test")
                it.flush()
            }
            fos.flush()
        }
    }

    private fun toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    fun readInternalStorage(view: View) {
        val file = File(filesDir, FILE_NAME)
        toast(if (file.exists()) "yes" else "no")

        if (file.exists()) {
            file.inputStream().use { fis ->
                fis.reader().use {
                    toast(it.readText())
                }
            }
        }
    }

    fun writeExternalStorage(view: View) {
        val file = File(getExternalFilesDir(""), FILE_NAME)
        toast(if (file.exists()) "yes" else "no")

        if (file.exists()) {
            file.delete()
        }

        file.outputStream().use { fos ->
            fos.writer().use {
                it.write("test")
                it.flush()
            }
            fos.flush()
        }
    }

    fun readExternalStorage(view: View) {
        val file = File(getExternalFilesDir(""), FILE_NAME)
        toast(if (file.exists()) "yes" else "no")

        if (file.exists()) {
            file.inputStream().use { fis ->
                fis.reader().use {
                    toast(it.readText())
                }
            }
        }
    }

    companion object {
        const val FILE_NAME = "test.txt"
        const val BITMAP_NAME = "test.png"
    }

    fun writeBitmapToDCIM(view: View) {
        val v = if (count++ and 1 == 0) view.rootView else view
        val bitmap =
            Bitmap.createBitmap(v.width, v.height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        v.draw(canvas)

        this.uri = saveBitmapToDCIM(bitmap, "image", BITMAP_NAME, 100)
    }

    private var count = 0

    private fun saveBitmapToDCIM(
        bitmap: Bitmap,
        path: String?,
        filename: String,
        quality: Int
    ): Uri? {
        var relativePath = Environment.DIRECTORY_DCIM
        path?.let { relativePath += "/$it" }

        var compressFormat = Bitmap.CompressFormat.PNG
        var mimeType = "image/png"
        if (filename.endsWith("jpg", true)) {
            compressFormat = Bitmap.CompressFormat.JPEG
            mimeType = "image/jpg"
        }

        var uri: Uri?
        if (atLeastAndroidQ()) {
            val values = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
            }

            uri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        } else {
            val directory = File(Environment.getExternalStorageDirectory(), relativePath).also {
                if (!it.exists()) {
                    it.mkdirs()
                }
            }

            val file = File(directory, filename)
            uri = Uri.fromFile(file)
        }

        if (uri != null) {
            try {
                contentResolver.openOutputStream(uri)?.use { os ->
                    bitmap.compress(compressFormat, quality, os)
                }
            } catch (e: IOException) {
                e.printStackTrace()
                uri = null
            }
        }

        return uri
    }

    private var uri: Uri? = null

    private fun atLeastAndroidQ() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q

    fun readBitmapFromDCIM(view: View) {
        val uri = this.uri
        if (uri != null) {
            val bitmap = BitmapFactory.decodeStream(contentResolver.openInputStream(uri))
            imageIv.setImageBitmap(bitmap)
        }

//        val cursor = contentResolver.query(
//            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//            arrayOf(MediaStore.MediaColumns._ID, MediaStore.MediaColumns.RELATIVE_PATH),
//            "${MediaStore.MediaColumns.DISPLAY_NAME}=?",
//            arrayOf(BITMAP_NAME),
//            null
//        )
//
//        cursor?.use { it ->
//            while (it.moveToNext()) {
//                val id = it.getLong(it.getColumnIndexOrThrow(MediaStore.MediaColumns._ID))
//                val relativePath =
//                    it.getString(it.getColumnIndexOrThrow(MediaStore.MediaColumns.RELATIVE_PATH))
//                val uri =
//                    ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
//                val bitmap = BitmapFactory.decodeFileDescriptor(
//                    contentResolver.openFileDescriptor(uri, "r")?.fileDescriptor
//                )
//
//                contentResolver.delete(
//                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                    "${MediaStore.MediaColumns._ID}=?",
//                    arrayOf()
//                )
//
//                imageIv.setImageBitmap(bitmap)
//            }
//        }
    }

    fun downloadFile(view: View) {
        download(testUrl, Environment.DIRECTORY_DOWNLOADS)
    }

    private val testUrl =
        "https://stream7.iqilu.com/10339/upload_transcode/202002/18/20200218114723HDu3hhxqIT.mp4"

    private fun download(urlString: String, relativePath: String) {
        thread {
            val filename = "test.mp4"
            val tempFileName = "$filename.tmp"
            val contentResolver = this.contentResolver
            val connection = URL(urlString).openConnection()
            val contentLength = connection.contentLength
            connection.getInputStream().buffered().use { bis ->
                val contentValues = ContentValues().apply {
                    put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
                    put(MediaStore.MediaColumns.DISPLAY_NAME, tempFileName)
                }

//                val uri = contentResolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
                val uri = Uri.fromFile(File(getExternalFilesDir(relativePath), filename))

                if (uri != null) {
                    contentResolver.openOutputStream(uri)?.buffered()?.use { bos ->
                        val buff = ByteArray(2048)
                        var len: Int
                        var downloaded = 0
                        while (bis.read(buff).also { len = it } != -1) {
                            bos.write(buff, 0, len)
                            downloaded += len

                            val progress = downloaded * 100.0 / contentLength
                            publishProgress(progress.toInt())
                        }
                        bos.flush()

//                        contentValues.put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
//                        contentResolver.update(uri, contentValues, null, null)
                    }

                    val file = uri.toFile()
                    saveVideoFileToDCIM(file, "app", 0)
                }

            }
        }
    }

    private fun publishProgress(progress: Int) {
        runOnUiThread {
            progressTv.text = "$progress%"
        }
    }

    fun downloadFileToOtherDirectory(view: View) {
        download(testUrl, "a_test_dir")
    }

    private fun saveVideoFileToDCIM(file: File, path: String?, videoDuration: Long): Uri {
        if (!file.exists()) {
            return Uri.EMPTY
        }

        var relativePath = Environment.DIRECTORY_DCIM
        path?.let { relativePath += "/$it" }

        val contentResolver = this.contentResolver
        val mimeType = "video/mp4"

        val values = ContentValues().apply {
            val currentTimeInSeconds = System.currentTimeMillis()
            put(MediaStore.MediaColumns.TITLE, file.name)
            put(MediaStore.MediaColumns.DISPLAY_NAME, file.name)
            put(MediaStore.MediaColumns.DATE_MODIFIED, currentTimeInSeconds)
            put(MediaStore.MediaColumns.DATE_ADDED, currentTimeInSeconds)
            put(MediaStore.MediaColumns.SIZE, file.length())
            put(MediaStore.Video.VideoColumns.DATE_TAKEN, System.currentTimeMillis())
            put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
            put(MediaStore.Video.VideoColumns.DURATION, videoDuration) //时长
        }

        if (atLeastAndroidQ()) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativePath)
        } else {
            val dir = File(
                Environment.getExternalStorageDirectory(), relativePath
            ).also { it.mkdirs() }

            val newFile = File(dir, file.name)
            values.put(MediaStore.MediaColumns.DATA, newFile.absolutePath)
        }

        contentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, values)?.also { uri ->
            try {
                contentResolver.openOutputStream(uri)?.use { outputStream ->
                    contentResolver.openInputStream(Uri.fromFile(file))?.use { inputStream ->
                        val buff = ByteArray(2048)
                        var len: Int
                        val length = file.length()
                        var written = 0
                        while (inputStream.read(buff).also { len = it } != -1) {
                            Thread.sleep(2)
                            outputStream.write(buff, 0, len)
                            written += len

                            val progress = written * 100.0 / length
                            publishProgress(progress.toInt())
                        }
                        outputStream.flush()
                        return uri
                    }
                }
            } catch (e: FileNotFoundException) {
                e.printStackTrace()
            }
        }

        return Uri.EMPTY
    }
}