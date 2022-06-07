package com.roll.healthmanager.ui

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import com.roll.healthmanager.HealthManagerHelper
import com.roll.healthmanager.R
import com.roll.healthmanager.helper.TaskHelper

class HealthManagerDialogActivity : AppCompatActivity() {

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, HealthManagerDialogActivity::class.java)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            context.startActivity(intent)
            if (context is Activity) {
                context.overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(HealthManagerHelper.getLayoutDialog())

        supportActionBar?.hide()

        actionBar?.hide()

        //沉浸式状态栏
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }

        findViewById<View>(R.id.id_health_ok)?.setOnClickListener {
            pageFinish()
        }

        findViewById<View>(R.id.id_health_ok_v2)?.setOnClickListener {
            pageFinish()
        }

        findViewById<View>(R.id.id_health_ok_v3)?.setOnClickListener {
            pageFinish()
        }

        findViewById<View>(R.id.id_health_ok_v4)?.setOnClickListener {
            pageFinish()
        }
    }

    fun pageFinish() {
        //如果这个页面destroyed之后需要重新计时
        HealthManagerHelper.startDelayTask()
        TaskHelper.finishTask()
        finish()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.fast_fade_in, R.anim.fast_fade_out);
    }
}