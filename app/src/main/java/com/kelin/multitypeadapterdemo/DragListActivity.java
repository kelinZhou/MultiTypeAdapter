package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.People;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.CommonImageHolder;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.multitypeadapterdemo.holder.ManHolder2;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;

import rx.functions.Action1;

/**
 * 描述 可拖拽的列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class DragListActivity extends BaseActivity {

    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, DragListActivity.class));
    }

    private MultiTypeAdapter mMultiTypeAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Drag&Swiped列表");
        setContentView(R.layout.include_common_list_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        mMultiTypeAdapter = new MultiTypeAdapter(recyclerView, 2);  //构建一个最多可将屏幕分为两份的多类型适配器。
        mMultiTypeAdapter.setItemDragEnable(true, true);
        recyclerView.setAdapter(mMultiTypeAdapter);
        loadData();  //加载数据
    }

    private void loadData() {
        //模拟从网络获取数据。
        DataHelper.getInstance().getManAndWoman().subscribe(new Action1<People>() {
            @Override
            public void call(People people) {
                ItemAdapter<Integer> titleAdapter; //用来加载显示头的子适配器。
                ItemAdapter<Person> personAdapter; //用来显示条目的适配器
                //创建女生的头的子适配器。
                titleAdapter = new ItemAdapter<Integer>(CommonImageHolder.class, people.getWomanListImage());
                //创建用来显示女生列表的子适配器。
                personAdapter = new ItemAdapter<Person>(people.getWomanList(), 1, ManHolder2.class);
                //将两个子适配器添加到多类型适配器中。
                mMultiTypeAdapter.addAdapter(titleAdapter, personAdapter);

                //在创建一个男生的头的子适配器。
                titleAdapter = new ItemAdapter<Integer>(CommonImageHolder.class, people.getManListImage());
                //在创建一个用来显示男生列表的子适配器。
                personAdapter = new ItemAdapter<Person>(people.getManList(), 2, ManHolder.class);
                //将两个子适配器添加到多类型适配器中。
                mMultiTypeAdapter.addAdapter(titleAdapter, personAdapter);

                //刷新列表
                mMultiTypeAdapter.notifyRefresh();
            }
        });
    }
}