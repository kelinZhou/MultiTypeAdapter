package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.kelin.multitypeadapterdemo.data.Classs;
import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.ClassHolder;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;
import com.kelin.recycleradapter.view.FloatLayout;

import java.util.List;

import rx.functions.Action1;

/**
 * 描述 多条目列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class FloatListActivity extends AppCompatActivity {

    private MultiTypeAdapter mMultiTypeAdapter;

    /**
     * 启动自身，可通过其他Activity调用此方法来启动MultiTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, FloatListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("悬浮条列表");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setContentView(R.layout.activity_float_list);

        FloatLayout floatLayout = (FloatLayout) findViewById(R.id.fl_float_layout);
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(recyclerView);
        mMultiTypeAdapter.setFloatLayout(floatLayout);
        recyclerView.setAdapter(mMultiTypeAdapter);
        loadData();
    }

    private void loadData() {
        DataHelper.getInstance().getClassList().subscribe(new Action1<List<Classs>>() {
            @Override
            public void call(List<Classs> classses) {
                ItemAdapter adapter;
                for (Classs classs : classses) {
                    adapter = new ItemAdapter<Classs>(ClassHolder.class, classs);
                    adapter.setFloatAble(true);
                    mMultiTypeAdapter.addAdapter(adapter);
                    mMultiTypeAdapter.addAdapter(new ItemAdapter<Person>(classs.getStudents(), ManHolder.class));
                }
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