package com.kelin.multitypeadapterdemo.data;

import android.support.annotation.DrawableRes;

/**
 * 描述 首页的条目。
 * 创建人 kelin
 * 创建时间 2017/6/16  下午1:51
 * 版本 v 1.0.0
 */

public class MainItemEntry {
    @DrawableRes
    private int img;
    private String title;

    public MainItemEntry(int img, String title) {
        this.img = img;
        this.title = title;
    }

    public int getImg() {
        return img;
    }

    public String getTitle() {
        return title;
    }
}
