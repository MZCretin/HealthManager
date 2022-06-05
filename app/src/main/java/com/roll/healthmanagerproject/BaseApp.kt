package com.roll.healthmanagerproject

import android.app.Application
import com.roll.healthmanager.HealthManager
import com.roll.healthmanager.config.HMConfig

class BaseApp : Application() {

    override fun onCreate() {
        super.onCreate()

        //一般在BaseApp初始化
        initHealthManager()
    }

    private fun initHealthManager() {
        var startHour = 21
        var startMinute = 0
        var startSecond = 0
        var endHour = 6
        var endMinute = 0
        var endSecond = 0
        var overDay = 1
        var appUseLimit = 1800
        var backgroundTime = 300
        //取出本地缓存数据
        if (BuildConfig.DEBUG) {
            //从本地环境获取配置 该功能由SDK提供
            val split = HealthManager.getLocalConfigInfo(this)
            if (split.size == 9) {
                try {
                    startHour = split[0]
                    startMinute = split[1]
                    startSecond = split[2]
                    endHour = split[3]
                    endMinute = split[4]
                    endSecond = split[5]
                    overDay = split[6]
                    appUseLimit = split[7]
                    backgroundTime = split[8]
                } catch (e: Exception) {

                }
            }
        }

        //健康管理
        val config = HMConfig
            //创建配置
            .createConfig()
            //设置环境 debug环境有日志输出 TAG为HealthManagerSDK
            .isDebug(BuildConfig.DEBUG)
            //设置允许APP在后台被计时的最长时间，比如设置5分钟，那么应用处于后台超过5分钟，就清空之前的计时，下次进入app就从头开始计时，否则就继续计时
            .setMaxInBackgroundTime(backgroundTime)
            //设置单次可使用的最长时间，当超过这个时间就会弹窗提醒
            .setStudyTimeLimit(appUseLimit)
            //设置欢迎页面，因为在这个页面不能显示健康管理弹窗 没有欢迎页面就不设置
            .setAppSplashActivity(SplashActivity::class.java)
            //设置周期性弹窗的ui
            .setLayoutCover(R.layout.activity_health_manager_cover)
            //设置夜深了弹簧ui
            .setLayoutDialog(R.layout.activity_health_manager_dialog)
            //设置夜深模式信息 即设置夜深模式开始的时间和结束时间 dailyNightModeOverDay指的是你设置的时间是否是跨越当日的
            //比如你设置深夜时间指的是当天晚上21：00到次日06：00 那就是 startHour = 21 startMinute = 0 startSecond = 0 endHour = 6  endMinute = 0 endSecond = 0 dailyNightModeOverDay = true
            //比如你设置深夜时间指的是当天晚上21：00到次日当天晚上23：00 那就是 startHour = 21 startMinute = 0 startSecond = 0 endHour = 23  endMinute = 0 endSecond = 0 dailyNightModeOverDay = false
            .setNightModeInfo(
                startHour,
                startMinute,
                startSecond,
                endHour,
                endMinute,
                endSecond,
                overDay == 1
            )
        //初始化
        HealthManager.init(this, config)
    }
}