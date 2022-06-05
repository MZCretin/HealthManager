# HealthManager
App健康管理组件 Android 健康提醒组件

## 简介
健康管理是针对APP使用者的一个提醒功能，是娱乐型APP中常用的功能！
该组件包含如下功能：
+ 1.周期性提醒功能，比如用户使用半小时之后提醒一次
+ 2.夜深提醒功能，比如超过晚上九点半使用APP，提醒用户太晚了

## 效果截图

+ 周期性提醒页面

<img src="./doc/images/img1.png" alt="img1" style="zoom:50%;" />

+ 深夜提醒页面

  <img src="./doc/images/img2.png" alt="img2" style="zoom:50%;" />

## 引入组件

### Step 1 Add it in your root build.gradle at the end of repositories:

```
allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
```



### Step 2 Add the dependency

```
dependencies {
	        implementation 'com.github.MZCretin:HealthManager:v1.0.0'
}
```



### Step3 Init SDK

Initialization on the BaseApplication is recommended.

```kotlin
        var startHour = 21
        var startMinute = 0
        var startSecond = 0
        var endHour = 6
        var endMinute = 0
        var endSecond = 0
        var overDay = 1
        var appUseLimit = 1800
        var backgroundTime = 300

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
```

