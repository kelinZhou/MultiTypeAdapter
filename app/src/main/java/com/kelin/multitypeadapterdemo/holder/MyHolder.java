package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.util.Locale;

/**
 * 描述 我的Holder
 * 创建人 kelin
 * 创建时间 2016/12/2  下午5:42
 * 版本 v 1.0.0
 */
@ItemLayout(rootLayoutId = R.layout.item_my)
public class MyHolder extends ItemViewHolder<Person> {

    protected MyHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    public int[] onGetNeedListenerChildViewIds() {
        return new int[]{R.id.btnDelete};
    }

    @Override
    public void onBindData(int position, Person s) {
        setImageResource(R.id.iv_avatar, s.getAvatar());
        setText(R.id.tv_name, String.format("姓名：%s", s.getName()));
        setText(R.id.tv_age, String.format(Locale.CHINA, "年龄：%d 岁", s.getAge()));
        setText(R.id.tv_height, String.format(Locale.CHINA, "身高：%d CM", s.getHeight()));
        setText(R.id.tv_weight, String.format(Locale.CHINA, "体重：%d Kg", s.getWeight()));
        setText(R.id.tv_country, String.format("国家：%s", s.getCountry()));
    }

    @Override
    public boolean areContentsTheSame(Person oldItemData, Person newItemDate) {
        return oldItemData.equals(newItemDate);
    }
}
