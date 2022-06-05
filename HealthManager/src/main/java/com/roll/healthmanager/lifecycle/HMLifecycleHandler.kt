package com.roll.healthmanager.lifecycle

import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.text.TextUtils
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.lifecycle.ProcessLifecycleOwner
import com.roll.healthmanager.HealthManagerHelper
import com.roll.healthmanager.helper.TaskHelper

class HMLifecycleHandler : Application.ActivityLifecycleCallbacks, LifecycleObserver {
    //本地记录topactivity
    internal var topActivity: Activity? = null

    //记录页面个数
    private var activityCount = 0

    //是否是启动app
    internal var isLaunchApp = false

    //是否是主页需要被忽略
    internal var isIgnored = false

    init {
        ProcessLifecycleOwner.get().lifecycle.addObserver(this)
    }

    /**
     * 判断是否有欢迎页面
     */
    private fun hasSplashActivity(): Boolean {
        return !TextUtils.isEmpty(HealthManagerHelper.getAppSplashActivity())
    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {

        /**
         * 统计信息
         */
        fun statisticsActivity() {
            isIgnored = false
            isLaunchApp = activityCount == 0
            if (isLaunchApp) {
                TaskHelper.startDelayTask()
                TaskHelper.hasShowDialog = false
            }
            activityCount++
        }

        if (hasSplashActivity()) {
            if (activity.javaClass.canonicalName != HealthManagerHelper.getAppSplashActivity()) {
                statisticsActivity()
            } else {
                TaskHelper.hasShowDialog = false
                isIgnored = true
            }
        } else {
            statisticsActivity()
        }
    }

    override fun onActivityStarted(activity: Activity) {
    }

    override fun onActivityResumed(activity: Activity) {
        topActivity = activity
    }

    override fun onActivityPaused(activity: Activity) {

    }

    override fun onActivityStopped(activity: Activity) {

    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (hasSplashActivity()) {
            if (activity.javaClass.canonicalName != HealthManagerHelper.getAppSplashActivity()) {
                activityCount--
            }
        } else {
            activityCount--
        }

        if (topActivity == activity) {
            topActivity = null
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    fun onAppBackgrounded() {
        //取消任务
        HealthManagerHelper.stopDelayTask()
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    fun onAppForegrounded() {
        //启动任务
        if (!isIgnored)
            HealthManagerHelper.startDelayTask()
    }
}