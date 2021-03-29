package com.example.baseui2

import android.app.Application
import android.content.Context
import android.util.Log

import com.whr.baseui.helper.UiCoreHelper
import com.whr.ktxmvp.impl.UiCoreProxyImpl
import kotlin.properties.Delegates


class App : Application() {

    override fun onCreate() {
        super.onCreate()
        appContext = this
        startTime=System.currentTimeMillis()
        initUiCoreProxy()
    }

    companion object {
        lateinit var appContext: Context
        var startTime by Delegates.notNull<Long>()
    }

    private fun initUiCoreProxy() {
        UiCoreHelper.setProxyA(UiCoreProxyImpl())
    }
}

