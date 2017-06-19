package com.kelin.multitypeadapterdemo.data;

import java.util.List;

/**
 * 描述 班级
 * 创建人 kelin
 * 创建时间 2017/6/19  下午6:08
 * 版本 v 1.0.0
 */

public class Classs {
    
    private String className;
    private int count;
    private List<Person> students;

    public Classs(String className, int count, List<Person> students) {
        this.className = className;
        this.count = count;
        this.students = students;
    }

    public String getClassName() {
        return className;
    }

    public int getCount() {
        return count;
    }

    public List<Person> getStudents() {
        return students;
    }
}
