package com.whr.baseui.common

import android.os.Bundle
import android.view.*
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModelProvider
import com.whr.baseui.mvvm.BaseViewModel
import com.whr.baseui.swipeback.SwipeBackActivity
import com.whr.baseui.swipeback.SwipeBackFragment

/**
 * activity
 */
abstract class BaseDataBindingActivity<Binding : ViewDataBinding> : SwipeBackActivity() {
    companion object {
      const  val FORGET_TOKEN = "forget_token"
        const val SMS_VERIFY_BODY = "SMS_VERIFY_BODY"
    }
    protected lateinit var binding: Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, getLayoutId())
        binding.lifecycleOwner = this
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    open fun beforeSetContentView() {
    }

    abstract fun onCreateCalled(savedInstanceState: Bundle?)


}


/**
 * fragment
 */
abstract class BaseDataBindingFragment<Binding : ViewDataBinding> : SwipeBackFragment() {
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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreatedCalled(view, savedInstanceState)
    }

    @LayoutRes
    abstract fun getLayoutId(): Int

    abstract fun onViewCreatedCalled(view: View, savedInstanceState: Bundle?)

}

