package com.kelin.recycleradapter;

import android.content.Context;
import android.support.annotation.AttrRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;

/**
 * 描述 用来显示悬浮条目的布局容器。
 * 创建人 kelin
 * 创建时间 2017/5/24  下午1:12
 * 版本 v 1.0.0
 */

public class FloatLayout extends FrameLayout {

    private ViewGroup mFloatLayout;
    private OnSizeChangedListener mOnSizeMeasuredCallback;
    private OnBindEventListener mOnBindEventListener;

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
            mFloatLayout = floatContent;
        }
    }

    public void setOnBindEventListener(OnBindEventListener listener) {
        mOnBindEventListener = listener;
    }

    /**
     * 给悬浮控件绑定事件。
     *
     * @param curHolder   当前悬浮的ViewHolder。
     * @param itemAdapter 当前悬浮的 {@link ItemAdapter}。
     * @param viewHelper  view的帮助类。
     * @param position    当前的列表索引。
     */
    void bindEvent(ItemViewHolder curHolder, ItemAdapter itemAdapter, ViewHelper viewHelper, int position) {
        if (mOnBindEventListener != null) {
            mOnBindEventListener.onBindEvent(curHolder, itemAdapter, viewHelper, position);
        }
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

    public interface OnBindEventListener {
        /**
         * 当需要给悬浮控件绑定事件的时候调用。
         *
         * @param curHolder   当前悬浮的ViewHolder。
         * @param itemAdapter 当前悬浮的 {@link ItemAdapter}。
         * @param viewHelper  view的帮助类。
         */
        void onBindEvent(ItemViewHolder curHolder, ItemAdapter itemAdapter, ViewHelper viewHelper, int position);
    }
}
