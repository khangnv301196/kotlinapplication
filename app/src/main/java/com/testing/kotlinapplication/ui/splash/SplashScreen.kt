package com.testing.kotlinapplication.ui.splash

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.PersistableBundle
import androidx.appcompat.app.AppCompatActivity
import com.testing.kotlinapplication.MainActivity
import com.testing.kotlinapplication.ui.staff.StaffActivity
import com.testing.kotlinapplication.util.Constant
import com.testing.kotlinapplication.util.Preference

class SplashScreen : AppCompatActivity() {
    private lateinit var preference: Preference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        preference = Preference(this)
        Handler(Looper.getMainLooper()).postDelayed(Runnable {
            var islogin = preference.getValueBoolien(Constant.IS_LOGIN, false)
            var usertype = preference.getValueInt(Constant.USER_TYPE)
            if ((islogin == true) && usertype == 2) {
                var intent = Intent(this, StaffActivity::class.java)
                startActivity(intent)
            } else {
                var intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
            }
        }, 3000)
    }
}