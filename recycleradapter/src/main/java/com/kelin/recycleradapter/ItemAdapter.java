package com.kelin.recycleradapter;

import android.database.Observable;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;
import com.kelin.recycleradapter.interfaces.AdapterEdit;
import com.kelin.recycleradapter.interfaces.ChildEventBindInterceptor;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 描述 {@link RecyclerView} 的适配器 {@link MultiTypeAdapter} 的子适配器。
 * 创建人 kelin
 * 创建时间 2017/1/19  下午4:22
 * 版本 v 1.0.0
 */

public class ItemAdapter<D> implements AdapterEdit<D, ItemViewHolder<D>> {
    /**
     * 表示创建头Holder。
     */
    private static final int HEADER_HOLDER = 0X0000_0101;
    /**
     * 表示创建脚Holder。
     */
    private static final int FOOTER_HOLDER = 0X0000_0102;
    private static final String TAG = "ItemAdapter";
    /**
     * {@link MultiTypeAdapter} 对象。也是当前Adapter的父级Adapter，是输入RecyclerView的Adapter。
     */
    private MultiTypeAdapter mParentAdapter;
    /**
     * 用来记录当前Adapter在RecyclerView列表中的起始位置。
     */
    int firstItemPosition;
    /**
     * 用来记录当前Adapter在RecyclerView列表中的结束位置。
     */
    int lastItemPosition;
    /**
     * 适配器数据的观察者对象。
     */
    private AdapterDataObservable mAdapterDataObservable = new AdapterDataObservable();
    /**
     * 用来存放ViewHolder的字节码对象。
     */
    private Class<? extends ItemViewHolder<D>> mHolderClass;
    /**
     * 用来记录头布局的资源文件ID。
     */
    private
    @LayoutRes
    int mHeaderLayoutId;
    /**
     * 用来记录脚布局的资源文件ID。
     */
    private
    @LayoutRes
    int mFooterLayoutId;
    /**
     * 用来记录当前适配器中的布局资源ID。
     */
    private
    @LayoutRes
    int mRootLayoutId;
    /**
     * 条目事件监听对象。
     */
    private OnItemEventListener<D> mItemEventListener;
    /**
     * 用来记录条目的占屏比。
     */
    private int mItemSpanSize;
    /**
     * 当前页面的数据集。
     */
    private List<D> mDataList;

    private boolean isVisible;
    private ItemViewHolder<D> mFloatLayoutBinder;
    private boolean isFloatAble;
    private ChildEventBindInterceptor mEventInterceptor;

