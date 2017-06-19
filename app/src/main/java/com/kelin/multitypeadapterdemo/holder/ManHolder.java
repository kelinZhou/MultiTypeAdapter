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
@ItemLayout(R.layout.item_person_layout)
public class ManHolder extends ItemViewHolder<Person> {

    protected ManHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    public int[] onGetNeedListenerChildViewIds() {
        return new int[]{R.id.btnDelete};
    }

    @Override
    public void onBindData(int position, Person person) {
        setImageResource(R.id.iv_avatar, person.getAvatar());
        setText(R.id.tv_name, String.format("姓名：%s", person.getName()));
        setText(R.id.tv_age, String.format(Locale.CHINA, "年龄：%d 岁", person.getAge()));
        setText(R.id.tv_height, String.format(Locale.CHINA, "身高：%d CM", person.getHeight()));
        setText(R.id.tv_weight, String.format(Locale.CHINA, "体重：%d Kg", person.getWeight()));
        setText(R.id.tv_country, String.format("国家：%s", person.getCountry()));
    }

    @Override
    public boolean areContentsTheSame(Person oldItemData, Person newItemDate) {
        return oldItemData.equals(newItemDate);
    }
}
