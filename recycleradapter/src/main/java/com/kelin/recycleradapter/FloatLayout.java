package com.kelin.recycleradapter;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kelin.recycleradapter.interfaces.LayoutItem;

/**
 * 描述 用来显示悬浮条目的布局容器。
 * 创建人 kelin
 * 创建时间 2017/5/24  下午1:12
 * 版本 v 1.0.0
 */

public class FloatLayout extends FrameLayout implements LayoutItem {

    private ViewGroup mFloatLayout;
    private OnSizeChangedListener mOnSizeMeasuredCallback;
    private int mLayoutPosition;

    public FloatLayout(@NonNull Context context) {
        this(context, null);
    }

    public FloatLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FloatLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        if (mOnSizeMeasuredCallback != null) {
            mOnSizeMeasuredCallback.onSizeChanged(w, h);
        }
    }

    public boolean isEmpty() {
        return mFloatLayout == null;
    }

    void setFloatContent(@LayoutRes int floatContentId, OnSizeChangedListener callback) {
        setFloatContent((ViewGroup) LayoutInflater.from(getContext()).inflate(floatContentId, this, false), callback);
    }

    void setFloatContent(ViewGroup floatContent, OnSizeChangedListener callback) {
        if (floatContent != null) {
            mOnSizeMeasuredCallback = callback;
            removeAllViews();
            ViewGroup parent = (ViewGroup) floatContent.getParent();
            if (parent != null) parent.removeView(floatContent);
            addView(floatContent);
            floatContent.requestLayout();  //7.0上必须要调用这段代码，否则view不会被绘制。
            mFloatLayout = floatContent;
        }
    }

    @Override
    public int getLayoutPosition() {
        return mLayoutPosition;
    }

    /**
     * 设置当前悬浮条的所在列表的布局位置。
     * @param layoutPosition 当前的布局位置。
     */
    void setLayoutPosition(int layoutPosition) {
        mLayoutPosition = layoutPosition;
    }

    @Override
    public View getItemView() {
        return mFloatLayout;
    }

    interface OnSizeChangedListener {
        /**
         * 当宽高被测量出来以后的回调。
         *
         * @param width  测量到的宽度。
         * @param height 测量到的高度。
         */
        void onSizeChanged(int width, int height);
    }
}
