package com.example.baseui2.test

import androidx.lifecycle.MutableLiveData
import com.example.baseui2.ApiHelper
import com.whr.baseui.mvvm.BaseViewModel

class TestVModel:BaseViewModel() {
    var test: MutableLiveData<String> = MutableLiveData()
    fun requestTestData() {
        showWaitDialog()
        launchRequest({
            ApiHelper.api().requestTestApi("utf-8", "卫衣").await()
        }, { data: List<List<String>>? ->
            test.value = data.toString()
        }, { errMsg: String? ->
            showToast(errMsg)
        }, {
            hideWaitDialog()
        })
    }

     var mData: MutableLiveData<TestBean> = MutableLiveData()
    fun requestTestBean() {
        showWaitDialog()
        launchRequest({
            ApiHelper.api().requestTestApi("utf-8", "卫衣").await()
        }, { data: List<List<String>>? ->
            var bean=TestBean()
            bean.content=data.toString()
            mData.value=bean
        }, { errMsg: String? ->
            showToast(errMsg)
        }, {
            hideWaitDialog()
        })
    }
}