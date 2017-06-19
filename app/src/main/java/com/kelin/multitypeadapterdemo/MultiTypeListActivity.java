package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

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
 * 描述 多条目列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class MultiTypeListActivity extends AppCompatActivity {

    private MultiTypeAdapter mMultiTypeAdapter;

    /**
     * 启动自身，可通过其他Activity调用此方法来启动MultiTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, MultiTypeListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("多条目列表");
        setContentView(R.layout.activity_multi_type_list);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(recyclerView, 2);
        recyclerView.setAdapter(mMultiTypeAdapter);
        loadData();
    }

    private void loadData() {
        DataHelper.getInstance().getManAndWoman().subscribe(new Action1<People>() {
            @Override
            public void call(People people) {
                ItemAdapter<Integer> titleAdapter = new ItemAdapter<Integer>(CommonImageHolder.class);
                titleAdapter.addItem(0, people.getWomanListImage());
                mMultiTypeAdapter.addAdapter(titleAdapter);
                ItemAdapter<Person> adapter = new ItemAdapter<Person>(people.getWomanList(), 1, ManHolder2.class);
                mMultiTypeAdapter.addAdapter(adapter);

                titleAdapter = new ItemAdapter<Integer>(CommonImageHolder.class);
                titleAdapter.addItem(0, people.getManListImage());
                mMultiTypeAdapter.addAdapter(titleAdapter);
                adapter = new ItemAdapter<Person>(people.getManList(), 2, ManHolder.class);
                mMultiTypeAdapter.addAdapter(adapter);
                mMultiTypeAdapter.notifyRefresh();
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}