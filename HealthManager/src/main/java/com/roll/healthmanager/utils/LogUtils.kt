package com.roll.healthmanager.utils

import android.annotation.SuppressLint
import android.util.Log
import com.roll.healthmanager.HealthManagerHelper
import java.text.SimpleDateFormat
import java.util.*

/**
 * @date: on 2020-06-18
 * @author: a112233
 * @email: mxnzp_life@163.com
 * @desc: 添加描述
 */
object LogUtils {
    /**
     * 本地保存日志信息
     */
    private var logList: MutableList<String>? = null

    @SuppressLint("SimpleDateFormat")
    private val format = SimpleDateFormat("yyyy-MM-dd HH:mm:sss")

    /**
     * 打印日志
     *
     * @param msg
     */
    internal fun log(msg: String) {
        if (HealthManagerHelper.isDebug()) {
            val msg = format.format(Date()) + "：" + msg
            if (logList == null) {
                logList = mutableListOf()
            }
            logList?.add(msg)
            Log.e("HealthManagerSDK", msg)
        }
    }

    /**
     * 获取log日志
     */
    internal fun getLog(): MutableList<String>? {
        return logList
    }
}