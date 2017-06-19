package com.kelin.multitypeadapterdemo.data;

import android.text.TextUtils;

/**
 * 创建人 kelin
 * 创建时间 2017/6/19  上午9:47
 * 版本 v 1.0.0
 */

public class Person {
    private int avatar;
    private String name;
    private String country;
    private int age;
    private int height;
    private int weight;

    public Person(int avatar, String name, String country, int age, int height, int weight) {
        this.avatar = avatar;
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.weight = weight;
    }

    public int getAvatar() {
        return avatar;
    }

    public String getName() {
        return name;
    }

    public String getCountry() {
        return country;
    }

    public int getAge() {
        return age;
    }

    public int getHeight() {
        return height;
    }

    public int getWeight() {
        return weight;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Person) {
            Person p = (Person) o;
            return TextUtils.equals(p.name, name) && TextUtils.equals(p.country, country) && p.age == age && p.height == height;
        } else {
            return false;
        }
    }
}
