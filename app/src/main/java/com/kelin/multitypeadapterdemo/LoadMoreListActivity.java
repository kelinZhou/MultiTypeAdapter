package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

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
public class LoadMoreListActivity extends BaseActivity {

    /**
     * 启动自身，可通过其他Activity调用此方法来启动MultiTypeListActivity。
     *
     * @param activityContext 需要一个上下文Activity作为参数，一般把Activity.this作为参数即可。
     */
    public static void startAction(Activity activityContext) {
        activityContext.startActivity(new Intent(activityContext, LoadMoreListActivity.class));
    }

    /**
     * 定义每页的数量。
     */
    public static final int PAGE_SIZE = 10;
    /**
     * 记录当前需要加载的页数。
     */
    private int mPage = 1;
    private MultiTypeAdapter mMultiTypeAdapter;
    private ItemAdapter<Person> mItemAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("分页加载");
        setContentView(R.layout.include_common_list_layout);

        final RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = new MultiTypeAdapter(recyclerView);
        recyclerView.setAdapter(mMultiTypeAdapter);
        //添加一个头条目。
        mMultiTypeAdapter.addAdapter(new ItemAdapter<Integer>(CommonImageHolder.class, R.mipmap.img_common_title));
        //创建用来显示内容的子条目。
        mItemAdapter = new ItemAdapter<>(ManHolder.class);
        //将子条目添加到多类型Adapter中。
        mMultiTypeAdapter.addAdapter(mItemAdapter);
        //设置加载更多的布局，有重载方法。这里用的是五个参数的，前三个分别是 加载中、点击重试和没有更多数据时显示的布局文件。
        //第四个参数是偏移值，就是在最后一个条目之前的第几个条目被显示时触发加载更多，最后一个是加载更多的回调。
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
        //加载数据
        DataHelper.getInstance().getPersons(page, pageSize).subscribe(new Subscriber<List<Person>>() {
            @Override
            public void onCompleted() {}

            @Override
            public void onError(Throwable e) {
                if (mMultiTypeAdapter.getItemCount() <= 10) {
                    //设置LoadMore不可用，因为如果当前总数据不足以显示一屏幕则会出现一只显示加载中却总也触发不了LoadMore的bug。
                    mMultiTypeAdapter.setLoadMoreUsable(false);
                } else {
                    //设置加载更多可用。
                    mMultiTypeAdapter.setLoadMoreUsable(true);
                    //设置加载更多完成。
                    mMultiTypeAdapter.setLoadMoreFailed();
                }
            }

            @Override
            public void onNext(List<Person> persons) {
                //将获取到的数据添加到子适配其中，这个addAll也是有重载方法的，这里调用的是一个参数的方法，这个方法默认会在添加数据后刷新列表。
                mItemAdapter.addAll(persons);
                //判断是否是最后一页了，一般情况下如果服务器给的数据是空的或者给的数据量不满足我们请求的pageSize就认为是最后一页了。
                if (persons.isEmpty() || persons.size() < PAGE_SIZE) {
                    mMultiTypeAdapter.setNoMoreData(); //设置没有更多数据了。
                } else {
                    mMultiTypeAdapter.setLoadMoreFinished();  //设置加载更多完成。
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataHelper.getInstance().release();
    }
}