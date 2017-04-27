package com.kelin.recycleradapter.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述 加载更多时的ViewHolder。
 * 创建人 kelin
 * 创建时间 2017/4/26  下午5:51
 * 版本 v 1.0.0
 */
public class LoadMoreViewHolder extends ItemViewHolder<Object> {

    public LoadMoreViewHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    protected void initHolder(View itemView) {}

    @Override
    public void onBindData(int position, Object o) {}

    @Override
    public boolean areItemsTheSame(Object oldItemData, Object newItemDate) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(Object oldItemData, Object newItemDate) {
        return true;
    }
}
