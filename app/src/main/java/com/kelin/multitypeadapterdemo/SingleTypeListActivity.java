package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;
import com.kelin.multitypeadapterdemo.holder.MyHolder;
import com.kelin.recycleradapter.MultiTypeAdapter;
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

    private int mPage;
    private SingleTypeAdapter<String, MyHolder> mAdapter;

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

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        //        ((SimpleItemAnimator)recyclerView.getItemAnimator()).setSupportsChangeAnimations(false);
        mPage = 0;
        List<String> list = loadData();
        mAdapter = new SingleTypeAdapter<>(recyclerView, list, MyHolder.class);

        final Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<String> data = loadData();
                mAdapter.addAll(data);
                mAdapter.setLoadMoreFinished();
                if (mPage == 3) {
                    mAdapter.setNoMoreData();
                }
            }
        };

        mAdapter.setLoadMoreView(R.layout.layout_load_more, R.layout.layout_load_more_failed, R.layout.layout_no_more_data, 1, new MultiTypeAdapter.LoadMoreCallback() {
            @Override
            public void onLoadMore() {
                recyclerView.postDelayed(runnable, 500);
            }
        });
        mAdapter.setItemEventListener(new SingleTypeAdapter.OnItemEventListener<String, SingleTypeAdapter<String, MyHolder>>() {
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
        recyclerView.setAdapter(mAdapter);
    }

    @NonNull
    private List<String> loadData() {
        List<String> list = new ArrayList<>();
        for (int i = mPage * 30; i < (mPage + 1) * 30; i++) {
            list.add("测试条目" + i);
        }
        mPage++;
        return list;
    }
}