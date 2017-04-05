package com.kelin.multitypeadapterdemo;

import android.app.Application;

import com.squareup.leakcanary.LeakCanary;

/**
 * 创建人 kelin
 * 创建时间 2017/3/31  下午4:41
 * 版本 v 1.0.0
 */

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        LeakCanary.install(this);
    }
}
