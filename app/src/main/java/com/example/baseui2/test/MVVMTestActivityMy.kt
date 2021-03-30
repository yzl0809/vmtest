package com.example.baseui2.test

import androidx.lifecycle.Observer
import com.example.baseui2.R
import com.whr.baseui.base.MyBaseVMActivity

class MVVMTestActivityMy: MyBaseVMActivity<TestVModel>() {
    override val layoutId: Int
        get() = R.layout.mvvm_test

    override fun initViews() {

    }

    override fun requestData() {
        mViewModel.requestTestData()
        mViewModel.test.observe(this, Observer {
            mViewModel.showToast(it!!)
        })
    }
}