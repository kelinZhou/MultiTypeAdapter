package com.example.recycleradapter.adapter;

import android.database.Observable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.example.recycleradapter.adapter.holder.HeaderFooterViewHolder;
import com.example.recycleradapter.adapter.holder.ItemLayout;
import com.example.recycleradapter.adapter.holder.ItemViewHolder;
import com.example.recycleradapter.adapter.listener.OnItemEventListener;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 描述 {@link RecyclerView} 的适配器的基类。
 * 创建人 kelin
 * 创建时间 2016/11/28  上午10:09
 * 版本 v 1.0.0
 */

public class SingleTypeAdapter<D, H extends ItemViewHolder<D>> extends RecyclerAdapter<D, H> {

    /**
     * 用来存放ViewHolder的字节码对象。
     */
    Class<? extends H> mHolderClass;
    /**
     * 当前的条目点击监听对象。
     */
    private OnItemEventListener<D> mItemEventListener;
    /**
     * 当前适配器中的ViewHolder对象。
     */
    private H mViewHolder;
    /**
     * 适配器数据的观察者对象。
     */
    private AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable(this);

    /**
     * 设置条目的事件监听。
     *
     * @param listener {@link OnItemEventListener} 对象。
     */
    public void setItemEventListener(OnItemEventListener<D> listener) {
        mItemEventListener = listener;
    }

    /**
     * 构建一个空的适配器
     */
    public SingleTypeAdapter(Class<H> holderClass) {
        this(null, holderClass);
    }

    /**
     * 构建一个拥有初始数据模型的适配器。
     *
     * @param list 数据模型的集合。
     */
    public SingleTypeAdapter(List<D> list, Class<? extends H> holderClass) {
        if (holderClass == null) {
            throw new RuntimeException("you mast set holderClass and not null object");
        }
        setDataList(list != null ? list : new ArrayList<D>());
        mHolderClass = holderClass;
    }

    /**
     * 在列表的末尾处添加一个条目。
     *
     * @param object 要添加的对象。
     */
    public void addItem(D object) {
        addItem(getDataList().size(), object);
    }

    /**
     * 在列表的指定位置添加一个条目。
     *
     * @param position 要添加的位置。
     * @param object   要添加的对象。
     */
    public void addItem(int position, D object) {
        addItem(position, object, true);
    }

    /**
     * 在列表的指定位置添加一个条目。
     *
     * @param position 要添加的位置。
     * @param object   要添加的对象。
     * @param refresh  是否刷新列表。
     */
    public void addItem(int position, D object, boolean refresh) {
        mAdapterDataObservable.add(position, object);
        getDataList().add(position, object);
        if (refresh) {
            notifyRefresh();
        }
    }

    /**
     * 批量增加Item。
     *
     * @param datum 要增加Item。
     */
    public void addAll(Collection<D> datum) {
        addAll(datum, true);
    }

    /**
     * 批量增加Item。
     *
     * @param positionStart 批量增加的其实位置。
     * @param datum         要增加Item。
     */
    public void addAll(int positionStart, Collection<D> datum) {
        addAll(positionStart, datum, true);
    }

    /**
     * 批量增加Item。
     *
     * @param datum   要增加Item。
     * @param refresh 是否在增加完成后刷新条目。
     */
    public void addAll(Collection<D> datum, boolean refresh) {
        addAll(-1, datum, refresh);
    }

    /**
     * 批量增加Item。
     *
     * @param positionStart 批量增加的其实位置。
     * @param datum         要增加Item。
     * @param refresh       是否在增加完成后刷新条目。
     */
    public void addAll(int positionStart, Collection<D> datum, boolean refresh) {
        if (positionStart < 0) {
            positionStart = getDataList().size();
        }
        boolean addAll = getDataList().addAll(positionStart, datum);
        if (addAll) {
            mAdapterDataObservable.addAll(positionStart, datum);
        }
        if (refresh) {
            notifyRefresh();
        }
    }

    /**
     * 移除指定位置的条目。
     *
     * @param position 要移除的条目的位置。
     * @return 返回被移除的对象。
     */
    public D removeItem(int position) {
        return removeItem(position, true);
    }

    /**
     * 移除指定位置的条目。
     *
     * @param position 要移除的条目的位置。
     * @param refresh  是否在移除完成后刷新列表。
     * @return 返回被移除的对象。
     */
    public D removeItem(int position, boolean refresh) {
        if (!isEmptyList()) {
            D d = getDataList().remove(position);
            if (d != null) {
                mAdapterDataObservable.remove(d);
            }
            if (refresh) {
                notifyRefresh();
            }
            return d;
        } else {
            return null;
        }
    }

