package com.kelin.multitypeadapterdemo.holder;

import android.support.annotation.LayoutRes;
import android.view.ViewGroup;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.util.Locale;

/**
 * 描述 ${TODO}
 * 创建人 kelin
 * 创建时间 2017/6/19  下午5:32
 * 版本 v 1.0.0
 */
@ItemLayout(R.layout.item_person_layout2)
public class ManHolder2 extends ItemViewHolder<Person> {

    protected ManHolder2(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(parent, itemRootViewId);
    }

    @Override
    public boolean areContentsTheSame(Person oldItemData, Person newItemDate) {
        return oldItemData.equals(newItemDate);
    }

    /**
     * 绑定数据的时候调用。
     *
     * @param position 当前的Item索引。
     * @param person   当前索引对应的数据对象。
     */
    @Override
    public void onBindData(int position, Person person) {
        setImageResource(R.id.iv_avatar, person.getAvatar());
        setText(R.id.tv_name, String.format("姓名：%s", person.getName()));
        setText(R.id.tv_age, String.format(Locale.CHINA, "年龄：%d 岁", person.getAge()));
        setText(R.id.tv_height, String.format(Locale.CHINA, "身高：%d CM", person.getHeight()));
        setText(R.id.tv_weight, String.format(Locale.CHINA, "体重：%d Kg", person.getWeight()));
        setText(R.id.tv_country, String.format("国家：%s", person.getCountry()));
    }
}
