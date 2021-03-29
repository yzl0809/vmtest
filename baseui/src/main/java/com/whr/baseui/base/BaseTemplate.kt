package com.whr.baseui.base

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.whr.baseui.common.ext.getGenericClass
import com.whr.baseui.mvvm.BaseViewModel
import com.whr.baseui.utils.TUtils

/**
 * MVVM activity基类
 */

abstract class BaseMvvmActivity<VM : BaseViewModel> :
    BaseActivity() {
    lateinit var mViewModel: VM
    private var providerVMClass: Class<VM>? = null
    override fun initViews(savedInstanceState: Bundle?) {
        initMVVM()
    }
    private fun initMVVM() {
        providerVMClass = TUtils.getT<VM>(this, 1).javaClass
        providerVMClass?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }

}

/**
 * MVVM fragment基类
 */
abstract class BaseMvvmFragment<VM : BaseViewModel> :
    BaseFragment() {
    lateinit var mViewModel: VM
    private var providerVMClass: Class<VM>? = null
    override fun initViews(savedInstanceState: Bundle?) {
        initMVVM()
    }
    private fun initMVVM() {
        providerVMClass = TUtils.getT<VM>(this, 1).javaClass
        providerVMClass?.let {
            mViewModel = ViewModelProviders.of(this).get(it)
            mViewModel.let(lifecycle::addObserver)
        }
    }
}

/**
 * dataBingDing activity基类
 */
abstract class BaseDataBindingActivity<Binding : ViewDataBinding,VM:BaseViewModel> : BaseMvvmActivity<VM>() {
    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
        offsetView()
    }

    @LayoutRes
    abstract fun getLayoutId(): Int



    open fun offsetView() {
        if (isImmerse() && autoOffsetView()) {
            offsetStatusBar(binding.root)
        }
    }

}

/**
 * dataBingDing fragment基类
 */
abstract class BaseDataBindingFragment<Binding : ViewDataBinding,VM:BaseViewModel> : BaseMvvmFragment<VM>() {
    protected lateinit var binding: Binding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, getLayoutId(), container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    @LayoutRes
    abstract fun getLayoutId(): Int


}