    /**
     * 将指定的对象从列表中移除。
     *
     * @param object 要移除的对象。
     * @return 移除成功返回改对象所在的位置，移除失败返回-1。
     */
    public int removeItem(D object) {
        return removeItem(object, true);
    }

    /**
     * 将指定的对象从列表中移除。
     *
     * @param object  要移除的对象。
     * @param refresh 是否在移除完成后刷新列表。
     * @return 移除成功返回改对象所在的位置，移除失败返回-1。
     */
    public int removeItem(D object, boolean refresh) {
        if (!isEmptyList()) {
            int position;
            List<D> dataList = getDataList();
            position = dataList.indexOf(object);
            if (position != -1) {
                boolean remove = dataList.remove(object);
                if (remove) {
                    mAdapterDataObservable.remove(object);
                }
                if (refresh) {
                    notifyRefresh();
                }
            }
            return position;
        } else {
            return -1;
        }
    }

    /**
     * 批量移除条目。
     *
     * @param positionStart 开始移除的位置。
     * @param itemCount     要移除的条目数。
     */
    public void removeAll(int positionStart, int itemCount) {
        removeAll(positionStart, itemCount, true);
    }

    /**
     * 批量移除条目。
     *
     * @param positionStart 开始移除的位置。
     * @param itemCount     要移除的条目数。
     * @param refresh       是否在移除成功后刷新列表。
     */
    public void removeAll(int positionStart, int itemCount, boolean refresh) {
        if (!isEmptyList()) {
            List<D> dataList = getDataList();
            int positionEnd = positionStart + itemCount > dataList.size() ? dataList.size() : positionStart + itemCount;
            for (int i = positionStart; i < positionEnd; i++) {
                D d = dataList.remove(positionStart);
                if (d != null) {
                    mAdapterDataObservable.remove(d);
                }
            }
            if (refresh) {
                notifyRefresh();
            }
        }
    }

    /**
     * 批量移除条目。
     *
     * @param datum 要移除的条目的数据模型对象。
     */
    public void removeAll(Collection<D> datum) {
        removeAll(datum, true);
    }

    /**
     * 批量移除条目。
     *
     * @param datum   要移除的条目的数据模型对象。
     * @param refresh 是否在移除成功后刷新列表。
     */
    public void removeAll(@NonNull Collection<D> datum, boolean refresh) {
        if (!isEmptyList() && !datum.isEmpty()) {
            List<D> dataList = getDataList();
            boolean removeAll = dataList.removeAll(datum);
            if (removeAll) {
                mAdapterDataObservable.removeAll(datum);
                if (refresh) {
                    notifyRefresh();
                }
            }
        }
    }

    /**
     * 清空列表。
     */
    public void clear() {
        clear(true);
    }

    /**
     * 清空列表。
     *
     * @param refresh 在清空完成后是否刷新列表。
     */
    public void clear(boolean refresh) {
        if (!isEmptyList()) {
            getDataList().clear();
            mAdapterDataObservable.removeAll(getDataList());
            if (refresh) {
                notifyRefresh();
            }
        }
    }

