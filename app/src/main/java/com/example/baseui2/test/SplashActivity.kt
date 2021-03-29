package com.example.baseui2.test

import android.content.Intent
import android.os.Bundle
import com.example.baseui2.R
import com.whr.baseui.base.BaseActivity
import kotlinx.android.synthetic.main.splash.*

class SplashActivity:BaseActivity() {

    override fun getLayoutId(): Int {
        return R.layout.splash
    }


    override fun initViews(savedInstanceState: Bundle?) {
        btn1.setOnClickListener {
            intent = Intent(this,MVVMTestActivity::class.java)
        startActivity(intent)
        }
        btn2.setOnClickListener {
            intent = Intent(this,DataBingdingTestActivity::class.java)
            startActivity(intent)
        }
    }

}