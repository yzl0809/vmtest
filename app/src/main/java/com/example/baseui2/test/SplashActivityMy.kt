package com.example.baseui2.test

import android.content.Intent
import com.example.baseui2.R
import com.whr.baseui.base.MyBaseActivity
import kotlinx.android.synthetic.main.splash.*

class SplashActivityMy:MyBaseActivity() {
    override val layoutId: Int
        get() = R.layout.splash

    override fun initViews() {
        btn0.setOnClickListener {
            intent = Intent(this,TestActivity::class.java)
            startActivity(intent)
        }
        btn1.setOnClickListener {
            intent = Intent(this,MVVMTestActivityMy::class.java)
            startActivity(intent)
        }
        btn2.setOnClickListener {
            intent = Intent(this,DataBingDingTestActivity::class.java)
            startActivity(intent)
        }
    }

}