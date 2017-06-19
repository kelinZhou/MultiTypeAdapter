package com.kelin.multitypeadapterdemo.data;

import android.support.annotation.DrawableRes;

import com.kelin.multitypeadapterdemo.R;

import java.util.List;

/**
 * 创建人 kelin
 * 创建时间 2017/6/19  下午4:53
 * 版本 v 1.0.0
 */

public class People {
    @DrawableRes
    private int manListImage = R.mipmap.img_man;
    @DrawableRes
    private int womanListImage = R.mipmap.img_woman;
    private List<Person> manList;
    private List<Person> womanList;

    public People(List<Person> manList, List<Person> womanList) {
        this.manList = manList;
        this.womanList = womanList;
    }

    public List<Person> getManList() {
        return manList;
    }

    public List<Person> getWomanList() {
        return womanList;
    }

    @DrawableRes
    public int getManListImage() {
        return manListImage;
    }

    @DrawableRes
    public int getWomanListImage() {
        return womanListImage;
    }
}
