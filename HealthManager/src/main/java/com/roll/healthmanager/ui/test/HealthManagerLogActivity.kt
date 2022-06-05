package com.roll.healthmanager.ui.test

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.View
import android.widget.TextView
import com.roll.healthmanager.HealthManager
import com.roll.healthmanager.R

class HealthManagerLogActivity : AppCompatActivity() {
    private var tvLog:TextView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_health_manager_log)

        tvLog = findViewById(R.id.tv_log)
        findViewById<View>(R.id.tv_back).setOnClickListener {
            finish()
        }
        tvLog?.movementMethod = ScrollingMovementMethod.getInstance();
        HealthManager.getLog().forEach {
            tvLog?.append(it + "\n\n")
        }
    }
}