    public ItemAdapter(@NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(@Size(min = 1, max = 100) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(null, spanSize, holderClass);
    }

    public ItemAdapter(List<D> list, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(list, SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(List<D> list, @Size(min = 1, max = 100) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        mItemSpanSize = spanSize <= 0 ? SPAN_SIZE_FULL_SCREEN : spanSize;
        mHolderClass = holderClass;
        ItemLayout annotation = holderClass.getAnnotation(ItemLayout.class);
        if (annotation != null) {
            mRootLayoutId = annotation.value();
        } else {
            throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
        }

        setDataList(list);
    }

    public void setFloatAble(boolean floatAble) {
        isFloatAble = floatAble;
    }

    /**
     * 设置当前是否可见。
     *
     * @param visible true表示可见，false表示不可见。
     */
    void setVisibleState(boolean visible) {
        isVisible = visible;
    }

    /**
     * 判断当前适配器是否可见。
     *
     * @return 可见返回true, 不可见返回false。
     */
    boolean isVisible() {
        return isVisible;
    }

    /**
     * 设置数据。
     *
     * @param list 数据集合。
     */
    public void setDataList(List<D> list) {
        mDataList = list != null ? list : new ArrayList<D>();
    }

    /**
     * 获取当前的数据集合。
     */
    List<D> getDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
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
     * 在列表的末尾处添加一个条目。
     *
     * @param object 要添加的对象。
     */
    @Override
    public void addItem(@NonNull D object) {
        addItem(getDataList().size(), object);
    }

    /**
     * 在列表的指定位置添加一个条目。
     *
     * @param position 要添加的位置。
     * @param object   要添加的对象。
     */
    @Override
    public void addItem(int position, @NonNull D object) {
        addItem(position, object, true);
    }

    /**
     * 在列表的指定位置添加一个条目。
     *
     * @param position 要添加的位置。
     * @param object   要添加的对象。
     * @param refresh  是否刷新列表。
     */
    @Override
    public void addItem(int position, @NonNull D object, boolean refresh) {
        getDataList().add(position, object);
        mAdapterDataObservable.add(position, object);
        if (refresh) {
            mapNotifyItemInserted(position);
        }
    }

    /**
     * 批量增加Item。
     *
     * @param datum 要增加Item。
     */
    @Override
    public void addAll(@NonNull Collection<D> datum) {
        addAll(datum, true);
    }

    /**
     * 批量增加Item。
     *
     * @param positionStart 批量增加的其实位置。
     * @param datum         要增加Item。
     */
    @Override
    public void addAll(@Size(min = 0) int positionStart, @NonNull Collection<D> datum) {
        addAll(positionStart, datum, true);
    }

    /**
     * 批量增加Item。
     *
     * @param datum   要增加Item。
     * @param refresh 是否在增加完成后刷新条目。
     */
    @Override
    public void addAll(@NonNull Collection<D> datum, boolean refresh) {
        addAll(-1, datum, refresh);
    }

    /**
     * 批量增加Item。
     *
     * @param positionStart 批量增加的其实位置。
     * @param datum         要增加Item。
     * @param refresh       是否在增加完成后刷新条目。
     */
    @Override
    public void addAll(@Size(min = 0) int positionStart, @NonNull Collection<D> datum, boolean refresh) {
        if (datum.isEmpty()) return;
        if (positionStart < 0) {
            positionStart = getDataList().size();
        }
        boolean addAll = getDataList().addAll(positionStart, datum);
        if (addAll) {
            mAdapterDataObservable.addAll(positionStart, datum);
            if (refresh) {
                mapNotifyItemRangeInserted(positionStart, datum.size());
            }
        }
    }

    /**
     * 移除指定位置的条目。
     *
     * @param position 要移除的条目的位置。
     * @return 返回被移除的对象。
     */
    @Override
    public D removeItem(@Size(min = 0) int position) {
        return removeItem(position, true);
    }

    /**
     * 移除指定位置的条目。
     *
     * @param position 要移除的条目的位置。
     * @param refresh  是否在移除完成后刷新列表。
     * @return 返回被移除的对象。
     */
    @Override
    public D removeItem(@Size(min = 0) int position, boolean refresh) {
        if (position < 0) return null;
        if (!isEmptyList()) {
            D d = getDataList().remove(position);
            if (d != null) {
                mAdapterDataObservable.remove(d);
                if (refresh) {
                    mapNotifyItemRemoved(position);
                }
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
    @Override
    public int removeItem(@NonNull D object) {
        return removeItem(object, true);
    }

    /**
     * 将指定的对象从列表中移除。
     *
     * @param object  要移除的对象。
     * @param refresh 是否在移除完成后刷新列表。
     * @return 移除成功返回改对象所在的位置，移除失败返回-1。
     */
    @Override
    public int removeItem(@NonNull D object, boolean refresh) {
        if (!isEmptyList()) {
            int position;
            List<D> dataList = getDataList();
            position = dataList.indexOf(object);
            if (position != -1) {
                boolean remove = dataList.remove(object);
                if (remove) {
                    mAdapterDataObservable.remove(object);
                    if (refresh) {
                        mapNotifyItemRemoved(position);
                    }
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
    @Override
    public void removeAll(@Size(min = 0) int positionStart, int itemCount) {
        removeAll(positionStart, itemCount, true);
    }

    /**
     * 批量移除条目。
     *
     * @param positionStart 开始移除的位置。
     * @param itemCount     要移除的条目数。
     * @param refresh       是否在移除成功后刷新列表。
     */
    @Override
    public void removeAll(@Size(min = 0) int positionStart, @Size(min = 0) int itemCount, boolean refresh) {
        if (positionStart < 0 || itemCount < 0)
            throw new IllegalArgumentException("the positionStart Arguments or itemCount Arguments must is greater than 0 integer");
        if (!isEmptyList()) {
            List<D> dataList = getDataList();
            int positionEnd = positionStart + (itemCount = itemCount > dataList.size() ? dataList.size() : itemCount);
            List<D> temp = new ArrayList<>();
            for (int i = positionStart; i < positionEnd; i++) {
                temp.add(dataList.get(i));
            }
            boolean removeAll = dataList.removeAll(temp);
            if (removeAll) {
                mAdapterDataObservable.removeAll(temp);
                if (refresh) {
                    mapNotifyItemRangeRemoved(positionStart, itemCount);
                }
            }
        }
    }

    /**
     * 批量移除条目。
     *
     * @param datum 要移除的条目的数据模型对象。
     */
    @Override
    public void removeAll(@NonNull Collection<D> datum) {
        removeAll(datum, true);
    }

    /**
     * 批量移除条目。
     *
     * @param datum   要移除的条目的数据模型对象。
     * @param refresh 是否在移除成功后刷新列表。
     */
    @Override
    public void removeAll(@NonNull Collection<D> datum, boolean refresh) {
        if (!isEmptyList() && !datum.isEmpty()) {
            List<D> dataList = getDataList();
            Iterator<D> iterator = datum.iterator();
            D d = iterator.next();
            int positionStart = dataList.indexOf(d);
            boolean removeAll = dataList.removeAll(datum);
            if (removeAll) {
                mAdapterDataObservable.removeAll(datum);
                if (refresh) {
                    mapNotifyItemRangeRemoved(positionStart, datum.size());
                }
            }
        }
    }

    /**
     * 清空列表。
     */
    @Override
    public void clear() {
        clear(true);
    }

    /**
     * 清空列表。
     *
     * @param refresh 在清空完成后是否刷新列表。
     */
    @Override
    public void clear(boolean refresh) {
        if (!isEmptyList()) {
            List<D> dataList = getDataList();
            dataList.clear();
            mAdapterDataObservable.removeAll(dataList);
            if (refresh) {
                mapNotifyItemRangeRemoved(0, dataList.size());
            }
        }
    }

    /**
     * 获取条目类型。
     *
     * @return 返回跟布局的资源ID。
     */
    @Override
    public
    @LayoutRes
    int getRootViewType() {
        return mRootLayoutId;
    }

    /**
     * 当需要创建ViewHolder的时候调用。
     *
     * @param parent   当前的parent对象，也就是RecyclerView对象。
     * @param viewType 当前的条目类型，也是当前要创建的ViewHolder的布局文件ID。
     * @return 需要返回一个 {@link ItemViewHolder<D>} 对象。
     */
    @Override
    public ItemViewHolder<D> onCreateViewHolder(ViewGroup parent, int viewType) {
        ItemViewHolder<D> viewHolder;
        try {
            Constructor<? extends ItemViewHolder<D>> constructor = mHolderClass.getDeclaredConstructor(ViewGroup.class, int.class);
            constructor.setAccessible(true);
            viewHolder = constructor.newInstance(parent, viewType);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if (viewHolder != null) {
            bindItemClickEvent(viewHolder);
            return viewHolder;

        } else {
            throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
        }
    }


    /**
     * 设置条目的事件监听。
     *
     * @param listener {@link SingleTypeAdapter.OnItemEventListener} 对象。
     */
    public void setItemEventListener(@NonNull OnItemEventListener<D> listener) {
        mItemEventListener = listener;
    }

    public void notifyRefresh() {
        //因为ItemAdapter不是RecyclerView的Adapter，所以这里是调用父级Adapter的刷新。
        if (mParentAdapter != null) {
            mParentAdapter.notifyRefresh();
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder<D> holder, int position, List<Object> payloads) {
        if (position == 0) {
            setVisibleState(true);
            Log.i(TAG, "onViewRecycled: 条目被显示并绑定数据。AdapterPosition=" + position);
        }
        if (isFloatAble && mFloatLayoutBinder == null) {
            mFloatLayoutBinder = holder;
        }
        holder.onBindPartData(position, getItemObject(holder), payloads);
    }

    /**
     * 获取当前条目的总数量。
     */
    @Override
    public int getItemCount() {
        return getDataList().size();
    }

    /**
     * 获取条目的占屏比。
     *
     * @param position 当前的条目的位置。
     * @return 返回当前条目的占屏比。
     */
    @Override
    public int getItemSpanSize(int position) {
        return mItemSpanSize;
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

    private D getItemObject(ItemViewHolder<D> holder) {
        return getItemObject(holder.getLayoutPosition());
    }

    private D getItemObject(int parentPosition) {
        return (D) mParentAdapter.getObject(parentPosition);
    }

    @SuppressWarnings("unchecked")
    private View.OnClickListener onGetClickListener(final ItemViewHolder<D> viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemEventListener == null) return;
                mItemEventListener.adapter = mParentAdapter.getChildAdapterByPosition(viewHolder.getLayoutPosition());
                int position = viewHolder.getLayoutPosition();
                int adapterPosition = getAdapterPosition(viewHolder);

                D object = getItemObject(viewHolder);
                if (v.getId() == viewHolder.itemView.getId() || v.getId() == viewHolder.getItemClickViewId()) {
                    mItemEventListener.onItemClick(position, object, adapterPosition);
                } else {
                    mItemEventListener.onItemChildClick(position, object, v, adapterPosition);
                }
            }
        };
    }

    private View.OnLongClickListener onGetLongClickListener(final ItemViewHolder<D> viewHolder) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemEventListener != null) {
                    mItemEventListener.onItemLongClick(viewHolder.getLayoutPosition(), getItemObject(viewHolder), getAdapterPosition(viewHolder));
                }
                return true;
            }
        };
    }

    private int getAdapterPosition(ItemViewHolder<D> holder) {
        return mParentAdapter.getItemAdapterPosition(holder.getLayoutPosition());
    }

    void setParent(MultiTypeAdapter parent) {
        mParentAdapter = parent;
        //被添加的MultiTypeAdapter中以后检查悬浮条件的规则。
        if (isFloatAble() && (mItemSpanSize < mParentAdapter.getTotalSpanSize() || getItemCount() > 1)) {
            throw new RuntimeException("The current child adapter number must be can only be one, and not have header or footer in the child adapter, also must is be full screen width.");
        }
    }

    Class<?> getItemModelClass() {
        return isEmptyList() ? null : getObject(0).getClass();
    }

    private void bindItemClickEvent(ItemViewHolder<D> viewHolder) {
        View.OnClickListener onClickListener = onGetClickListener(viewHolder);

        if (viewHolder.clickable()) {
            View clickView;
            if (viewHolder.getItemClickViewId() == 0 || (clickView = viewHolder.getView(viewHolder.getItemClickViewId())) == null) {
                clickView = viewHolder.itemView;
            }
            clickView.setOnClickListener(onClickListener);
            clickView.setOnLongClickListener(onGetLongClickListener(viewHolder));
        }
        int[] childViewIds = viewHolder.onGetNeedListenerChildViewIds();
        if (childViewIds != null && childViewIds.length > 0) {
            View v;
            for (int viewId : childViewIds) {
                v = viewHolder.getView(viewId);
                if (v != null && (mEventInterceptor == null || !mEventInterceptor.onInterceptor(v))) {
                    v.setOnClickListener(onClickListener);
                }
            }
        }
    }

    private void mapNotifyItemInserted(int position) {
        if (mParentAdapter != null)
            mParentAdapter.notifyItemInserted(position + firstItemPosition);
    }

    private void mapNotifyItemRangeInserted(int positionStart, int itemCount) {
        if (mParentAdapter != null)
            mParentAdapter.notifyItemRangeInserted(positionStart + firstItemPosition, itemCount);
    }

    private void mapNotifyItemRemoved(int position) {
        if (mParentAdapter != null) {
            if (!isEmptyList()) {
                mParentAdapter.notifyItemRemoved(position + firstItemPosition);
            } else {
                mParentAdapter.notifyItemRemoved(position + firstItemPosition);
            }
        }
    }

    private void mapNotifyItemRangeRemoved(int positionStart, int itemCount) {
        if (isEmptyList()) {
            positionStart = firstItemPosition;

        } else {
            positionStart += firstItemPosition;
        }
        if (mParentAdapter != null) mParentAdapter.notifyItemRangeRemoved(positionStart, itemCount);
    }

    /**
     * 注册数据观察者。
     *
     * @param observer 观察者对象。
     */
    void registerObserver(AdapterDataObserver observer) {
        mAdapterDataObservable.registerObserver(observer);
    }

    /**
     * 取消注册数据观察者。
     *
     * @param observer 观察者对象。
     */
    void unRegisterObserver(AdapterDataObserver observer) {
        mAdapterDataObservable.unregisterObserver(observer);
    }

    /**
     * 取消注册所有数据观察者。
     */
    void unregisterAll() {
        mAdapterDataObservable.unregisterAll();
    }

    /**
     * 当当前Adapter中视图中消失的时候调用。
     *
     * @param holder   当前的ViewHolder对象。
     * @param position 当前的索引。
     */
    void onViewRecycled(ItemViewHolder holder, int position) {
        if (position == getItemCount() - 1) {
            setVisibleState(false);
            Log.i(TAG, "onViewRecycled: 条目被释放。AdapterPosition=" + position);
        }
    }

    void onBindFloatViewData(ViewHelper viewHelper, int position, D d) {
        mFloatLayoutBinder.onBindFloatLayoutData(viewHelper, position - firstItemPosition, d);
    }

    public boolean isFloatAble() {
        return isFloatAble;
    }

    /**
     * 设置条目子控件事件绑定的拦截器。
     * @param interceptor 子控件事件绑定的拦截器。
     */
    public void setEventInterceptor(ChildEventBindInterceptor interceptor) {
        mEventInterceptor = interceptor;
    }

    /**
     * 当前适配器的数据改变被观察者。
     */
    private class AdapterDataObservable extends Observable<AdapterDataObserver> {

        public void add(int position, Object object) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).add(position, object, ItemAdapter.this);
            }
        }

        void addAll(int firstPosition, Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).addAll(firstPosition, dataList, ItemAdapter.this);
            }
        }

        void remove(Object d) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).remove(d, ItemAdapter.this);
            }
        }

        void removeAll(Collection<D> dataList) {
            for (int i = mObservers.size() - 1; i >= 0; i--) {
                mObservers.get(i).removeAll(dataList, ItemAdapter.this);
            }
        }
    }

    /**
     * 当前适配器的数据观察者对象。
     */
    static abstract class AdapterDataObserver {
        /**
         * 列表中新增了数据。
         *
         * @param position 被新增的位置。
         * @param object   新增的数据。
         * @param adapter  当前被观察的Adapter对象。
         */
        protected abstract void add(int position, Object object, ItemAdapter adapter);

        /**
         * 列表中批量新增了数据。
         *
         * @param firstPosition 新增的起始位置。
         * @param dataList      新增的数据集合。
         * @param adapter       当前被观察的Adapter对象。
         */
        protected abstract void addAll(int firstPosition, Collection dataList, ItemAdapter adapter);

        /**
         * 删除了列表中的数据。
         *
         * @param object  被删除的数据。
         * @param adapter 当前被观察的Adapter对象。
         */
        protected abstract void remove(Object object, ItemAdapter adapter);

        /**
         * 批量删除了列表中的数据。
         *
         * @param dataList 被删除的数据集合。
         * @param adapter  当前被观察的Adapter对象。
         */
        protected abstract void removeAll(Collection dataList, ItemAdapter adapter);
    }

    /**
     * 描述 {@link RecyclerView} 的条目点击事件监听。
     * 创建人 kelin
     * 创建时间 2017/1/19  下午12:21
     * 版本 v 1.0.0
     *
     * @param <D> 数据模型的泛型限定。
     */
    public static abstract class OnItemEventListener<D> {
        private ItemAdapter<D> adapter;

        protected ItemAdapter<D> getAdapter() {
            return adapter;
        }

        /**
         * 当条目被点击的时候调用。
         *
         * @param position        当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param d               被点击的条目的条目信息对象。
         * @param adapterPosition 当前被点击的条目在 {@link android.support.v7.widget.RecyclerView.Adapter} 中的索引,
         *                        一般情况下该参数的值会和 position 参数的值一致。只有当使用 {@link ItemAdapter}
         *                        作为 {@link MultiTypeAdapter} 的子条目并使用该监听时这个值才有意义，
         *                        这时该参数的值将会与 position 参数的值不同，该参数的值将表示当前点击的条目在当前子 Adapter 中的位置。
         */
        public abstract void onItemClick(int position, D d, int adapterPosition);

        /**
         * 当条目被长时点击的时候调用。
         *
         * @param position        当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param d               被点击的条目的条目信息对象。
         * @param adapterPosition 当前被点击的条目在 {@link android.support.v7.widget.RecyclerView.Adapter} 中的索引,
         *                        一般情况下该参数的值会和 position 参数的值一致。只有当使用 {@link ItemAdapter}
         *                        作为 {@link MultiTypeAdapter} 的子条目并使用该监听时这个值才有意义，
         *                        这时该参数的值将会与 position 参数的值不同，该参数的值将表示当前点击的条目在当前子 Adapter 中的位置。
         */
        public void onItemLongClick(int position, D d, int adapterPosition) {
        }

        /**
         * 当条目中的子控件被点击的时候调用。
         *       * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
         *
         * @param d               被点击的条目的条目信息对象。
         * @param view            被点击的{@link View}。
         * @param adapterPosition 当前被点击的条目在 {@link android.support.v7.widget.RecyclerView.Adapter} 中的索引,
         *                        一般情况下该参数的值会和 position 参数的值一致。只有当使用 {@link ItemAdapter}
         *                        作为 {@link MultiTypeAdapter} 的子条目并使用该监听时这个值才有意义，
         *                        这时该参数的值将会与 position 参数的值不同，该参数的值将表示当前点击的条目在当前子 Adapter 中的位置。
         */
        public abstract void onItemChildClick(int position, D d, View view, int adapterPosition);
    }
}
