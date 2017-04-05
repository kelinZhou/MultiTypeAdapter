package com.kelin.multitypeadapterdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Toast;

import com.kelin.multitypeadapterdemo.holder.Type1Holder;
import com.kelin.multitypeadapterdemo.holder.Type2Holder;
import com.kelin.recycleradapter.ItemAdapter;
import com.kelin.recycleradapter.MultiTypeAdapter;
import com.kelin.recycleradapter.listener.OnItemEventListener;

import java.util.ArrayList;
import java.util.List;

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
        setContentView(R.layout.activity_multi_type_list);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mMultiTypeAdapter = MultiTypeAdapter.Factory.create(recyclerView, 3);
//        Type1Adapter type1Adapter = new Type1Adapter(getList("A类型条目", 5), 0, 2);
//        multiTypeAdapter.addAdapter(type1Adapter);
//
//        Type2Adapter type2Adapter = new Type2Adapter(getList("B类型条目", 9), 1);
//        multiTypeAdapter.addAdapter(type2Adapter);
//
//        Type1Adapter type1Adapter1 = new Type1Adapter(getList("C类型条目", 5), 2, 1);
//        multiTypeAdapter.addAdapter(type1Adapter1);

//        OnChildClickListener listener = new OnChildClickListener<String>(){
//            @Override
//            public void onChildClick(int position, String s, View view) {
//                Toast.makeText(getApplicationContext(), "条目点击position=" + position + "|s=" + s, Toast.LENGTH_SHORT).show();
//            }
//        };
//
//        OnItemEventListener itemClickListener = new OnItemEventListener<String>() {
//            @Override
//            public void onItemClick(int position, String s) {
//                Toast.makeText(getApplicationContext(), "条目点击position=" + position + "|s=" + s, Toast.LENGTH_SHORT).show();
//            }
//        };

        for (int i = 0; i < 1000; i++) {
            ItemAdapter<String> adapter;
            if (i % 2 == 0) {
                ItemAdapter<String> itemAdapter = new ItemAdapter<>(Type1Holder.class);
                itemAdapter.addItem("A类型条目" + i + "-0");
                itemAdapter.addItem("A类型条目" + i + "-1");
                itemAdapter.addItem("A类型条目" + i + "-2");
                itemAdapter.addItem("A类型条目" + i + "-3");
                itemAdapter.addItem("A类型条目" + i + "-4");
                adapter = itemAdapter;
            } else {
                ItemAdapter<String> itemAdapter = new ItemAdapter<>(Type2Holder.class);
                itemAdapter.addItem("B类型条目" + i + "-0");
                itemAdapter.addItem("B类型条目" + i + "-1");
                adapter = itemAdapter;
            }
            adapter.setItemEventListener(new OnItemEventListener<String>() {

                @Override
                public void onItemHeaderClick(int position) {
                    Toast.makeText(getApplicationContext(), "头ViewHolder被点击！！！position：" + position, Toast.LENGTH_SHORT).show();
                }

                @Override
                public void onItemClick(int position, String s, int adapterPosition) {
                    Toast.makeText(getApplicationContext(), "条目点击position=" + position + "|s=" + s, Toast.LENGTH_SHORT).show();
                    mMultiTypeAdapter.notifyDataSetChanged();
                }

                @Override
                public void onItemChildClick(int position, String s, View view, int adapterPosition) {
                    Toast.makeText(getApplicationContext(), "子控件position=" + position + "|s=" + s, Toast.LENGTH_SHORT).show();
                    ArrayList<String> ls = new ArrayList<String>();
                    ls.add("新增条目1|" + position);
                    ls.add("新增条目2|" + position);
                    ls.add("新增条目3|" + position);
                    mMultiTypeAdapter.getChildAdapterByPosition(position).addAll(1,ls);
                    // TODO: 2017/4/5 优化回调这里考虑是否将Adapter从参数中传入，或者通过其他方式获取。
                }

                @Override
                public void onItemFooterClick(int position, int adapterPosition) {
                    Toast.makeText(getApplicationContext(), "脚ViewHolder被点击！！！position：" + position + "|adapterPosition：" + adapterPosition, Toast.LENGTH_SHORT).show();
                }
            });
            mMultiTypeAdapter.addAdapter(adapter);
        }
        recyclerView.setAdapter(mMultiTypeAdapter);
    }

    private List<String> getList(@NonNull String text, @IntRange(from = 5, to = 100) int size) {
        List<String> list = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            list.add(text + i);
        }
        return list;
    }
}