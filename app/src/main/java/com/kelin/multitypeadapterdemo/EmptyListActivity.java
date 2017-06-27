package com.kelin.multitypeadapterdemo;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.recycleradapter.SingleTypeAdapter;

import java.util.List;

import rx.functions.Action1;

/**
 * 创建人 kelin
 * 创建时间 2017/6/24  下午10:07
 * 版本 v 1.0.0
 */

public class EmptyListActivity extends BaseActivity {


    public static void startAction(@NonNull Context context) {
        context.startActivity(new Intent(context, EmptyListActivity.class));
    }


    private SingleTypeAdapter<Person, ManHolder> mAdapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EmptyView列表");
        setContentView(R.layout.include_common_list_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new SingleTypeAdapter<>(recyclerView, ManHolder.class);
        mAdapter.setEmptyView(R.layout.layout_common_empty_layout);  //设置列表为空要显示的布局ID。
        recyclerView.setAdapter(mAdapter);
    }

    private void loadData() {
        //加载数据
        DataHelper.getInstance().getPersons().subscribe(new Action1<List<Person>>() {
            @Override
            public void call(List<Person> persons) {
                mAdapter.setDataList(persons);
                mAdapter.notifyRefresh(); //刷新列表
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //添加两个menu用来操作清空列表和加载数据。
        getMenuInflater().inflate(R.menu.menu_load, menu);
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_load:  //如果点击了加载数据则进行加载数据。
                loadData();
                break;
            case R.id.menu_clear:  //如果点击了清空列表则进行清空列表。
                mAdapter.clear();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
