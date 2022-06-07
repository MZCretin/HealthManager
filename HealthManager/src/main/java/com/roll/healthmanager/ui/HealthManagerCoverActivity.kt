package com.roll.healthmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.Intent.*
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.roll.healthmanager.HealthManagerHelper
import com.roll.healthmanager.R
import com.roll.healthmanager.helper.TaskHelper

//这个activity是用来做遮罩层的 所以需要去掉状态栏的一些信息
class HealthManagerCoverActivity : AppCompatActivity() {
    private var isClick = false

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HealthManagerCoverActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(FLAG_ACTIVITY_NEW_TASK)
            }
            intent.addFlags(FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(HealthManagerHelper.getLayoutCover())

        supportActionBar?.hide()

        actionBar?.hide()

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

//      设置点击事件
        findViewById<View>(R.id.id_health_ok)?.setOnClickListener {
            finishPage()
        }
        findViewById<View>(R.id.id_health_ok_v2)?.setOnClickListener {
            finishPage()
        }
        findViewById<View>(R.id.id_health_ok_v3)?.setOnClickListener {
            finishPage()
        }
        findViewById<View>(R.id.id_health_ok_v4)?.setOnClickListener {
            finishPage()
        }
    }

    fun finishPage() {
        //如果这个页面destroyed之后需要重新计时
        HealthManagerHelper.resetTask()
        HealthManagerHelper.startDelayTask()

        finish()
    }

    override fun finish() {
        super.finish()
        TaskHelper.finishTask()
        overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
    }
}