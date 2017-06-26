package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kelin.multitypeadapterdemo.data.Classs;
import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.ClassHolder;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;
import com.kelin.recycleradapter.FloatLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;

import java.util.List;

import rx.functions.Action1;

/**
 * 描述 多条目列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class FloatListActivity extends BaseActivity {

    private MultiTypeAdapter mMultiTypeAdapter;
    private RecyclerView mRecyclerView;
    private FloatLayout mFloatLayout;

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

        setContentView(R.layout.activity_float_list);

        mFloatLayout = (FloatLayout) findViewById(R.id.fl_float_layout);
        mFloatLayout.setOnBindEventListener(new FloatLayout.OnBindEventListener() {

            @Override
            public void onBindEvent(ItemViewHolder curHolder, final ItemAdapter itemAdapter, ViewHelper viewHelper, final int position) {
                View.OnClickListener l = new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (v.getId() == mFloatLayout.getId()) {
                            Snackbar.make(mRecyclerView, "悬浮条目被点击：position=" + position + "|class=" + ((Classs) mMultiTypeAdapter.getObject(position)).getClassName(), 2000).show();
                        } else if (v.getId() == R.id.tv_show_more) {
                            Snackbar.make(mRecyclerView, "您点击了悬浮条目的更多：position=" + position + "|class=" + ((Classs) mMultiTypeAdapter.getObject(position)).getClassName(), 2000).show();
                        }
                    }
                };
                viewHelper.getRootView().setOnClickListener(l);
                viewHelper.getView(R.id.tv_show_more).setOnClickListener(l);
            }
        });
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(mRecyclerView);
        mMultiTypeAdapter.setFloatLayout(mFloatLayout);
        mRecyclerView.setAdapter(mMultiTypeAdapter);
        loadData();
    }

    private void loadData() {
        DataHelper.getInstance().getClassList().subscribe(new Action1<List<Classs>>() {
            @Override
            public void call(List<Classs> classses) {
                ItemAdapter<Classs> adapter;
                for (final Classs classs : classses) {
                    adapter = new ItemAdapter<>(ClassHolder.class, classs);
                    adapter.setItemEventListener(new ItemAdapter.OnItemEventListener<Classs>() {
                        @Override
                        public void onItemClick(int position, Classs o, int adapterPosition) {
                            Snackbar.make(mRecyclerView, "条目被点击：position=" + position + "|class=" + o.getClassName(), 2000).show();
                        }

                        @Override
                        public void onItemChildClick(int position, Classs o, View view, int adapterPosition) {
                            if (view.getId() == R.id.tv_show_more) {
                                Snackbar.make(mRecyclerView, "您点击了显示更多：position=" + position + "|class=" + o.getClassName(), 2000).show();
                            }
                        }
                    });
                    adapter.setFloatAble(true);
                    mMultiTypeAdapter.addAdapter(adapter);
                    mMultiTypeAdapter.addAdapter(new ItemAdapter<Person>(classs.getStudents(), ManHolder.class));
                }
                mMultiTypeAdapter.notifyRefresh();
            }
        });
    }
}