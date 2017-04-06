package com.kelin.recycleradapter;

import android.database.Observable;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;

import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.List;

/**
 * 描述 ${TODO}
 * 创建人 kelin
 * 创建时间 2017/4/6  下午1:36
 * 版本 v 1.0.0
 */

public abstract class EditableSupperAdapter<D, VH extends ItemViewHolder<D>> extends SupperAdapter<D, VH> {

    /**
     * 适配器数据的观察者对象。
     */
    private AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable();

    /**
     * 当前适配器中的ViewHolder对象。
     */
    private VH mViewHolder;

    /**
     * 用来存放ViewHolder的字节码对象。
     */
    Class<? extends VH> mHolderClass;

    EditableSupperAdapter(List<D> list, Class<? extends VH> holderClass) {
        setDataList(list);
        mHolderClass = holderClass;
    }

    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return createHolder(parent, viewType);
    }

    /**
     * 当需要创建 {@link ItemViewHolder<D>} 对象的时候调用。
     *
     * @param parent {@link ItemViewHolder<D>} 的父容器对象，通常情况下为 {@link android.support.v7.widget.RecyclerView}。
     * @return {@link ItemViewHolder<D>} 对象。
     */
    VH createHolder(ViewGroup parent, @LayoutRes int layoutId) {
        try {
            Constructor<? extends VH> constructor = mHolderClass.getDeclaredConstructor(ViewGroup.class, int.class);
            constructor.setAccessible(true);
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

    void bindItemClickEvent(VH viewHolder) {
        View.OnClickListener onClickListener = onGetClickListener(viewHolder);

        View clickView;
        if (viewHolder.getItemClickViewId() == 0 || (clickView = viewHolder.getView(viewHolder.getItemClickViewId())) == null) {
            clickView = viewHolder.itemView;
        }
        clickView.setOnClickListener(onClickListener);
        int[] childViewIds = viewHolder.onGetNeedListenerChildViewIds();
        if (childViewIds != null && childViewIds.length > 0) {
            for (int viewId : childViewIds) {
                View v = viewHolder.getView(viewId);
                if (v != null) {
                    v.setOnClickListener(onClickListener);
                }
            }
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            onBindViewHolder(holder, position);
        } else {
            holder.onBindPartData(position, getObject(position), payloads);
        }
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.onBindData(position, getItemObject(holder));
    }

    @Override
    public int getItemViewType(int position) {
        ItemLayout rootLayout = mHolderClass.getAnnotation(ItemLayout.class);
        if (rootLayout != null) {
            return rootLayout.rootLayoutId();
        }
        throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
    }

    @Override
    public int getItemCount() {
        return isEmptyList() ? 0 : getDataList().size();
    }

    /**
     * 获取条目的数据模型。
     *
     * @param holder 当前的ViewHolder对象。
     */
    protected D getItemObject(VH holder) {
        return getObject(holder.getLayoutPosition());
    }

    /**
     * 当需要给ViewHolder绑定事件的时候调用。
     * @param viewHolder 当前要绑定事件的ViewHolder对象。
     */
    protected abstract View.OnClickListener onGetClickListener(VH viewHolder);


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
            notifyRefresh();   // TODO: 2017/4/5 这里只是测试在这样使用，测试完成后要换成notifyItemxxxx()
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

        public void add(int position, Object object) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).add(position, object, EditableSupperAdapter.this);
            }
        }

        void addAll(int firstPosition, Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).addAll(firstPosition, (Collection<Object>) dataList, EditableSupperAdapter.this);
            }
        }

        void remove(Object d) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).remove(d, EditableSupperAdapter.this);
            }
        }

        void removeAll(Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).removeAll((Collection<Object>) dataList, EditableSupperAdapter.this);
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
        protected abstract void add(int position, Object object, EditableSupperAdapter adapter);

        /**
         * 列表中批量新增了数据。
         *
         * @param firstPosition 新增的起始位置。
         * @param dataList      新增的数据集合。
         * @param adapter       当前被观察的Adapter对象。
         */
        protected abstract void addAll(int firstPosition, Collection<Object> dataList, EditableSupperAdapter adapter);

        /**
         * 删除了列表中的数据。
         *
         * @param object  被删除的数据。
         * @param adapter 当前被观察的Adapter对象。
         */
        protected abstract void remove(Object object, EditableSupperAdapter adapter);

        /**
         * 批量删除了列表中的数据。
         *
         * @param dataList 被删除的数据集合。
         * @param adapter  当前被观察的Adapter对象。
         */
        protected abstract void removeAll(Collection<Object> dataList, EditableSupperAdapter adapter);
    }
}