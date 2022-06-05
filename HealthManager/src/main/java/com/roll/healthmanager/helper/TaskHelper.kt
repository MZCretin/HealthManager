package com.roll.healthmanager.helper

import android.app.Application
import android.os.Handler
import android.os.Looper
import com.roll.healthmanager.HealthManagerHelper
import com.roll.healthmanager.ui.HealthManagerCoverActivity
import com.roll.healthmanager.ui.HealthManagerDialogActivity
import com.roll.healthmanager.utils.LogUtils
import java.util.*

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: TaskHelper
 * Author: cretin
 * Date: 2021/8/18 10:22 上午
 * Description: 任务帮助类
 */
object TaskHelper {

    private var SHOW_COVER = 1000
    private var SHOW_DIALOG = 2000

    //记录使用app的时间 单位s
    private var currentUseAppTime = 0L

    //进入后台的时间点戳
    private var inBackgroundTimestamp = 0L

    //开始使用app的时间戳
    private var startUseAppTimestamp = 0L

    //记录当前的状态 0 都没有显示 1 显示了浮层 2 显示了弹窗
    private var status = 0

    //记录是否显示过对话框
    internal var hasShowDialog = false

    //专门处理延时任务
    private var handler: Handler? = null

    internal fun init(application: Application) {
        handler = Handler(Looper.getMainLooper()) {
            if (it.what == SHOW_COVER) {
                //显示浮层 只有在当前没有显示内容的时候才会显示
                if (status == 0) {
                    LogUtils.log("任务时间到，显示浮层")
                    status = 1
                    val topActivity = HealthManagerHelper.lifecycleHandler?.topActivity
                    if (topActivity != null) {
                        HealthManagerCoverActivity.start(topActivity)
                    } else {
                        HealthManagerCoverActivity.start(application)
                    }
                } else {
                    //代表使用的时间已经达标了 下次需要直接显示浮窗
                    currentUseAppTime = HealthManagerHelper.getStudyTimeLimit().toLong()
                }
            }
            if (it.what == SHOW_DIALOG) {
                //显示对话框 只有在当前没有显示内容的时候才会显示
                if (status == 0) {
                    hasShowDialog = true
                    LogUtils.log("任务时间到，显示对话框")
                    status = 2
                    val topActivity = HealthManagerHelper.lifecycleHandler?.topActivity
                    if (topActivity != null) {
                        HealthManagerDialogActivity.start(topActivity)
                    } else {
                        HealthManagerDialogActivity.start(application)
                    }
                }
            }
            true
        }
    }

    /**
     * 开启延时任务
     */
    internal fun startDelayTask() {
        startCoverTask()
        startDialogTask()
    }

    /**
     * 结束任务
     */
    internal fun finishTask() {
        status = 0
    }

    /**
     * 启动对话框的任务
     */
    private fun startDialogTask() {
        handler?.removeMessages(SHOW_DIALOG)

        //判断时区 如果不是大陆时区 则不做限制
        if (TimeZone.getDefault() == TimeZone.getTimeZone("Asia/Shanghai") ||
            TimeZone.getDefault() == TimeZone.getTimeZone("GMT+08")
        ) {
            //只有是大陆时区才需要处理
            LogUtils.log("当前是大陆时区")
        } else {
            LogUtils.log("当前不是大陆时区，不走弹窗流程")
            return
        }

        if (hasShowDialog) {
            LogUtils.log("自启动后已经显示过弹窗")
            return
        }

        val info = HealthManagerHelper.getNightModeInfo()
        //是否跨天
        val isOverDay = info[6] == 1
        //构建时间戳
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH) + 1
        val day = calendar.get(Calendar.DAY_OF_MONTH)
//        val hour = calendar.get(Calendar.HOUR_OF_DAY)
//        val minute = calendar.get(Calendar.MINUTE)
//        val second = calendar.get(Calendar.SECOND)

        val startTime = Calendar.getInstance()
        val endTime = Calendar.getInstance()
        if (isOverDay) {
            //如果是跨天 比如21：00到次日6：00  startTime 记录的是6:00 endTime 记录的是21:00
            startTime.set(Calendar.YEAR, year)
            startTime.set(Calendar.MONTH, month - 1)
            startTime.set(Calendar.DAY_OF_MONTH, day)
            startTime.set(Calendar.HOUR_OF_DAY, info[3])
            startTime.set(Calendar.MINUTE, info[4])
            startTime.set(Calendar.SECOND, info[5])

            endTime.set(Calendar.YEAR, year)
            endTime.set(Calendar.MONTH, month - 1)
            endTime.set(Calendar.DAY_OF_MONTH, day)
            endTime.set(Calendar.HOUR_OF_DAY, info[0])
            endTime.set(Calendar.MINUTE, info[1])
            endTime.set(Calendar.SECOND, info[2])
        } else {
            //如果不是跨天 比如18：00到21：00 startTime记录的是18:00 endTime 记录的是21：00
            startTime.set(Calendar.YEAR, year)
            startTime.set(Calendar.MONTH, month - 1)
            startTime.set(Calendar.DAY_OF_MONTH, day)
            startTime.set(Calendar.HOUR_OF_DAY, info[0])
            startTime.set(Calendar.MINUTE, info[1])
            startTime.set(Calendar.SECOND, info[2])

            endTime.set(Calendar.YEAR, year)
            endTime.set(Calendar.MONTH, month - 1)
            endTime.set(Calendar.DAY_OF_MONTH, day)
            endTime.set(Calendar.HOUR_OF_DAY, info[3])
            endTime.set(Calendar.MINUTE, info[4])
            endTime.set(Calendar.SECOND, info[5])
        }

