package com.example.baseui2.test

import androidx.lifecycle.Observer
import com.example.baseui2.R
import com.whr.baseui.base.MyBaseVMActivity
import kotlinx.android.synthetic.main.mvvm_test.*

class TestActivity:MyBaseVMActivity<TestVModel>() {
    override fun requestData() {
        mViewModel.requestTestData()
        mViewModel.test.observe(this, Observer {
            resultTv.text=it
        })
    }

    override val layoutId: Int
        get() = R.layout.mvvm_test

    override fun initViews() {
    }
}