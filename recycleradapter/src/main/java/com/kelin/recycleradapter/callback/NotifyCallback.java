package com.kelin.recycleradapter.callback;

import android.os.Bundle;

/**
 * 描述 当需要刷新数据列表的时候的回调对象。用来描述数据源是否发生了变化。
 * 创建人 kelin
 * 创建时间 2017/3/27  下午6:16
 * 版本 v 1.0.0
 */

public interface NotifyCallback<D> {

    /**
     * 判断两个数据模型是否为同一个数据模型。如果指定模型有唯一标识应当以唯一标识去判断。
     * @param oldItemData 旧的数据模型。
     * @param newItemDate 新的数据模型。
     * @return 如果相同则应当返回 <code color="blue">true</code>,否则应当返回 <code color="blue">false</code>。
     */
    boolean areItemsTheSame(D oldItemData, D newItemDate);

    /**
     * 用来检查 两个item是否含有相同的数据,用返回的信息（true false）来检测当前item的内容是否发生了变化,用这个方法替代equals方法去检查是否相等。
     * 所以你可以根据你的UI去改变它的返回值。这个方法只有当 {@link #areItemsTheSame(D, D)} 返回 <code color="blue">true</code> 的时候才会被调用。
     * @param oldItemData 旧的数据模型。
     * @param newItemDate 新的数据模型。
     * @return 如果数据发生了变化则返回 <code color="blue">true</code>, 否则应当返回 <code color="blue">false</code>。
     *
     * @see #areItemsTheSame(D, D);
     */
    boolean areContentsTheSame(D oldItemData, D newItemDate);

    /**
     * 获取两个对象不同的部分。
     * @param oldItemData 旧的数据模型。
     * @param newItemDate 新的数据模型。
     * @param bundle 比较两个对象不同的部分，将两个对象不同的部分存入该参数中。
     */
    void getChangePayload(D oldItemData, D newItemDate, Bundle bundle);
}
