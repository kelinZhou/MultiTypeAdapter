package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.kelin.multitypeadapterdemo.holder.MyHolder;
import com.kelin.recycleradapter.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 单条目列表页面
 * 创建人 kelin
 * 创建时间 2016/12/6  下午5:53
 * 版本 v 1.0.0
 */
public class SingleTypeListActivity extends AppCompatActivity {

    /**
     * 启动自身，可通过其他Activity调用此方法来启动SingleTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, SingleTypeListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_type_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        List<String> list = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            list.add("测试条目" + i);
        }
        SingleTypeAdapter<String, MyHolder> adapter = new SingleTypeAdapter<>(list, MyHolder.class);
        adapter.setItemEventListener(new SingleTypeAdapter.OnItemEventListener<String, SingleTypeAdapter<String, MyHolder>>() {
            @Override
            public void onItemClick(int position, String s) {
                Toast.makeText(getApplicationContext(), "点击了条目：" + s, Toast.LENGTH_SHORT).show();
                getAdapter().addItem(position + 1, "新增条目" + position);
            }

            @Override
            public void onItemChildClick(int position, String s, View view) {
                getAdapter().removeItem(s);
                Toast.makeText(getApplicationContext(), "删除了条目：" + s, Toast.LENGTH_SHORT).show();
            }
        });

        if (recyclerView != null) {
            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
            recyclerView.setAdapter(adapter);
        }
    }
}