package com.roll.healthmanagerproject

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.roll.healthmanager.ui.test.HealthManagerConfigActivity
import com.roll.healthmanager.ui.test.HealthManagerLogActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<Button>(R.id.btn_open).setOnClickListener {
            startActivity(Intent(this, HealthManagerLogActivity::class.java))
        }
        findViewById<Button>(R.id.btn_config).setOnClickListener {
            startActivity(Intent(this, HealthManagerConfigActivity::class.java))
        }
    }
}