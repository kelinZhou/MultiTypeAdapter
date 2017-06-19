package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.multitypeadapterdemo.data.Classs;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;

import java.util.Locale;

/**
 * 描述 班级的头。
 * 创建人 kelin
 * 创建时间 2017/6/19  下午6:53
 * 版本 v 1.0.0
 */
@ItemLayout(R.layout.item_class_title_layout)
public class ClassHolder extends ItemViewHolder<Classs> {

    protected ClassHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    /**
     * 绑定数据的时候调用。
     *
     * @param position 当前的Item索引。
     * @param classs   当前索引对应的数据对象。
     */
    @Override
    public void onBindData(int position, Classs classs) {
        setText(R.id.tv_class_name, classs.getClassName());
        setText(R.id.tv_count, String.format(Locale.CHINA, "%d 人", classs.getCount()));
    }

    @Override
    public void onBindFloatLayoutData(ViewHelper viewHelper, int position, Classs classs) {
        viewHelper.setText(R.id.tv_class_name, classs.getClassName());
        viewHelper.setText(R.id.tv_count, String.format(Locale.CHINA, "%d 人", classs.getCount()));
    }
}
