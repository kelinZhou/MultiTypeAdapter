package com.kelin.multitypeadapterdemo;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mRecyclerView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindClickListener(R.id.singleType, R.id.multiType, R.id.single_ype_model, R.id.multi_type_model);
    }

    private void bindClickListener(@IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                findViewById(viewId).setOnClickListener(this);
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.singleType:
                SingleTypeListActivity.startAction(this);
                break;

            case R.id.multiType:
                MultiTypeListActivity.startAction(this);
                break;

            case R.id.single_ype_model:
                break;

            case R.id.multi_type_model:
                break;
        }
    }
}
