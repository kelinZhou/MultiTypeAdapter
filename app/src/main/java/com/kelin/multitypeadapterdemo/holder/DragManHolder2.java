package com.kelin.multitypeadapterdemo.holder;

import android.view.View;

import com.kelin.multitypeadapterdemo.R;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.util.Locale;

/**
 * 创建人 kelin
 * 创建时间 2017/6/19  下午5:32
 * 版本 v 1.0.0
 */
@ItemLayout(R.layout.item_person_drag_layout2)
public class DragManHolder2 extends ItemViewHolder<Person> {

    protected DragManHolder2(View itemView) {
        super(itemView);
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
        setText(R.id.tv_name_and_age, String.format(Locale.CHINA, "%s  %d 岁", person.getName(), person.getAge()));
    }

    @Override
    public int getDragHandleViewId() {
        return R.id.iv_handle;
    }
}
