package com.roll.healthmanagerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import java.util.*

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        var time = 3
        findViewById<TextView>(R.id.tv_splash).let {
            Timer().schedule(object : TimerTask() {
                override fun run() {
                    it.text = "我是欢迎页面，${time--}秒后关闭"
                }
            }, 0, 1000)

            it.postDelayed({
                startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                finish()
            }, 3000)
        }


    }
}