package com.kelin.recycleradapter.interfaces;

import java.util.List;

/**
 * 描述 对象池。
 * 创建人 kelin
 * 创建时间 2017/6/15  下午11:10
 * 版本 v 1.0.0
 */

public interface Pool<T> {

    /**
     * 通过位置获取对象。
     * @param position 要获取的对象的位置。
     */
    T acquire(int position);

    /**
     * 获取全部对象。
     */
    List<T> acquireAll();

    /**
     * 发布一个对象到池子中。
     * @param instance 要发布的对象。
     */
    void release(T instance);

    /**
     * 获取对象的个数。
     */
    int size();

    /**
     * 获取一个对象的位置。
     * @param instance 要获取位置的对象。
     */
    int indexOf(T instance);

    /**
     * 移除一个对象。
     * @param instance 要移除的对象。
     */
    void remove(T instance);
}
