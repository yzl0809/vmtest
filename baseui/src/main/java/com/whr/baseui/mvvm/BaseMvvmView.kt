package com.whr.baseui.mvvm



interface BaseMvvmView {

    fun showWaitDialog()

    fun showWaitDialog(message: String)

    fun showWaitDialog(message: String, cancelable: Boolean)

    fun hideWaitDialog()

    fun showToast(msg: String?)

    fun showStatusEmptyView(emptyMessage: String)

    fun showStatusErrorView(emptyMessage: String?)

    fun showStatusLoadingView(loadingMessage: String)

    fun showStatusLoadingView(loadingMessage: String, isHasMinTime: Boolean)

    fun hideStatusView()
}
