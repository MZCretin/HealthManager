package com.roll.healthmanager

import android.app.Application
import android.content.Context
import android.text.TextUtils
import com.roll.healthmanager.config.HMConfig
import com.roll.healthmanager.utils.HealthManagerSPUtils
import com.roll.healthmanager.utils.LogUtils

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: HealthManagerHelper
 * Author: cretin
 * Date: 2021/8/16 3:15 下午
 * Description: 健康管理操作类
 */
object HealthManager {
    //防止多次初始化
    private var isInit = false

    /**
     * 初始化
     */
    fun init(application: Application, config: HMConfig) {
        if (isInit) {
            return
        }
        isInit = true
        //配置参数
        HealthManagerHelper.init(application, config)
    }

    /**
     * 获取log日志列表
     */
    fun getLog(): MutableList<String> {
        return LogUtils.getLog() ?: mutableListOf()
    }

    /**
     * 获取本地缓存信息 请注意 请仅在测试环境下使用本数据 正式环境请使用您特定的数据
     *
     * 请注意 返回的是一个数据 数组长度为9 从0~8依次是
     * startHour，startMinute，startSecond，endHour，endMinute，endSecond，overDay，appUseLimit，backgroundTime
     */
    fun getLocalConfigInfo(context: Context, defaultData: Array<Int>? = null): Array<Int> {
        var startHour = 21
        var startMinute = 0
        var startSecond = 0
        var endHour = 6
        var endMinute = 0
        var endSecond = 0
        var overDay = 1
        var appUseLimit = 1800
        var backgroundTime = 300
        //如果你传了defaultData 说明你对我给的默认数据不是很满意
        if (defaultData != null && defaultData.size == 9) {
            startHour = defaultData[0].toInt()
            startMinute = defaultData[1].toInt()
            startSecond = defaultData[2].toInt()
            endHour = defaultData[3].toInt()
            endMinute = defaultData[4].toInt()
            endSecond = defaultData[5].toInt()
            overDay = defaultData[6].toInt()
            appUseLimit = defaultData[7].toInt()
            backgroundTime = defaultData[8].toInt()
        }
        //从本地环境获取配置
        val healthManagerConfig: String =
            HealthManagerSPUtils.getInstance(context).getString("health_manager_config", "")
        if (!TextUtils.isEmpty(healthManagerConfig)) {
            val split = healthManagerConfig.split(" ").toTypedArray()
            if (split.size == 9) {
                try {
                    startHour = split[0].toInt()
                    startMinute = split[1].toInt()
                    startSecond = split[2].toInt()
                    endHour = split[3].toInt()
                    endMinute = split[4].toInt()
                    endSecond = split[5].toInt()
                    overDay = split[6].toInt()
                    appUseLimit = split[7].toInt()
                    backgroundTime = split[8].toInt()
                } catch (e: Exception) {
                }
            }
        }
        return arrayOf(
            startHour,
            startMinute,
            startSecond,
            endHour,
            endMinute,
            endSecond,
            overDay,
            appUseLimit,
            backgroundTime
        )
    }

}