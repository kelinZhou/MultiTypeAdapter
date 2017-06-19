package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;

/**
 * 创建人 kelin
 * 创建时间 2016/12/6  下午7:07
 * 版本 v 1.0.0
 */

@ItemLayout(R.layout.item_type2)
public class Type2Holder extends ItemViewHolder<String> {

    protected Type2Holder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    public int[] onGetNeedListenerChildViewIds() {
        return new int[]{R.id.btnDelete};
    }

    @Override
    public void onBindData(int position, String s) {
        setText(R.id.tvTitle, s);
    }

    @Override
    public void onBindFloatLayoutData(ViewHelper viewHelper, int position, String s) {
        viewHelper.setText(R.id.tvTitle, s);
    }

    @Override
    public boolean areContentsTheSame(String oldItemData, String newItemDate) {
        return oldItemData.equals(newItemDate);
    }

    @Override
    public boolean areItemsTheSame(String oldItemData, String newItemDate) {
        boolean b = super.areItemsTheSame(oldItemData, newItemDate);
        return b;
    }
}