    /**
     * 获取指定对象在列表中的位置。
     *
     * @param object 要获取位置的对象。
     * @return 返回该对象在里列表中的位置。
     */
    public int getItemPosition(D object) {
        if (isEmptyList()) {
            return -1;
        }
        return getDataList().indexOf(object);
    }

    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemLayout rootLayout = mHolderClass.getAnnotation(ItemLayout.class);
        if (rootLayout != null) {
            int rootLayoutId = rootLayout.rootLayoutId();
            return createHolder(parent, rootLayoutId);
        }
        throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
    }

    /**
     * 当需要创建 {@link ItemViewHolder<D>} 对象的时候调用。
     *
     * @param parent {@link ItemViewHolder<D>} 的父容器对象，通常情况下为 {@link android.support.v7.widget.RecyclerView}。
     * @return {@link ItemViewHolder<D>} 对象。
     */
    H createHolder(ViewGroup parent, @LayoutRes int layoutId) {
        try {
            Constructor<? extends H> constructor = mHolderClass.getConstructor(ViewGroup.class, int.class);
            mViewHolder = constructor.newInstance(parent, layoutId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (mViewHolder != null) {
            bindItemClickEvent(mViewHolder);
            return mViewHolder;

        } else {
            throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
        }
    }

    /**
     * 绑定条目的点击事件。
     *
     * @param holder 要绑定的Holder。
     */
    void bindItemClickEvent(final H holder) {
        View.OnClickListener clickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemEventListener == null) return;
                int position = holder.getLayoutPosition();
                int adapterPosition = getAdapterPosition(holder);
                if (holder instanceof HeaderFooterViewHolder) {
                    HeaderFooterViewHolder viewHolder = (HeaderFooterViewHolder) holder;
                    if (viewHolder.isHeader()) {
                        mItemEventListener.onItemHeaderClick(position);
                    } else if (viewHolder.isFooter()) {
                        mItemEventListener.onItemFooterClick(position, adapterPosition);
                    }
                    return;
                }
                D object = getItemObject(holder);
                if (v.getId() == holder.itemView.getId() || v.getId() == holder.getItemClickViewId()) {
                    mItemEventListener.onItemClick(position, object, adapterPosition);
                } else {
                    mItemEventListener.onItemChildClick(position, object, v, adapterPosition);
                }
            }
        };

        View clickView;
        if (holder.getItemClickViewId() == 0 || (clickView = holder.getView(holder.getItemClickViewId())) == null) {
            clickView = holder.itemView;
        }
        clickView.setOnClickListener(clickListener);
        int[] childViewIds = holder.onGetNeedListenerChildViewIds();
        if (childViewIds != null && childViewIds.length > 0) {
            for (int viewId : childViewIds) {
                View v = holder.getView(viewId);
                if (v != null) {
                    v.setOnClickListener(clickListener);
                }
            }
        }
    }

    /**
     * 获取条目的数据模型。
     *
     * @param holder 当前的ViewHolder对象。
     */
    protected D getItemObject(H holder) {
        return getObject(holder.getLayoutPosition());
    }

    /**
     * 获取条目在Adapter中的位置。
     *
     * @param holder 当前的ViewHolder对象。
     */
    protected int getAdapterPosition(H holder) {
        return holder.getLayoutPosition();
    }

    @Override
    public void onBindViewHolder(H holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.onBindPartData(position, getObject(position), payloads);
        }
    }

    @Override
    public void onBindViewHolder(H holder, int position) {
        holder.onBindData(position, getItemObject(holder));
    }

    @Override
    public int getItemCount() {
        return isEmptyList() ? 0 : getDataList().size();
    }

    @Override
    protected boolean areContentsTheSame(D oldItemData, D newItemData) {
        return mViewHolder.areContentsTheSame(oldItemData, newItemData);
    }

    @Override
    protected boolean areItemsTheSame(D oldItemData, D newItemData) {
        return mViewHolder.areItemsTheSame(oldItemData, newItemData);
    }

    @Override
    protected void getChangePayload(D oldItemData, D newItemData, Bundle bundle) {
        mViewHolder.getChangePayload(oldItemData, newItemData, bundle);
    }

    /**
     * 注册数据观察者。
     *
     * @param observer 观察者对象。
     */
    public void registerObserver(AdapterDataObserver observer) {
        mAdapterDataObservable.registerObserver(observer);
    }

    /**
     * 取消注册数据观察者。
     *
     * @param observer 观察者对象。
     */
    public void unRegisterObserver(AdapterDataObserver observer) {
        mAdapterDataObservable.unregisterObserver(observer);
    }

    /**
     * 取消注册所有数据观察者。
     */
    public void unregisterAll() {
        mAdapterDataObservable.unregisterAll();
    }

    private class AdapterDataObservable extends Observable<AdapterDataObserver> {

        private final SingleTypeAdapter mAdapter;

        AdapterDataObservable(SingleTypeAdapter singleTypeAdapter) {
            mAdapter = singleTypeAdapter;
        }

        public void add(int position, Object object) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).add(position, object, mAdapter);
            }
        }

        void addAll(int firstPosition, Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).addAll(firstPosition, (Collection<Object>) dataList, mAdapter);
            }
        }

        void remove(Object d) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).remove(d, mAdapter);
            }
        }

        void removeAll(Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).removeAll((Collection<Object>) dataList, mAdapter);
            }
        }
    }

    static abstract class AdapterDataObserver {
        /**
         * 列表中新增了数据。
         *
         * @param position 被新增的位置。
         * @param object   新增的数据。
         * @param adapter  当前被观察的Adapter对象。
         */
        protected abstract void add(int position, Object object, SingleTypeAdapter adapter);

        /**
         * 列表中批量新增了数据。
         *
         * @param firstPosition 新增的起始位置。
         * @param dataList      新增的数据集合。
         * @param adapter       当前被观察的Adapter对象。
         */
        protected abstract void addAll(int firstPosition, Collection<Object> dataList, SingleTypeAdapter adapter);

        /**
         * 删除了列表中的数据。
         *
         * @param object  被删除的数据。
         * @param adapter 当前被观察的Adapter对象。
         */
        protected abstract void remove(Object object, SingleTypeAdapter adapter);

        /**
         * 批量删除了列表中的数据。
         *
         * @param dataList 被删除的数据集合。
         * @param adapter  当前被观察的Adapter对象。
         */
        protected abstract void removeAll(Collection<Object> dataList, SingleTypeAdapter adapter);
    }
}