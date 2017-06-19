package com.kelin.multitypeadapterdemo.data;

import android.support.annotation.IntDef;
import android.text.TextUtils;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import static com.kelin.multitypeadapterdemo.data.Person.Sex.MAN;
import static com.kelin.multitypeadapterdemo.data.Person.Sex.UNKNOWN;
import static com.kelin.multitypeadapterdemo.data.Person.Sex.WOMAN;

/**
 * 创建人 kelin
 * 创建时间 2017/6/19  上午9:47
 * 版本 v 1.0.0
 */

public class Person {

    @IntDef({MAN, WOMAN, UNKNOWN})
    @Retention(RetentionPolicy.SOURCE)
    public @interface Sex {
        /**
         * 男性。
         */
        int MAN = 0X0000_0001;
        /**
         * 女性。
         */
        int WOMAN = 0X0000_0002;
        /**
         * 未知。
         */
        int UNKNOWN = 0X0000_0003;
    }

    private int avatar;
    private String name;
    private String country;
    @Sex private int sex;
    private int age;
    private int height;
    private int weight;

    public Person(int avatar, String name, String country, int age, int height, int weight, @Sex int sex) {
        this.avatar = avatar;
        this.name = name;
        this.country = country;
        this.age = age;
        this.height = height;
        this.weight = weight;
        this.sex = sex;
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

    @Sex
    public int getSex() {
        return sex;
    }

    @Override
    public boolean equals(Object o) {
        if (o != null && o instanceof Person) {
            Person p = (Person) o;
            return TextUtils.equals(p.name, name) && TextUtils.equals(p.country, country) && p.age == age && p.height == height && p.sex == sex;
        } else {
            return false;
        }
    }
}