        fun runDelayTask() {
            //不需要立即显示 需要计算时间去显示
            //发起每天定时的弹窗的任务
            //如果是跨天 比如21：00到次日6：00  startTime 记录的是6:00 endTime 记录的是21:00
            //如果不是跨天 比如18：00到21：00 startTime记录的是18:00 endTime 记录的是21：00
            val dialogTimeDelay = if (isOverDay) {
                //如果是跨天的 只需要计算离startTime的时间长度
                endTime.time.time - calendar.time.time
            } else {
                //不是跨天的 那么需要看情况处理
                if (calendar.before(startTime)) {
                    startTime.time.time - calendar.time.time
                } else {
                    startTime.time.time - calendar.time.time + 24 * 3600 * 1000L
                }
            }
            LogUtils.log("当前不在夜深场景，需要等待${dialogTimeDelay / 1000}s后才会触发弹窗")
            handler?.sendEmptyMessageDelayed(SHOW_DIALOG, dialogTimeDelay)
        }

        //如果是跨天 比如21：00到次日6：00  startTime 记录的是6:00 endTime 记录的是21:00
        //如果不是跨天 比如18：00到21：00 startTime记录的是18:00 endTime 记录的是21：00
        if (isOverDay) {
            if (calendar.before(startTime) || calendar.after(endTime)) {
                LogUtils.log("当前在夜深场景显示弹窗 1")
                handler?.sendEmptyMessage(SHOW_DIALOG)
            } else {
                runDelayTask()
            }
        } else {
            if (calendar.after(startTime) && calendar.before(endTime)) {
                //需要显示对话框
                LogUtils.log("当前在夜深场景显示弹窗 2")
                handler?.sendEmptyMessage(SHOW_DIALOG)
            } else {
                runDelayTask()
            }
        }
    }

    /**
     * 启动浮层的任务
     */
    private fun startCoverTask() {
        handler?.removeMessages(SHOW_COVER)
        //查看app是否进入后台超过5分钟
        val backgroundTime = (System.currentTimeMillis() - inBackgroundTimestamp) / 1000L
        if (inBackgroundTimestamp != 0L && backgroundTime > HealthManagerHelper.getMaxInBackgroundTime()) {
            //如果超过时间 则重新开始计时
            currentUseAppTime = 0L
            LogUtils.log("app在后台停留时间超过${HealthManagerHelper.getMaxInBackgroundTime()}s，时间被重置")
        } else {
            //时间没有被重置的时候需要重新计算一波
            //说明之前计时过
            if (startUseAppTimestamp != 0L)
                currentUseAppTime += (System.currentTimeMillis() - startUseAppTimestamp) / 1000L
        }
        startUseAppTimestamp = System.currentTimeMillis()
        LogUtils.log("任务被开启，当前已经使用app的时长为：" + currentUseAppTime + "s")
        //发起显示cover的任务
        //计算时间
        val coverTimeDelay = HealthManagerHelper.getStudyTimeLimit() - currentUseAppTime
        LogUtils.log("将在 " + (if (coverTimeDelay <= 0) 0 else coverTimeDelay) + "s 后显示提示浮层")
        handler?.sendEmptyMessageDelayed(
            SHOW_COVER,
            (if (coverTimeDelay <= 0) 0 else coverTimeDelay) * 1000L
        )
    }

    /**
     * 重置数据
     */
    internal fun resetData() {
        LogUtils.log("任务完成，数据重置")
        currentUseAppTime = 0
        inBackgroundTimestamp = 0
        startUseAppTimestamp = 0

        //移除任务
        handler?.removeMessages(SHOW_COVER)
        handler?.removeMessages(SHOW_DIALOG)
    }

    /**
     * 停止延时任务
     */
    internal fun stopDelayTask() {
        //移除任务
        handler?.removeMessages(SHOW_COVER)
        handler?.removeMessages(SHOW_DIALOG)

        inBackgroundTimestamp = System.currentTimeMillis()

        //记录当前APP使用的时间
        currentUseAppTime += (System.currentTimeMillis() - startUseAppTimestamp) / 1000L

        LogUtils.log("任务被停止，当前已经使用app的时长为：" + currentUseAppTime + "s")
    }
}