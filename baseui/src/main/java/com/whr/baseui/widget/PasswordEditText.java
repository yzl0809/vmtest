package com.whr.baseui.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.inputmethod.EditorInfo;


import androidx.annotation.DrawableRes;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.content.ContextCompat;

import com.whr.baseui.R;

import java.util.regex.Matcher;
import java.util.regex.Pattern;



/**
 * 密码编辑框
 */
public class PasswordEditText extends AppCompatEditText {

    private static final String TAG = "DEBUG-WCL: " + PasswordEditText.class.getSimpleName();

    // 模式的显示图标
    @DrawableRes
    private int mShowPwdIcon = R.mipmap.ic_open_eye;

    // 模式的加密图标
    @DrawableRes
    private int mHidePwdIcon = R.mipmap.ic_close_eye;

    private boolean mIsShowPwdIcon; // 是否显示指示器

    private Drawable mDrawableSide; // 显示隐藏指示器

    public PasswordEditText(Context context) {
        this(context, null);
    }

    public PasswordEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initFields(attrs, 0);
    }

    @TargetApi(21)
    public PasswordEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initFields(attrs, defStyleAttr);
    }

    // 初始化布局
    public void initFields(AttributeSet attrs, int defStyleAttr) {
        if (attrs != null) {
            // 获取属性信息
            TypedArray styles = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PasswordEditText, defStyleAttr, 0);
            try {
                // 根据参数, 设置Icon
                mShowPwdIcon = styles.getResourceId(R.styleable.PasswordEditText_pet_iconShow, mShowPwdIcon);
                mHidePwdIcon = styles.getResourceId(R.styleable.PasswordEditText_pet_iconHide, mHidePwdIcon);
            } finally {
                styles.recycle();
            }
        }
        // 密码状态
        setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        filterPwd();
        addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() > 0) {
                    // 有文字时显示指示器
                    showPasswordVisibilityIndicator(true);
                } else {
                    mIsShowPwdIcon = false;
                    restorePasswordIconVisibility(mIsShowPwdIcon);
                    showPasswordVisibilityIndicator(false); // 隐藏指示器
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void filterPwd() {
        setFilters(new InputFilter[]{new InputFilter.LengthFilter(16), new InputFilter() {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {
                if (source.equals(" ")) {
                    return "";
                } else {
                    if (source.length() > 1) {
                        boolean isPwd = true;
                        for (int i = 0; i < source.length(); i++) {
                            char c = source.charAt(i);
                            if (!pwdFilter(String.valueOf(c))) {
                                isPwd = false;
                                break;
                            }
                        }
                        if (!isPwd)
                            return "";
                    } else {
                        if (!pwdFilter(source.toString())) {
                            return "";
                        }
                    }
                }
                return source;
            }
        }});
    }

    public boolean pwdFilter(String str) {
        // 只允许字母、数字
        String regEx = "[0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }

    // 存储状态
    @Override
    public Parcelable onSaveInstanceState() {
        Parcelable state = super.onSaveInstanceState();
        return new PwdSavedState(state, mIsShowPwdIcon);
    }

    // 恢复状态
    @Override
    public void onRestoreInstanceState(Parcelable state) {
        PwdSavedState savedState = (PwdSavedState) state;
        super.onRestoreInstanceState(savedState.getSuperState());
        mIsShowPwdIcon = savedState.isShowingIcon();
        restorePasswordIconVisibility(mIsShowPwdIcon);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mDrawableSide == null) {
            return super.onTouchEvent(event);
        }
        final Rect bounds = mDrawableSide.getBounds();
        final int x = (int) event.getRawX(); // 点击的位置

        int iconX = (int) getTopRightCorner().x;

        // Icon的位置
        int leftIcon = iconX - bounds.width();

        Log.e(TAG, "x: " + x + ", leftIcon: " + leftIcon);

        // 大于Icon的位置, 才能触发点击
        if (x >= leftIcon) {
            togglePasswordIconVisibility(); // 变换状态
            event.setAction(MotionEvent.ACTION_CANCEL);
            return false;
        }
        return super.onTouchEvent(event);
    }

    // 获取上右角的距离
    public PointF getTopRightCorner() {
        float src[] = new float[8];
        float[] dst = new float[]{0, 0, getWidth(), 0, 0, getHeight(), getWidth(), getHeight()};
        getMatrix().mapPoints(src, dst);
        return new PointF(getX() + src[2], getY() + src[3]);
    }

    // 显示密码提示标志
    private void showPasswordVisibilityIndicator(boolean shouldShowIcon) {
        if (shouldShowIcon) {
            Drawable drawable = mIsShowPwdIcon ?
                    ContextCompat.getDrawable(getContext(), R.mipmap.ic_close_eye) :
                    ContextCompat.getDrawable(getContext(), R.mipmap.ic_open_eye);

            // 在最右侧显示图像
            setCompoundDrawablesWithIntrinsicBounds(null, null, drawable, null);

            mDrawableSide = drawable;
        } else {
            // 不显示周边的图像
            setCompoundDrawables(null, null, null, null);
            mDrawableSide = null;
        }
    }

    // 变换状态
    private void togglePasswordIconVisibility() {
        mIsShowPwdIcon = !mIsShowPwdIcon;
        restorePasswordIconVisibility(mIsShowPwdIcon);
        showPasswordVisibilityIndicator(true);
    }

    // 设置密码指示器的状态
    private void restorePasswordIconVisibility(boolean isShowPwd) {
        if (isShowPwd) {
            // 可视密码输入
            setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
        } else {
            // 非可视密码状态
            setInputType(EditorInfo.TYPE_CLASS_TEXT | EditorInfo.TYPE_TEXT_VARIATION_PASSWORD);
        }

        // 移动光标
        setSelection(getText().length());
    }

    // 存储密码状态, 显示Icon的位置
    protected static class PwdSavedState extends BaseSavedState {

        private final boolean mShowingIcon;

        private PwdSavedState(Parcelable superState, boolean showingIcon) {
            super(superState);
            mShowingIcon = showingIcon;
        }

        private PwdSavedState(Parcel in) {
            super(in);
            mShowingIcon = in.readByte() != 0;
        }

        public boolean isShowingIcon() {
            return mShowingIcon;
        }

        @Override
        public void writeToParcel(Parcel destination, int flags) {
            super.writeToParcel(destination, flags);
            destination.writeByte((byte) (mShowingIcon ? 1 : 0));
        }

        public static final Creator<PwdSavedState> CREATOR = new Creator<PwdSavedState>() {
            public PwdSavedState createFromParcel(Parcel in) {
                return new PwdSavedState(in);
            }

            public PwdSavedState[] newArray(int size) {
                return new PwdSavedState[size];
            }
        };
    }
}