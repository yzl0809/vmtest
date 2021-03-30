package com.example.baseui2.test

import androidx.lifecycle.Observer
import com.example.baseui2.BR
import com.example.baseui2.R
import com.example.baseui2.databinding.DatabingdingTestBinding
import com.whr.baseui.base.MyBaseBingDingVMActivity
import kotlinx.android.synthetic.main.databingding_test.*

class DataBingDingTestActivity:MyBaseBingDingVMActivity<DatabingdingTestBinding,TestVModel> (){


    override val layoutId: Int
        get() = R.layout.databingding_test

    override fun initViews() {
    }


    override fun requestData() {
        mViewModel.requestTestData()
        mViewModel.test.observe(this, Observer {
            content.text=it
            mViewModel.showToast(it!!)
        })
    }

    override fun initVariableId(): Int {
        return BR.mainVM
    }
}