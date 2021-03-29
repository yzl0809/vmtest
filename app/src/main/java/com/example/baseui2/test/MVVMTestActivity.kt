package com.example.baseui2.test

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.example.baseui2.R
import com.whr.baseui.base.BaseMvvmActivity
import kotlinx.android.synthetic.main.testlayout.*

class MVVMTestActivity: BaseMvvmActivity<TestVModel>() {
    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        mViewModel.requestTestData()
        mViewModel.test.observe(this, Observer {
            mViewModel.showToast(it!!)
        })
    }

    override fun getLayoutId(): Int {
       return R.layout.testlayout
    }
}