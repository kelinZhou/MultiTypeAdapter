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
import com.kelin.recycleradapter.FloatItemAdapter;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;
import com.kelin.recycleradapter.SuperItemAdapter;

import java.util.List;

import rx.functions.Action1;

/**
 * 描述 多条目列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class FloatListActivity extends BaseActivity {

    /**
     * 启动自身，可通过其他Activity调用此方法来启动MultiTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, FloatListActivity.class));
    }


    private MultiTypeAdapter mMultiTypeAdapter;
    private RecyclerView mRecyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("悬浮条列表");

        setContentView(R.layout.activity_float_list);
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(mRecyclerView);
        mRecyclerView.setAdapter(mMultiTypeAdapter);
        loadData();
    }

    private void loadData() {
        //加载数据。
        DataHelper.getInstance().getClassList().subscribe(new Action1<List<Classs>>() {
            @Override
            public void call(List<Classs> classses) {
                FloatItemAdapter<Classs> adapter;
                for (final Classs classs : classses) {
                    //构建一个用来显示班级的悬浮子Adapter。
                    adapter = new FloatItemAdapter<Classs>(ClassHolder.class, classs);
                    //设置条目事件监听。
                    adapter.setItemEventListener(new SuperItemAdapter.OnItemEventListener<Classs>() {
                        //当条目被点击。
                        @Override
                        public void onItemClick(int position, Classs o, int adapterPosition) {
                            Snackbar.make(mRecyclerView, "条目被点击：position=" + position + "|class=" + o.getClassName(), 2000).show();
                        }
                        //当条目被长按
                        @Override
                        public void onItemLongClick(int position, Classs o, int adapterPosition) {
                            Snackbar.make(mRecyclerView, "条目被长按：position=" + position + "|class=" + o.getClassName(), 2000).show();
                        }
                        //当条目中的子控件被点击
                        @Override
                        public void onItemChildClick(int position, Classs o, View view, int adapterPosition) {
                            if (view.getId() == R.id.tv_show_more) {
                                Snackbar.make(mRecyclerView, "您点击了显示更多：position=" + position + "|class=" + o.getClassName(), 2000).show();
                            }
                        }
                    });
                    //将子Adapter添加到多类型Adapter中。
                    mMultiTypeAdapter.addAdapter(adapter, new ItemAdapter<Person>(classs.getStudents(), ManHolder.class));
                }
                //刷新列表
                mMultiTypeAdapter.notifyRefresh();
            }
        });
    }
}