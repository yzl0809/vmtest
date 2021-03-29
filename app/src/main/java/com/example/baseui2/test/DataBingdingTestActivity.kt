package com.example.baseui2.test

import android.os.Bundle
import androidx.lifecycle.Observer
import com.example.baseui2.R
import com.example.baseui2.databinding.DatabingdingBinding
import com.whr.baseui.base.BaseDataBindingActivity
import kotlinx.android.synthetic.main.databingding.*

class DataBingdingTestActivity:BaseDataBindingActivity<DatabingdingBinding,TestVModel> (){
    override fun getLayoutId(): Int {
        return R.layout.databingding
    }

    override fun initViews(savedInstanceState: Bundle?) {
        super.initViews(savedInstanceState)
        binding.item=mViewModel.mData?.value
        mViewModel.requestTestBean()
        mViewModel.test.observe(this, Observer {
            content.text=it
            mViewModel.showToast(it!!)
        })
    }
}