package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

/**
 * 创建人 kelin
 * 创建时间 2016/12/6  下午7:07
 * 版本 v 1.0.0
 */

@ItemLayout(headerLayoutId = R.layout.item_type1_header, rootLayoutId = R.layout.item_type1, footerLayoutId = R.layout.item_type1_footer)
public class Type1Holder extends ItemViewHolder<String> {

    private TextView mTvTitle;

    protected Type1Holder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    public int[] onGetNeedListenerChildViewIds() {
        return new int[]{R.id.btnDelete};
    }

    @Override
    protected void initHolder(View itemView) {
        mTvTitle = getView(R.id.tvTitle);
    }

    @Override
    public void onBindData(int position, String s) {
        mTvTitle.setText(s);
    }

    @Override
    public boolean areContentsTheSame(String oldItemData, String newItemDate) {
        return oldItemData.equals(newItemDate);
    }
}
