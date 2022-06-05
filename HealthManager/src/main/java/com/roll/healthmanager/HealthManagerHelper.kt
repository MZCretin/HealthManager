package com.roll.healthmanager

import android.annotation.SuppressLint
import android.app.Application
import android.text.TextUtils
import com.roll.healthmanager.lifecycle.HMLifecycleHandler
import com.roll.healthmanager.config.HMConfig
import com.roll.healthmanager.helper.TaskHelper
import com.roll.healthmanager.utils.LogUtils
import java.lang.RuntimeException

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: HealthManagerHelper
 * Author: cretin
 * Date: 2021/8/17 4:38 下午
 * Description: 健康管理帮助类
 */
object HealthManagerHelper {

    //本地保存当前日志的配置信息
    internal var config: HMConfig? = null
    internal var application: Application? = null

    private var isInit = false

    //生命周期管理者
    @SuppressLint("StaticFieldLeak")
    internal var lifecycleHandler: HMLifecycleHandler? = null

    /**
     * 初始化
     */
    internal fun init(application: Application, config: HMConfig?) {
        if (isInit) {
            return
        }
        isInit = true

        //检查有没有设置布局
        if (config?.layoutCover == 0) {
            throw RuntimeException("请设置浮层的布局文件")
        }
        if (config?.layoutDialog == 0) {
            throw RuntimeException("请设置对话框的布局文件")
        }

        this.application = application
        this.lifecycleHandler = HMLifecycleHandler()
        //注册生命周期
        application.registerActivityLifecycleCallbacks(lifecycleHandler)
        if (config == null) {
            this.config = HMConfig.createEmptyConfig()
        } else {
            this.config = config
        }
        TaskHelper.init(application)

        LogUtils.log(
            "健康管理初始化完成，您当前的配置为：\n" +
                    "夜深模式时间范围：" + config?.dailyNightModeStartHour?.toString() + "时" + config?.dailyNightModeStartMinute + "分" + config?.dailyNightModeStartSecond + "秒" + (if (config?.dailyNightModeOverDay == true) {
                " 至次日 "
            } else {
                " 至 "
            }) + config?.dailyNightModeEndHour?.toString() + "时" + config?.dailyNightModeEndMinute + "分" + config?.dailyNightModeEndSecond + "秒" + "\n" +
                    "单次累计可使用app最大时长：${config?.studyTimeLimit}s\n"+
                    "允许app在后台的最大有效时间：${config?.maxInBackgroundTime}s\n"+
                    "欢迎页面全路径地址：${if(TextUtils.isEmpty(config?.appSplashActivity)) "未配置" else "已配置："+config?.appSplashActivity}"
        )
    }

    /**
     * 是否是debug
     */
    internal fun isDebug(): Boolean {
        if (config != null) {
            return config?.isDebug ?: false
        }
        return false
    }

    /**
     * 获取允许app在后台的最大有效时间 单位秒
     */
    internal fun getMaxInBackgroundTime(): Int {
        return config?.maxInBackgroundTime ?: 5 * 60
    }

    /**
     * 获取夜深模式的信息
     */
    internal fun getNightModeInfo(): Array<Int> {
        if (config != null) {
            return arrayOf(
                config?.dailyNightModeStartHour ?: 21,
                config?.dailyNightModeStartMinute ?: 0,
                config?.dailyNightModeStartSecond ?: 0,
                config?.dailyNightModeEndHour ?: 6,
                config?.dailyNightModeEndMinute ?: 6,
                config?.dailyNightModeEndSecond ?: 6,
                if (config?.dailyNightModeOverDay == true) 1 else 0
            )
        }
        return arrayOf(
            21, 0, 0, 6, 0, 0, 1
        )
    }

    /**
     * 获取浮层的layout布局
     */
    internal fun getLayoutCover(): Int {
        return config?.layoutCover ?: 0
    }

    /**
     * 获取对话框的layout布局
     */
    internal fun getLayoutDialog(): Int {
        return config?.layoutDialog ?: 0
    }

    /**
     * 获取可以累计使用app的时间 单位秒 默认为30分钟
     */
    internal fun getStudyTimeLimit(): Int {
        return config?.studyTimeLimit ?: 30 * 60
    }

    /**
     * 获取app的class
     */
    internal fun getAppSplashActivity(): String {
        return config?.appSplashActivity ?: ""
    }

    /**
     * 开启延时任务
     */
    internal fun startDelayTask() {
        TaskHelper.startDelayTask()
    }

    /**
     * 停止延时任务
     */
    internal fun stopDelayTask() {
        TaskHelper.stopDelayTask()
    }

    /**
     * 重置任务
     */
    internal fun resetTask() {
        TaskHelper.resetData()
    }

}