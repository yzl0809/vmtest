package com.whr.baseui.helper


import com.whr.baseui.proxy.IUiCoreProxy

class UiCoreHelper {

    companion object {
        var proxy: IUiCoreProxy? = null

        fun setProxyA(iUiCoreProxy: IUiCoreProxy) {
            proxy = iUiCoreProxy
        }

        fun getProxyA(): IUiCoreProxy {
            if (proxy == null)
                throw NullPointerException("IUiCoreProxy is null, plase use setUiCoreProxy(setUiCoreProxy iUiCoreProxy) method in somewhere") as Throwable
            return proxy as IUiCoreProxy
        }

    }
}
