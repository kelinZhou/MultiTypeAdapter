package com.kelin.multitypeadapterdemo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;

import com.kelin.multitypeadapterdemo.data.MainItemEntry;
import com.kelin.multitypeadapterdemo.holder.MainItemHolder;
import com.kelin.recycleradapter.SingleTypeAdapter;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle("MultiTypeAdapterDemo");
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        List<MainItemEntry> entryList = new ArrayList<>();
        entryList.add(new MainItemEntry(R.mipmap.img_single, "单条目列表"));
        entryList.add(new MainItemEntry(R.mipmap.img_multi, "多条目列表"));
        entryList.add(new MainItemEntry(R.mipmap.img_float, "悬浮条列表"));
        entryList.add(new MainItemEntry(R.mipmap.img_load_more, "分页加载"));
        SingleTypeAdapter<MainItemEntry, MainItemHolder> adapter = new SingleTypeAdapter<>(recyclerView, entryList, MainItemHolder.class);
        recyclerView.setAdapter(adapter);
        adapter.setItemEventListener(new SingleTypeAdapter.OnItemEventListener<MainItemEntry, SingleTypeAdapter<MainItemEntry, MainItemHolder>>() {
            @Override
            public void onItemClick(int position, MainItemEntry mainItemEntry) {
                switch (mainItemEntry.getImg()) {
                    case R.mipmap.img_single:
                        SingleTypeListActivity.startAction(MainActivity.this);
                        break;
                    case R.mipmap.img_multi:
                        MultiTypeListActivity.startAction(MainActivity.this);
                        break;
                    case R.mipmap.img_float:
                        FloatListActivity.startAction(MainActivity.this);
                        break;
                    case R.mipmap.img_load_more:
                        LoadMoreListActivity.startAction(MainActivity.this);
                        break;
                }
            }
        });
    }
}
