package com.testing.kotlinapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.ui.staff.StaffActivity

class SplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            var intent = Intent(this, StaffActivity::class.java)
            startActivity(intent)
        }, 3000)
    }
}