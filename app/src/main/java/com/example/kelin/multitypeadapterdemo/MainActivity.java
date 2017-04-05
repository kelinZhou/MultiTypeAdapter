package com.example.kelin.multitypeadapterdemo;

import android.os.Bundle;
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

        View singleType = findViewById(R.id.singleType);
        View multiType = findViewById(R.id.multiType);
        if (singleType != null) {
            singleType.setOnClickListener(this);
        }
        if (multiType != null) {
            multiType.setOnClickListener(this);
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
        }
    }
}
