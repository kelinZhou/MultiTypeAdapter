package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.recycleradapter.SingleTypeAdapter;

import java.util.List;

import rx.functions.Action1;

/**
 * 描述 单条目列表页面
 * 创建人 kelin
 * 创建时间 2016/12/6  下午5:53
 * 版本 v 1.0.0
 */
public class SingleTypeListActivity extends BaseActivity {

    /**
     * 启动自身，可通过其他Activity调用此方法来启动SingleTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, SingleTypeListActivity.class));
    }


    private SingleTypeAdapter<Person, ManHolder> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("单条目列表");
        setContentView(R.layout.include_common_list_layout);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mAdapter = new SingleTypeAdapter<>(recyclerView, ManHolder.class);
        recyclerView.setAdapter(mAdapter);
        loadData();
    }

    private void loadData() {
        DataHelper.getInstance().getPersons().subscribe(new Action1<List<Person>>() {
            @Override
            public void call(List<Person> persons) {
                mAdapter.setDataList(persons, true);
            }
        });
    }
}