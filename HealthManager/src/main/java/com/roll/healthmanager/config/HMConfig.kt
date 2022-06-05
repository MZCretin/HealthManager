package com.roll.healthmanager.config

/**
 * Copyright (C), 2015-2021, 编程猫有限公司
 * FileName: CMConfig
 * Author: cretin
 * Date: 2021/7/31 12:55 下午
 * Description: 健康管理配置类
 */
class HMConfig {

    //是否是debug环境
    internal var isDebug = false

    //定义可以累计使用app的时间 单位秒
    internal var studyTimeLimit = 30 * 60

    //定义允许app在后台的最大有效时间 单位秒
    internal var maxInBackgroundTime = 5 * 60

    //浮层的layout
    internal var layoutCover: Int = 0

    //设置app的欢迎页 欢迎页面是不被统计的
    internal var appSplashActivity = ""

    //对话框的layout
    internal var layoutDialog: Int = 0

    //夜深模式开始和结束的小时
    internal var dailyNightModeStartHour = 21
    internal var dailyNightModeEndHour = 6

    //夜深模式开始和结束的分钟
    internal var dailyNightModeStartMinute = 0
    internal var dailyNightModeEndMinute = 0

    //夜深模式开始和结束的秒
    internal var dailyNightModeStartSecond = 0
    internal var dailyNightModeEndSecond = 0

    //夜深模式时间配置是否跨天
    internal var dailyNightModeOverDay = true

    companion object {

        /**
         * 创建空的配置类 所有属性都使用默认的
         */
        internal fun createEmptyConfig(): HMConfig {
            return HMConfig()
        }

        /**
         * 创建配置类
         */
        fun createConfig(): HMConfig {
            return createEmptyConfig()
        }
    }

    /**
     * 设置浮层的layout布局
     */
    fun setLayoutCover(layoutCover: Int): HMConfig {
        this.layoutCover = layoutCover
        return this
    }

    /**
     * 设置夜深模式信息
     */
    fun setNightModeInfo(
        dailyNightModeStartHour: Int,
        dailyNightModeStartMinute: Int,
        dailyNightModeStartSecond: Int,
        dailyNightModeEndHour: Int,
        dailyNightModeEndMinute: Int,
        dailyNightModeEndSecond: Int,
        dailyNightModeOverDay: Boolean
    ): HMConfig {
        this.dailyNightModeStartHour = dailyNightModeStartHour
        this.dailyNightModeStartMinute = dailyNightModeStartMinute
        this.dailyNightModeStartSecond = dailyNightModeStartSecond
        this.dailyNightModeEndHour = dailyNightModeEndHour
        this.dailyNightModeEndMinute = dailyNightModeEndMinute
        this.dailyNightModeEndSecond = dailyNightModeEndSecond
        this.dailyNightModeOverDay = dailyNightModeOverDay
        return this
    }

    /**
     * 设置对话框的layout布局
     */
    fun setLayoutDialog(layoutDialog: Int): HMConfig {
        this.layoutDialog = layoutDialog
        return this
    }

    /**
     * 设置弹出浮层的时间间隔
     */
    fun setStudyTimeLimit(studyTimeLimit: Int): HMConfig {
        this.studyTimeLimit = studyTimeLimit
        return this
    }

    /**
     * 设置app的欢迎页 如果没有就不设置
     */
    fun setAppSplashActivity(appSplashActivity: Class<*>): HMConfig {
        this.appSplashActivity = appSplashActivity.canonicalName ?: ""
        return this
    }

    /**
     * 设置允许app在后台最大有效时间
     */
    fun setMaxInBackgroundTime(maxInBackgroundTime: Int): HMConfig {
        this.maxInBackgroundTime = maxInBackgroundTime
        return this
    }

    /**
     * 是否是debug环境 debug环境会输出本地日志
     */
    fun isDebug(debug: Boolean): HMConfig {
        this.isDebug = debug
        return this
    }
}