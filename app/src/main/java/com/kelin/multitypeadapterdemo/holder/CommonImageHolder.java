package com.kelin.multitypeadapterdemo.holder;

import android.view.View;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

/**
 * 描述 公共的图片条目。
 * 创建人 kelin
 * 创建时间 2017/6/19  下午5:19
 * 版本 v 1.0.0
 */
@ItemLayout(R.layout.item_common_image_layout)
public class CommonImageHolder extends ItemViewHolder<Integer> {

    protected CommonImageHolder(View itemView) {
        super(itemView);
    }

    @Override
    public boolean areContentsTheSame(Integer oldItemData, Integer newItemDate) {
        return oldItemData.equals(newItemDate);
    }

    /**
     * 绑定数据的时候调用。
     *
     * @param position 当前的Item索引。
     * @param integer  当前索引对应的数据对象。
     */
    @Override
    public void onBindData(int position, Integer integer) {
        setImageResource(R.id.iv_title_image, integer);
    }
}
