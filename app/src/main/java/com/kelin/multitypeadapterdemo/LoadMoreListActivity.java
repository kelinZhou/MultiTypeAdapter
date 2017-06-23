package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;

import com.kelin.multitypeadapterdemo.data.DataHelper;
import com.kelin.multitypeadapterdemo.data.Person;
import com.kelin.multitypeadapterdemo.holder.CommonImageHolder;
import com.kelin.multitypeadapterdemo.holder.ManHolder;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;

import java.util.List;

import rx.Subscriber;

/**
 * 描述 多条目列表的页面。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:23
 * 版本 v 1.0.0
 */
public class LoadMoreListActivity extends AppCompatActivity {

    public static final int PAGE_SIZE = 10;

    private MultiTypeAdapter mMultiTypeAdapter;
    private int mPage = 1;
    private ItemAdapter<Person> mItemAdapter;

    /**
     * 启动自身，可通过其他Activity调用此方法来启动MultiTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, LoadMoreListActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("分页加载");
        setContentView(R.layout.activity_load_more_list);
        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(recyclerView);
        recyclerView.setAdapter(mMultiTypeAdapter);
        mMultiTypeAdapter.addAdapter(new ItemAdapter<Integer>(CommonImageHolder.class, R.mipmap.img_common_title));
        mItemAdapter = new ItemAdapter<>(ManHolder.class);
        mMultiTypeAdapter.addAdapter(mItemAdapter);
        mMultiTypeAdapter.setLoadMoreView(R.layout.layout_load_more, R.layout.layout_load_more_failed, R.layout.layout_no_more_data, 1, new MultiTypeAdapter.LoadMoreCallback() {
            @Override
            public void onLoadMore() {
                //模拟耗时操作。
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(++mPage, PAGE_SIZE);
                    }
                }, 500);
            }

            @Override
            public void onReloadMore() {
                //模拟耗时操作。
                recyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        loadData(mPage, PAGE_SIZE);
                    }
                }, 500);
            }
        });
        loadData(mPage, PAGE_SIZE);
    }

    private void loadData(int page, int pageSize) {
        DataHelper.getInstance().getPersons(page, pageSize).subscribe(new Subscriber<List<Person>>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
                if (mMultiTypeAdapter.getItemCount() <= 10) {
                    //设置LoadMore不可用，因为如果当前总数据不足以显示一屏幕则会出现一只显示加载中却总也触发不了LoadMore的bug。
                    mMultiTypeAdapter.setLoadMoreUsable(false);
                } else {
                    mMultiTypeAdapter.setLoadMoreUsable(true);
                    mMultiTypeAdapter.setLoadMoreFailed();
                }
            }

            @Override
            public void onNext(List<Person> persons) {
                mItemAdapter.addAll(persons);
                //判断是否是最后一页了，一般情况下如果服务器给的数据是空的或者给的数据量不满足我们请求的pageSize就认为是最后一页了。
                if (persons.isEmpty() || persons.size() < PAGE_SIZE) {
                    mMultiTypeAdapter.setNoMoreData();
                } else {
                    mMultiTypeAdapter.setLoadMoreFinished();
                }
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHelper.getInstance().release();
    }
}