package com.roll.healthmanager.ui.test

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Process
import android.text.TextUtils
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SwitchCompat
import com.roll.healthmanager.HealthManagerHelper
import com.roll.healthmanager.R
import com.roll.healthmanager.utils.HealthManagerSPUtils

class HealthManagerConfigActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        setContentView(R.layout.activity_health_manager_config)

        initData()
    }

    private fun initData() {
        //这里的代码看起来很恶心 其实不重要 哈哈哈 一个给测试的工具页面 要什么自行车

        //获取一下ui组件
        val tv_back = findViewById<TextView>(R.id.tv_back)
        val ed_single_time = findViewById<EditText>(R.id.ed_single_time)
        val ed_background_time = findViewById<EditText>(R.id.ed_background_time)
        val ed_start_time1 = findViewById<EditText>(R.id.ed_start_time1)
        val ed_start_time2 = findViewById<EditText>(R.id.ed_start_time2)
        val ed_start_time3 = findViewById<EditText>(R.id.ed_start_time3)
        val ed_start_time4 = findViewById<EditText>(R.id.ed_start_time4)
        val ed_start_time5 = findViewById<EditText>(R.id.ed_start_time5)
        val ed_start_time6 = findViewById<EditText>(R.id.ed_start_time6)
        val switch_over_day = findViewById<SwitchCompat>(R.id.switch_over_day)
        val btn_save = findViewById<Button>(R.id.btn_save)

        tv_back.setOnClickListener {
            finish()
        }

        //这里的配置信息 从左往右一次是 开始时间 时 分 秒 结束时间 时 分 秒 是否跨天 跨天1不跨天0 单次累计可使用app最大时长，单位s 允许app在后台的最大有效时间，单位s
        val healthManagerConfig =
            HealthManagerSPUtils.getInstance(this).getString("health_manager_config", "")

        var startHour = 21
        var startMinute = 0
        var startSecond = 0
        var endHour = 6
        var endMinute = 0
        var endSecond = 0
        var overDay = 1
        var appUseLimit = 1800
        var backgroundTime = 300
        if (!TextUtils.isEmpty(healthManagerConfig)) {
            val split = healthManagerConfig.split(" ")
            if (split.size == 9) {
                startHour = split[0].toInt()
                startMinute = split[1].toInt()
                startSecond = split[2].toInt()
                endHour = split[3].toInt()
                endMinute = split[4].toInt()
                endSecond = split[5].toInt()
                overDay = split[6].toInt()
                appUseLimit = split[7].toInt()
                backgroundTime = split[8].toInt()
            }
        }

        //显示数据
        ed_single_time?.setText("" + appUseLimit)
        ed_background_time?.setText("" + backgroundTime)
        ed_start_time1?.setText("" + startHour)
        ed_start_time2?.setText("" + startMinute)
        ed_start_time3?.setText("" + startSecond)
        ed_start_time4?.setText("" + endHour)
        ed_start_time5?.setText("" + endMinute)
        ed_start_time6?.setText("" + endSecond)
        switch_over_day?.isChecked = overDay == 1

        fun getNum(editText: EditText): Int {
            val text = editText?.text?.toString()?.replace(" ", "")?.trim()
            if (TextUtils.isEmpty(text)) {
                return -1
            }
            return text?.toInt() ?: -1
        }

        btn_save?.setOnClickListener {
            try {
                val startHour = getNum(ed_start_time1)
                val startMinute = getNum(ed_start_time2)
                val startSecond = getNum(ed_start_time3)
                val endHour = getNum(ed_start_time4)
                val endMinute = getNum(ed_start_time5)
                val endSecond = getNum(ed_start_time6)
                val overDay = if (switch_over_day.isChecked) 1 else 0
                val appUseLimit = getNum(ed_single_time)
                val backgroundTime = getNum(ed_background_time)
                if (startHour == -1 ||
                    startMinute == -1 ||
                    startSecond == -1 ||
                    endHour == -1 ||
                    endMinute == -1 ||
                    endSecond == -1 ||
                    appUseLimit == -1 ||
                    backgroundTime == -1
                ) {
                    Toast.makeText(this, "填入的数字不能为空", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
                hideSoftInput(this, it)
                //保存
                val saveHealthManagerConfig =
                    "$startHour $startMinute $startSecond $endHour $endMinute $endSecond $overDay $appUseLimit $backgroundTime"
                HealthManagerSPUtils.getInstance(this)
                    .save("health_manager_config", saveHealthManagerConfig)

                Toast.makeText(this, "保存成功，即将重启APP", Toast.LENGTH_LONG).show()
                ed_background_time?.postDelayed({
                    relaunchApp(true)
                }, 1000)
            } catch (e: Throwable) {

            }
        }
    }

    /**
     * Relaunch the application.
     *
     * @param isKillProcess True to kill the process, false otherwise.
     */
    fun relaunchApp(isKillProcess: Boolean) {
        val intent: Intent? = packageManager.getLaunchIntentForPackage(this.packageName)
        intent?.addFlags(
            Intent.FLAG_ACTIVITY_NEW_TASK
                    or Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK
        )
        HealthManagerHelper.application?.startActivity(intent)
        if (!isKillProcess) return
        Process.killProcess(Process.myPid())
        System.exit(0)
    }

    private fun hideSoftInput(
        healthManagerConfigActivity: HealthManagerConfigActivity,
        view: View
    ) {
        val imm: InputMethodManager =
            healthManagerConfigActivity.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        //隐藏软键盘
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0)
    }
}