package com.whr.baseui.base

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.whr.baseui.bean.ViewStatusEnum
import com.whr.baseui.helper.UiCoreHelper
import com.whr.baseui.mvvm.BaseViewModel
import com.whr.baseui.utils.*

/**
 * dataBingding+MVVM基类
 */
abstract class MyBaseBingDingVMActivity<V : ViewDataBinding, VM : BaseViewModel> : MyBaseActivity(), LifecycleObserver {
    lateinit var mBinding: V
    lateinit var mViewModel: VM
    private var providerVMClass: Class<VM>? = null
    lateinit var mActivity: MyBaseBingDingVMActivity<*, *>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mActivity = this
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
        AppManager.getAppManager().addActivity(this)
        UiCoreHelper.getProxyA().onActivityCreate(this)
        //初始化MVVM
        initMVVM()
        //初始化dataBingding
        initViewDataBinding()
        //返回状态监听
        vStatusObserve()
        //接口请求
        requestData()
    }


    abstract fun initVariableId(): Int

    /**
     * 注入绑定
     */
    private fun initViewDataBinding() {
        //DataBindingUtil类需要在project的build中配置 dataBinding {enabled true }, 同步后会自动关联android.databinding包
        mBinding = DataBindingUtil.setContentView(this,layoutId )
        mBinding.setVariable(initVariableId(), mViewModel)
        mBinding.executePendingBindings()
        //liveData绑定activity，fragment生命周期
        mBinding.lifecycleOwner = this
        setContentView(layoutId)
    }


    private fun initMVVM() {
        providerVMClass = TUtils.getT<VM>(this, 1).javaClass
        providerVMClass?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

    /**
     * 当activity结束时候调用
     */
    fun onActivityFinish() {
        finish()
    }




    /**
     * 初始化控件
     *
     * @param rootView
     */
    protected abstract fun requestData()








    /**
     * 重写此方法是为了Fragment的onActivityResult
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        UiCoreHelper.getProxyA().onActivityResult(this, requestCode, resultCode, data)
    }


    private fun vStatusObserve() {
        mViewModel.apply {
            vStatus.observe(this@MyBaseBingDingVMActivity,
                Observer {
                    when (it["status"]) {
                        ViewStatusEnum.SHOWWAITDIALOG -> {
                            if (it.containsKey("msg")) {
                                if (it.containsKey("cancelable"))
                                    showWaitDialog(
                                        it["msg"].toString(),
                                        it["cancelable"] as Boolean
                                    )
                                else
                                    mActivity.showWaitDialog(it["msg"].toString())
                            } else {
                                mActivity.showWaitDialog()
                            }
                        }
                        ViewStatusEnum.HIDEWAITDIALOG -> {
                            mActivity.hideWaitDialog()
                        }
                        ViewStatusEnum.SHOWTOAST -> {
                            mActivity.showToast(it["msg"].toString())
                        }
                    }
                })
        }
    }






    override fun onStart() {
        super.onStart()
        UiCoreHelper.getProxyA().onActivityStart(this)
    }

    override fun onRestart() {
        super.onRestart()
        UiCoreHelper.getProxyA().onActivityRestart(this)
    }

    override fun onResume() {
        super.onResume()
        UiCoreHelper.getProxyA().onActivityResume(this)
    }

    override fun onPause() {
        super.onPause()
        UiCoreHelper.getProxyA().onActivityPause(this)
    }

    override fun onStop() {
        super.onStop()
        UiCoreHelper.getProxyA().onActivityStop(this)
    }


    override fun onDestroy() {
        mViewModel.let {
            lifecycle.removeObserver(it)
        }
        hideSystemSoftInput()
        super.onDestroy()
        AppManager.getAppManager().finishActivity(this)
        UiCoreHelper.getProxyA().onActivityDestory(this)
    }



}