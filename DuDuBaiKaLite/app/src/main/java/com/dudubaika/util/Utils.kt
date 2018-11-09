package com.dudubaika.util

import android.animation.AnimatorListenerAdapter
import android.app.ActivityManager
import android.content.Context
import android.net.ConnectivityManager
import android.os.Environment
import android.text.TextUtils
import com.dudubaika.base.App
import com.meituan.android.walle.WalleChannelReader
import org.jetbrains.anko.doAsync
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter

object Utils {

    /**
     * 判断程序是否重复启动，在程序中是否有该线程，如果有说明重复启动
     */
    fun isApplicationRepeat(applicationContext: Context): Boolean {

        val pid = android.os.Process.myPid()
        var processName: String? = null
        val am = applicationContext.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager
        val l = am.runningAppProcesses
        val i = l.iterator()
        while (i.hasNext()) {
            val info = i.next()
            try {
                if (info.pid == pid) {
                    processName = info.processName
                }
            } catch (e: Exception) {
            }
        }
        return processName == null || !processName.equals(applicationContext.packageName, ignoreCase = true)
    }

    /**
     * 判断网络是否好用
     */
    fun isNetworkConnected(): Boolean {
        val connectivityManager = App.instance.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        return connectivityManager.getActiveNetworkInfo() != null
    }

    /**
     * 得到缓存总目录
     */
    fun getDataPath(): String {
        return App.instance.cacheDir.absolutePath + File.separator + "data"
    }

    /**
     * 得到网络缓存目录
     */
    fun getCachePath(): String {
        return getDataPath() + File.separator + "netCache"
    }

    /**
     * 得到crash log目录
     */
    fun getCrashLogPath(): String {
        val path = App.instance.getExternalFilesDir(null)!!.absolutePath + File.separator + "crashLog"
        val file = File(path)
        file.mkdirs()
        return file.absolutePath
    }

    /**
     * 得到http log目录
     */
    fun getHttpLogPath(): String {
        val path = App.instance.getExternalFilesDir(null)!!.absolutePath + File.separator + "httpLog"
        val file = File(path)
        file.mkdirs()
        return file.absolutePath
    }

    /**
     * 写入log到文本文件中
     */
    fun writerLog(log: String) {

        doAsync {
            try {
                val path = getHttpLogPath()
                val file = File(path, "http.txt")
                if (!file.exists()) {
                    file.createNewFile()
                } else {
                    //文件超过100k就清空重新写
                    val length = file.length()
                    val kb = length / 1024
                    if (kb > 100) {
                        val fileWriter = FileWriter(file)
                        fileWriter.write("")
                        fileWriter.flush()
                        fileWriter.close()
                    }
                }
                val out = FileWriter(file, true)
                val bw = BufferedWriter(out)
                bw.newLine()
                bw.write(log)
                bw.flush()
                bw.close()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

    }

    /**
     * 获取当前应用的版本号
     */
    fun getVersion(context: Context): String {
        try {
            val manager = context.packageManager
            val info = manager.getPackageInfo(context.packageName, 0)
            return info.versionName
        } catch (e: Exception) {
            e.printStackTrace()
            return "1.0.0"
        }
    }


    /**
     * 获取渠道号  默认2000 -> server
     * @return
     */
    fun getChannelId(): String {
        var channel_id = WalleChannelReader.getChannel(App.instance)
        if (TextUtils.isEmpty(channel_id)) {
            channel_id = "2000"
        }
        return channel_id!!
    }

    /**
     * 得到SD卡存储根目录
     */
    fun getSDPath(): String = Environment.getExternalStorageDirectory().absolutePath

    /**
     * 得到下载APP包目录
     */
    fun getDownLoadAppPath(): String = getSDPath() + File.separator + "dudubaika"

    /**
     * 给手机号显示
     */
    fun encryptPhoneNum(phoneNum: String): String {
        val array = phoneNum.toCharArray()
        val sb = StringBuilder()
        for (i in array.indices) {
            if (i == 3 || i == 4 || i == 5 || i == 6) {
                sb.append("*")
            } else {
                sb.append(array[i])
            }
        }
        return sb.toString()
    }

}