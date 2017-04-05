package com.example.recycleradapter.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述 用来描述子适配器的页眉和页脚的ViewHolder的类。
 * 创建人 kelin
 * 创建时间 2017/4/3  上午12:03
 * 版本 v 1.0.0
 */

public class HeaderFooterViewHolder extends ItemViewHolder {
    /**
     * 表示当前类型为头。
     */
    private static final int HOLDER_TYPE_HEADER = 0x0000_0010;
    /**
     * 表示当前类型为脚。
     */
    private static final int HOLDER_TYPE_FOOTER = 0x0000_0011;

    private int curType;

    public HeaderFooterViewHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    protected void initHolder(View itemView) {
    }

    @Override
    public void onBindData(int position, Object o) {
    }

    @Override
    public boolean areItemsTheSame(Object oldItemData, Object newItemDate) {
        return true;
    }

    @Override
    public boolean areContentsTheSame(Object oldItemData, Object newItemDate) {
        return true;
    }

    public void setHeaderType() {
        curType = HOLDER_TYPE_HEADER;
    }

    public void setFooterType() {
        curType = HOLDER_TYPE_FOOTER;
    }

    public boolean isHeader() {
        return curType == HOLDER_TYPE_HEADER;
    }

    public boolean isFooter() {
        return curType == HOLDER_TYPE_FOOTER;
    }
}
