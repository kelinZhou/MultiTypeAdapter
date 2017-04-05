package com.example.recycleradapter.listener;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.recycleradapter.ItemAdapter;
import com.example.recycleradapter.MultiTypeAdapter;
import com.example.recycleradapter.SingleTypeAdapter;

/**
 * 描述 {@link RecyclerView} 的条目点击事件监听。
 * 创建人 kelin
 * 创建时间 2017/1/19  下午12:21
 * 版本 v 1.0.0
 */

public abstract class OnItemEventListener<D> {

    /**
     * 当条目被点击的时候调用。
     *
     * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
     * @param d        被点击的条目的条目信息对象。
     * @param adapterPosition 当前被点击的条目在 {@link android.support.v7.widget.RecyclerView.Adapter} 中的索引,
     *                        一般情况下该参数的值会和 position 参数的值一致。只有当使用 {@link ItemAdapter}
     *                        作为 {@link MultiTypeAdapter} 的子条目并使用该监听时这个值才有意义，
     *                        这时该参数的值将会与 position 参数的值不同，该参数的值将表示当前点击的条目在当前子 Adapter 中的位置。
     *
     */
    public abstract void onItemClick(int position, D d, int adapterPosition);

    /**
     * 当条目中的子控件被点击的时候调用。
     *
     * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
     * @param d        被点击的条目的条目信息对象。
     * @param view 被点击的{@link View}。
     * @param adapterPosition 当前被点击的条目在 {@link android.support.v7.widget.RecyclerView.Adapter} 中的索引,
     *                        一般情况下该参数的值会和 position 参数的值一致。只有当使用 {@link ItemAdapter}
     *                        作为 {@link MultiTypeAdapter} 的子条目并使用该监听时这个值才有意义，
     *                        这时该参数的值将会与 position 参数的值不同，该参数的值将表示当前点击的条目在当前子 Adapter 中的位置。
     */
    public abstract void onItemChildClick(int position, D d, View view, int adapterPosition);

    /**
     * 当头ViewHolder被点击的时候调用。
     * <P>如果当前Item事件监听是设置给 {@link SingleTypeAdapter} 的，
     * 那么重写该方法是没有意义的，因为永远不会被调用。
     * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
     */
    public void onItemHeaderClick(int position) {}

    /**
     * 当脚ViewHolder被点击的时候调用。
     * <P>如果当前Item事件监听是设置给 {@link SingleTypeAdapter} 的，
     * 那么重写该方法是没有意义的，因为永远不会被调用。
     * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
     * @param adapterPosition 当前点击的条目在当前子 Adapter 中的位置。
     */
    public void onItemFooterClick(int position, int adapterPosition) {}
}
