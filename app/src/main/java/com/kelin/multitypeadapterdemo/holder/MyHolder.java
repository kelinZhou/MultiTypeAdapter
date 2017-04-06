package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

/**
 * 描述 我的Holder
 * 创建人 kelin
 * 创建时间 2016/12/2  下午5:42
 * 版本 v 1.0.0
 */
@ItemLayout(rootLayoutId = R.layout.item_my)
public class MyHolder extends ItemViewHolder<String> {

    private TextView mTvTitle;

    protected MyHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
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
        if (mTvTitle != null) {
            mTvTitle.setText(s);
        }
    }

    @Override
    public boolean areContentsTheSame(String oldItemData, String newItemDate) {
        return oldItemData.equals(newItemDate);
    }
}
