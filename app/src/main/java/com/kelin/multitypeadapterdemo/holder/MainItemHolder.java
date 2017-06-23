package com.kelin.multitypeadapterdemo.holder;

import android.view.View;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.multitypeadapterdemo.data.MainItemEntry;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

/**
 * 描述 首页条目的ViewHolder。
 * 创建人 kelin
 * 创建时间 2017/6/16  下午1:54
 * 版本 v 1.0.0
 */
@ItemLayout(R.layout.item_main_item_layout)
public class MainItemHolder extends ItemViewHolder<MainItemEntry> {

    protected MainItemHolder(View itemView) {
        super(itemView);
    }

    @Override
    public boolean areContentsTheSame(MainItemEntry oldItemData, MainItemEntry newItemDate) {
        return oldItemData.equals(newItemDate);
    }

    /**
     * 绑定数据的时候调用。
     *
     * @param position 当前的Item索引。
     * @param d        当前索引对应的数据对象。
     */
    @Override
    public void onBindData(int position, MainItemEntry d) {
        setImageResource(R.id.iv_icon, d.getImg());
        setText(R.id.tv_title, d.getTitle());
    }
}
