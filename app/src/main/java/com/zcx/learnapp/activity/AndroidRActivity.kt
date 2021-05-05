package com.zcx.learnapp.activity

import android.Manifest
import android.content.pm.PackageManager
import android.os.Environment
import android.view.View
import androidx.core.app.ActivityCompat
import com.zcx.learnapp.R
import com.zcx.learnapp.base.BaseActivity
import com.zcx.lib_annotations.Subject
import java.io.File

/**
 * @Description:
 * @Author: zcx
 * @CreateDate: 2021/2/22
 */
@Subject("Android 11")
class AndroidRActivity : BaseActivity() {
    override fun getLayoutId() = R.layout.activity_android_r

    fun accessMediaFileWithAbsolutePath(view: View) {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            doAccessFile()
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                1
            )
        }
    }

    private fun doAccessFile() {
        val directory =
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM)
        val file = File(directory, "1.jpg")
        if (file.canRead()) {
            "文件大小：${file.length()}B".toast()
        } else if (!file.exists()) {
            "文件不存在".toast()
        } else {
            "文件不可读".toast()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doAccessFile()
            } else {
                "已拒绝存储权限".toast()
            }
        }
    }
}