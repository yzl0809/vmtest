package com.whr.baseui.base

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.IBinder
import android.text.TextUtils
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.gyf.immersionbar.ktx.immersionBar
import com.whr.baseui.widget.WaitProgressDialog

/**
 * 统一 Activity
 * 支持沉浸式模式
 */
open abstract  class MyBaseActivity : AppCompatActivity() {

    protected lateinit var context: Context
    private var mWaitDialog: WaitProgressDialog? = null
    @get:LayoutRes
    abstract val layoutId: Int
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutId)
        context = this
        if (isImmerse()) {
            immerse()
        }
        initViews()
        if (null != intent) handleIntent(intent)
    }
    abstract fun initViews()
    private fun immerse() {
        immersionBar {
            // 透明状态栏
            transparentStatusBar()
            // 状态栏字体颜色
            statusBarDarkFont(statusBarDarkFont(), 0.2f)
            // 导航栏图标颜色
            navigationBarDarkIcon(navBarDarkIcon(), 0.2f)
        }
    }
    /**
     * 获取Intent
     *
     * @param intent
     */
    open fun handleIntent(intent: Intent) {}

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
            mWaitDialog = WaitProgressDialog(this)
        }
        mWaitDialog!!.show(message, cancelable)
    }
     fun hideWaitDialog() {
        if (mWaitDialog != null && mWaitDialog!!.isShowing) {
            mWaitDialog!!.dismiss()
        }
    }



     fun showToast(msg: String?) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }


     fun showToast(strId: Int) {
        Toast.makeText(this, getString(strId), Toast.LENGTH_SHORT).show()
    }

     fun showToast(strId1: Int, str: Int) {
        Toast.makeText(this, getString(strId1) + getString(str), Toast.LENGTH_SHORT).show()
    }

     fun showToast(strId1: Int, strin2: String?) {
        Toast.makeText(this, getString(strId1) + strin2, Toast.LENGTH_SHORT).show()
    }


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

    /**
     * 偏移导航栏高度
     */
    fun offsetNavBar(view: View) {
        view.setPadding(
            view.paddingStart,
            view.paddingTop,
            view.paddingEnd,
            view.paddingBottom + ImmersionBar.getNavigationBarHeight(this)
        )
    }

    /**
     * 默认开启沉浸式模式，可以复写方法关闭
     */
    open fun isImmerse() = true

    /**
     * 默认偏移根布局
     */
    open fun autoOffsetView() = true

    /**
     * 状态栏字体黑色
     */
    open fun statusBarDarkFont() = true

    /**
     * 导航栏图标黑色
     */
    open fun navBarDarkIcon() = false

    // 判定是否需要隐藏
    fun isHideInput(v: View?, ev: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.getHeight()
            val right = (left + v.getWidth())
            if (ev.getX() > left && ev.getX() < right && ev.getY() > top && ev.getY() < bottom) {
                return false;
            } else {
                return true;
            }
        }
        return true
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        if (ev.action == MotionEvent.ACTION_UP) {
            if (isDoubleClick) {
                return true
            }
        }
        //点击软键盘外部，软键盘消失
        if (ev.action == MotionEvent.ACTION_DOWN) {
            // 获得当前得到焦点的View，一般情况下就是EditText（特殊情况就是轨迹求或者实体案件会移动焦点）
            val v = currentFocus
            if (isShouldHideInput(v, ev)) {
                HideSoftInput(v!!.windowToken)
            }
        }
        return super.dispatchTouchEvent(ev)
    }

    // 隐藏软键盘
    open fun HideSoftInput(token: IBinder?) {
        if (token != null) {
            val manager =
                getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            manager.hideSoftInputFromWindow(
                token,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }
    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时没必要隐藏
     *
     * @param v
     * @param event
     * @return
     */
    open fun isShouldHideInput(v: View?, event: MotionEvent): Boolean {
        if (v != null && v is EditText) {
            val l = intArrayOf(0, 0)
            v.getLocationInWindow(l)
            val left = l[0]
            val top = l[1]
            val bottom = top + v.height
            val right = left + v.width
            return event.x <= left || event.x >= right || event.y <= top || event.y >= bottom
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditView上，和用户用轨迹球选择其他的焦点
        return false
    }
    open fun hideSystemSoftInput() {
        val view = window.peekDecorView()
        if (view != null && view.windowToken != null) {
            val im = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            im.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        }
    }


    fun releaseFocus(event: MotionEvent) {
        // 点击其他地方失去焦点
        val view = currentFocus
        if (view != null && view is EditText) {
            val location = IntArray(2)
            view.getLocationOnScreen(location)
            val x = location[0]
            val y = location[1]
            if (event.rawX < x || event.rawX > x + view.getWidth() || event.rawY < y || event.rawY > y + view.getHeight()
            ) {
                view.clearFocus()
                try {
                    val root = view.getParent() as View
                    if (root != null) {
                        root.isFocusable = true
                        root.isFocusableInTouchMode = true
                        root.requestFocus()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    /**
     * 返回页面布局
     *
     * @return
     */

    /**
     * 是否支持双击，默认为不支持
     */
    private val mDoubleClickEnable = false

    /**
     * 上一次点击的时间戳
     */
    private var mLastClickTime: Long = 0

    /**
     * 被判断为重复点击的时间间隔
     */
    private val MIN_CLICK_DELAY_TIME: Long = 200

    /**
     * 检测双击
     */
    val isDoubleClick: Boolean
        get() {
            if (mDoubleClickEnable) return false
            val time = System.currentTimeMillis()
            if (time - mLastClickTime > MIN_CLICK_DELAY_TIME) {
                mLastClickTime = time
                return false
            } else {
                return true
            }
        }


}