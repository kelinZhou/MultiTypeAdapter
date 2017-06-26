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

    private SingleTypeAdapter<Person, ManHolder> mAdapter;

    public static void startAction(@NonNull Context context) {
        context.startActivity(new Intent(context, EmptyListActivity.class));
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("EmptyView列表");
        setContentView(R.layout.include_common_list_layout);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new SingleTypeAdapter<>(recyclerView, ManHolder.class);
        mAdapter.setEmptyView(R.layout.layout_common_empty_layout);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_load, menu);
        getMenuInflater().inflate(R.menu.menu_clear, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_load:
                loadData();
                break;
            case R.id.menu_clear:
                mAdapter.clear();
        }
        return super.onOptionsItemSelected(item);
    }

    private void loadData() {
        DataHelper.getInstance().getPersons().subscribe(new Action1<List<Person>>() {
            @Override
            public void call(List<Person> persons) {
                mAdapter.setDataList(persons);
//                mAdapter.notifyRefresh();  这个方法无效，有待检查。
                mAdapter.notifyDataSetChanged();
            }
        });
    }
}
