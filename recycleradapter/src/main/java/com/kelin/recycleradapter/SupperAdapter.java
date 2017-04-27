package com.kelin.recycleradapter;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.RecyclerView;

import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 {@link android.support.v7.widget.RecyclerView.Adapter} 的基类。
 * 创建人 kelin
 * 创建时间 2017/3/28  下午12:42
 * 版本 v 1.0.0
 */

abstract class SupperAdapter<D, VH extends ItemViewHolder<D>> extends RecyclerView.Adapter<VH> {


    /**
     * 当前页面的数据集。
     */
    private List<D> mDataList;
    /**
     * 用来存页面数据集的副本。
     */
    private List<D> mTempList;
    /**
     * 当需要刷新列表时，用来比较两此数据不同的回调。
     */
    private DiffUtil.Callback mDiffUtilCallback;

    SupperAdapter() {

        mDiffUtilCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mTempList.size();
            }

            @Override
            public int getNewListSize() {
                return mDataList.size();
            }

            // 判断是否是同一个 item
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                return oldObject == newObject || SupperAdapter.this.areItemsTheSame(oldObject, newObject);
            }

            // 如果是同一个 item 判断内容是否相同
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                return oldObject == newObject || SupperAdapter.this.areContentsTheSame(oldObject, newObject);
            }

            @Nullable
            @Override
            public Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                if (oldObject == newObject) return null;
                Bundle bundle = new Bundle();
                SupperAdapter.this.getChangePayload(oldObject, newObject, bundle);
                return bundle.size() == 0 ? null : bundle;
            }
        };
    }

    @Override
    public final void onBindViewHolder(VH holder, int position) {}

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        holder.onBindPartData(position, getObject(position), payloads);
    }

    /**
     * 判断两个位置的Item是否相同。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @return 相同返回true，不同返回false。
     */
    protected abstract boolean areItemsTheSame(D oldItemData, D newItemData);

    /**
     * 判断两个位置的Item的内容是否相同。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @return 相同返回true，不同返回false。
     */
    protected abstract boolean areContentsTheSame(D oldItemData, D newItemData);

    /**
     * 获取两个位置的Item的内容不同之处。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @param bundle 将不同的内容存放到该参数中。
     */
    protected abstract void getChangePayload(D oldItemData, D newItemData, Bundle bundle);
    /**
     * 刷新RecyclerView。
     */
    public void notifyRefresh() {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mDiffUtilCallback);
        diffResult.dispatchUpdatesTo(this);
        // 通知刷新了之后，要更新副本数据到最新
        mTempList.clear();
        mTempList.addAll(mDataList);
    }

    /**
     * 设置数据。
     *
     * @param list 数据集合。
     */
    public void setDataList(List<D> list) {
        mDataList = list != null ? list : new ArrayList<D>();
        mTempList = new ArrayList<>(mDataList);
    }

    /**
     * 设置数据。
     *
     * @param list 数据集合。
     */
    void addDataList(List<D> list) {
        getDataList().addAll(list);
        mTempList.addAll(list);
    }

    /**
     * 设置数据。
     *
     * @param d 数据集合。
     */
    void addData(D d) {
        getDataList().add(d);
        mTempList.add(d);
    }

    /**
     * 获取当前的数据集合。
     */
    List<D> getDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
            mTempList = new ArrayList<>();
        }
        return mDataList;
    }

    /**
     * 判断当前列表是否为空列表。
     *
     * @return <code color="blue">true</code> 表示为空列表，<code color="blue">false</code> 表示为非空列表。
     */
    public boolean isEmptyList() {
        return mDataList == null || mDataList.isEmpty();
    }

    /**
     * 获取指定位置的对象。
     *
     * @param position 要获取对象对应的条目索引。
     * @return 返回 {@link D} 对象。
     */
    public D getObject(int position) {
        List<D> dataList = getDataList();
        if (dataList.size() > position && position >= 0) {
            return dataList.get(position);
        }
        return null;
    }

    /**
     * 获取指定位置的对象。
     *
     * @param position 要获取对象对应的条目索引。
     * @return 返回 {@link D} 对象。
     */
    D getOldObject(int position) {
        if (mTempList.size() > position && position >= 0) {
            return mTempList.get(position);
        }
        return null;
    }
}
