package com.whr.baseui.base

import android.app.Activity
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.gyf.immersionbar.ImmersionBar
import com.whr.baseui.widget.WaitProgressDialog

open abstract class BaseFragment : Fragment() {
    private var mRootView // 根布局
            : View? = null
    var TAG:String = javaClass.name
    private var mWaitDialog: WaitProgressDialog? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //防止穿透事件
        view.isClickable = true
        initViews(savedInstanceState)
    }

//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        if (mRootView == null) {
//            mRootView = inflater.inflate(getLayoutId(), null)
//            val parent = mRootView?.parent as ViewGroup
//            parent?.removeView(mRootView)
//        }
//        return mRootView
//    }
    abstract fun initViews(savedInstanceState: Bundle?)
    abstract fun getLayoutId(): Int
    /**
     * 偏移状态栏高度
     */
    fun offsetStatusBar(view: View) {
        view.setPadding(
            view.paddingStart,
            view.paddingTop + ImmersionBar.getStatusBarHeight(this),
            view.paddingEnd,
            view.paddingBottom
        )
    }

    fun setBackgroundAlpha(bgAlpha: Float) {
        val lp = (context as Activity).window
            .attributes
        lp.alpha = bgAlpha
        (context as Activity).window.attributes = lp
    }


    fun showWaitDialog() {
        showWaitDialog("Loading")
    }

    fun showWaitDialog(message: String) {
        if (TextUtils.isEmpty(message))
            showWaitDialog()
        else
            showWaitDialog(message, true)
    }

    fun showWaitDialog(message: String, cancelable: Boolean) {
        if (mWaitDialog == null) {
            mWaitDialog = WaitProgressDialog(requireActivity())
        }
        mWaitDialog!!.show(message, cancelable)
    }
    fun hideWaitDialog() {
        if (mWaitDialog != null && mWaitDialog!!.isShowing) {
            mWaitDialog!!.dismiss()
        }
    }
}