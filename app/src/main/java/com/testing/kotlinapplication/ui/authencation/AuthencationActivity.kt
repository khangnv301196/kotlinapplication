package com.testing.kotlinapplication.ui.authencation

import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.testing.kotlinapplication.R

class AuthencationActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authencation)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }
    fun hideSoftKeyBoard() {
        var inputMethodManager =
            getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(currentFocus?.windowToken, 0)
    }

    fun setUpUI(view: View) {
        if (!(view is EditText)) {
            view.setOnTouchListener(object : View.OnTouchListener {
                override fun onTouch(p0: View?, p1: MotionEvent?): Boolean {
                    hideSoftKeyBoard();
                    return false;
                }
            })
        }
    }

    fun setUIGroup(viewGroup: ViewGroup) {
        for (i in 0..viewGroup.childCount-1) {
            var innerView = viewGroup.getChildAt(i)
            setUpUI(innerView)
        }
    }
}