package com.kelin.recycleradapter;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.annotation.CheckResult;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.holder.LoadMoreViewHolder;
import com.kelin.recycleradapter.holder.ViewHelper;
import com.kelin.recycleradapter.interfaces.Orientation;
import com.kelin.recycleradapter.interfaces.Pool;
import com.kelin.recycleradapter.view.FloatLayout;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 描述 多条目的 {@link RecyclerView} 的适配器。
 * 创建人 kelin
 * 创建时间 2016/12/6  下午6:30
 * 版本 v 1.0.0
 */

public class MultiTypeAdapter extends SuperAdapter<Object, ItemViewHolder<Object>> {

    private static final String TAG = "MultiTypeAdapter";
    /**
     * 用来存放不同数据模型的 {@link ItemViewHolder}。不同数据模型的 {@link ItemViewHolder} 只会被存储一份，且是最初创建的那个。
     */
    private Map<Class, ItemViewHolder> mItemViewHolderMap = new HashMap<>();
    /**
     * 子条目数据变化的观察者。
     */
    private ItemAdapterDataObserver mAdapterDataObserver = new ItemAdapterDataObserver();
    /**
     * 加载更多失败时，点击重试的监听。
     */
    private LoadMoreRetryClickListener mLoadMoreRetryClickListener;
    /**
     * 用来展示悬浮窗的布局容器。
     */
    private FloatLayout mFloatLayout;
    /**
     * 用来记录当前第一个可见的条目的位置。
     */
    private int mCurPosition;
    /**
     * 用来记录悬浮条的高度。
     */
    private int mFloatLayoutHeight;
    /**
     * 用来记录上一次被绑定数的悬浮条的位置。
     */
    private int mLastBindPosition;
    /**
     * 为悬浮条绑定数据的帮助者。
     */
    private ViewHelper mFloatViewHelper;
    /**
     * 子条目的对象池。
     */
    private ChildAdapterPool mPool = new ChildAdapterPool();

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView 您要绑定的 {@link RecyclerView} 对象。
     */
    public MultiTypeAdapter(@NonNull RecyclerView recyclerView) {
        this(recyclerView, 1);
    }

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView  您要绑定的 {@link RecyclerView} 对象。
     * @param totalSpanSize 总的占屏比，通俗来讲就是 {@link RecyclerView} 的宽度被均分成了多少份。该值的范围是1~100之间的数(包含)。
     */
    public MultiTypeAdapter(@NonNull RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize) {
        this(recyclerView, totalSpanSize, LinearLayout.VERTICAL);
    }

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView  您要绑定的 {@link RecyclerView} 对象。
     * @param totalSpanSize 总的占屏比，通俗来讲就是 {@link RecyclerView} 的宽度被均分成了多少份。该值的范围是1~100之间的数(包含)。
     * @param orientation   列表的方向，该参数的值只能是{@link LinearLayout#HORIZONTAL} or {@link LinearLayout#VERTICAL}的其中一个。
     */
    public MultiTypeAdapter(@NonNull RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize, @Orientation int orientation) {
        super(recyclerView, totalSpanSize, orientation);
    }

    /**
     * 设置用来显示悬浮条目的布局。
     *
     * @param floatLayout 一个 {@link FloatLayout} 对象。
     */
    public void setFloatLayout(@NonNull FloatLayout floatLayout) {
        Drawable background = getRecyclerView().getBackground();
        mFloatLayout = floatLayout;
        ColorDrawable bg = (ColorDrawable) (background == null ? ((ViewGroup) getRecyclerView().getParent()).getBackground() : background);
        int color = bg.getColor();
        GradientDrawable drawable = new GradientDrawable(GradientDrawable.Orientation.TOP_BOTTOM, new int[]{color, Color.alpha(0)});
        mFloatLayout.setBackground(drawable);
        mFloatViewHelper = new ViewHelper(mFloatLayout);
        setFloatLayoutVisibility(false);
    }

    /**
     * 获取子适配器 {@link ItemAdapter} 的数量。
     * @return 返回已经被Add进来的所有的 {@link ItemAdapter} 的数量。
     * @see #addAdapter(ItemAdapter[])
     */
    public int getChildCount() {
        return mPool.size();
    }

    /**
     * 根据索引获取 {@link ItemAdapter}。
     * @param index 要获取的索引。
     * @return 返回已经被Add进来的指定索引的 {@link ItemAdapter}。
     * @see #addAdapter(ItemAdapter[])
     */
    public ItemAdapter getChildAt(int index) {
        return mPool.acquireAll().get(index);
    }

    /**
     * 获取所有的 {@link ItemAdapter}。
     * @return 返回已经被Add进来的所有的 {@link ItemAdapter}。
     * @see #addAdapter(ItemAdapter[])
     */
    public List<ItemAdapter> getAllChild() {
        return mPool.acquireAll();
    }

    private void setFloatLayoutVisibility(boolean visible) {
        if (mFloatLayout != null) {
            mFloatLayout.setVisibility(visible ? View.VISIBLE : View.GONE);
        }
    }

    private boolean isFloatLayoutShowing() {
        return mFloatLayout != null && mFloatLayout.getVisibility() == View.VISIBLE;
    }

    /**
     * 添加条目适配器 {@link ItemAdapter}。
     *
     * @param adapters {@link ItemAdapter} 对象。
     * @see ItemAdapter#ItemAdapter(Class)
     * @see ItemAdapter#ItemAdapter(int, Class)
     * @see ItemAdapter#ItemAdapter(List, Class)
     * @see ItemAdapter#ItemAdapter(List, int, Class)
     */
    @SuppressWarnings("unchecked")
    public MultiTypeAdapter addAdapter(@NonNull ItemAdapter... adapters) {
        for (ItemAdapter adapter : adapters) {
            adapter.registerObserver(mAdapterDataObserver);
            mPool.release(adapter);
            adapter.firstItemPosition = getDataList().size();
            addDataList(adapter.getDataList());
            adapter.lastItemPosition = getDataList().size() - 1;
            adapter.setParent(this);
        }
        return this;
    }

    @SuppressWarnings("unchecked")
    @Override
    public ItemViewHolder<Object> onCreateViewHolder(ViewGroup parent, int viewType) {
        if (mLoadMoreLayoutManager != null && viewType == mLoadMoreLayoutManager.getCurStateLayoutId()) {
            LoadMoreViewHolder loadMoreViewHolder = new LoadMoreViewHolder(parent, viewType);
            if (mLoadMoreLayoutManager.isRetryState()) {
                if (mLoadMoreRetryClickListener == null) {
                    mLoadMoreRetryClickListener = new LoadMoreRetryClickListener();
                }
                View clickView = loadMoreViewHolder.getView(loadMoreViewHolder.getItemClickViewId());
                if (clickView == null) {
                    clickView = loadMoreViewHolder.itemView;
                }
                clickView.setOnClickListener(mLoadMoreRetryClickListener);
            }
            return loadMoreViewHolder;
        }
        ItemViewHolder holder = null;
        for (ItemAdapter adapter : mPool.acquireAll()) {
            if (adapter.getRootViewType() == viewType) {
                holder = adapter.onCreateViewHolder(parent, viewType);
                if (mFloatLayout != null && adapter.isFloatAble()) {
                    mFloatLayout.setFloatContent(viewType, new FloatLayout.OnSizeChangedListener() {
                        @Override
                        public void onSizeChanged(int width, int height) {
                            mFloatLayoutHeight = height;
                        }
                    });
                }
                Class itemModelClass = adapter.getItemModelClass();
                if (!mItemViewHolderMap.containsKey(itemModelClass)) {
                    mItemViewHolderMap.put(itemModelClass, holder);
                }
            }
            if (holder != null) {
                return holder;
            }
        }
        throw new RuntimeException("the viewType: " + viewType + " not found !");
    }

    @Override
    public void onViewRecycled(ItemViewHolder<Object> holder) {
        if (holder instanceof LoadMoreViewHolder) {
            return;
        }
        int position = holder.getLayoutPosition();
        ItemAdapter itemAdapter = mPool.acquireFromLayoutPosition(position);
        itemAdapter.onViewRecycled(holder, position - itemAdapter.firstItemPosition);
    }

    @Override
    protected void onRecyclerViewScrolled(RecyclerView recyclerView, int dx, int dy, LinearLayoutManager lm) {
        //如果没有设置悬浮控件就不获取子适配器，这用这里的逻辑就不会执行。
        ItemAdapter adapter = mFloatLayout == null ? null : getAdjacentChildAdapterByPosition(mCurPosition, true, false);
        if (adapter != null && adapter.isFloatAble() && dy != 0) {
            View view = lm.findViewByPosition(adapter.firstItemPosition);
            if (view != null) {
                if (isFirstFloatAbleChildAdapter(mCurPosition + 1) && isFloatLayoutShowing()) {
                    setFloatLayoutVisibility(false);
                } else {
                    if (view.getTop() <= (isFloatLayoutShowing() ? mFloatLayoutHeight : 0)) {
                        setFloatLayoutVisibility(true);
                        ViewGroup.LayoutParams layoutParams = view.getLayoutParams();
                        int marginTop = 0;
                        if (layoutParams instanceof ViewGroup.MarginLayoutParams) {
                            marginTop = ((ViewGroup.MarginLayoutParams) layoutParams).topMargin;
                        }
                        if (view.getTop() - marginTop <= 0) {
                            mFloatLayout.setY(0);
                        } else {
                            mFloatLayout.setY(-(mFloatLayoutHeight - view.getTop()));
                        }
                    } else {
                        mFloatLayout.setY(0);
                    }
                }
            } else if (isFloatLayoutShowing()) {
                mFloatLayout.setY(0);
            }
        }

        //当速度快到一定程度的时候这个position可能会有间隔。而间隔的条目在极端情况下可能是最后一个需要绑定悬浮数据的条目。
        int first = lm.findFirstVisibleItemPosition();
        if (mCurPosition != first) {
            int max = Math.max(mCurPosition, first);
            int min = Math.min(mCurPosition, first);
            //如果本次的位置和上一次的位置不是相邻的，那么就循环将跳过的位置进行更新。否则直接更新。
            if (max - min > 1) {
                if (dy < 0) {
                    for (int i = max - 1; i >= min; i--) {
                        updateFloatLayout(dy, i);
                    }
                } else {
                    for (int i = min + 1; i <= max; i++) {
                        updateFloatLayout(dy, i);
                    }
                }
            } else {
                updateFloatLayout(dy, first);
            }
            mCurPosition = first;
        }
    }

    @SuppressWarnings("unchecked")
    private void updateFloatLayout(int dy, int first) {
        ItemAdapter itemAdapter;
        if (dy < 0) {
            itemAdapter = getAdjacentChildAdapterByPosition(first + 1, false, true);
        } else {
            itemAdapter = getChildAdapterByPosition(first);
        }
        if (itemAdapter != null && itemAdapter.isFloatAble() && mLastBindPosition != itemAdapter.firstItemPosition) {
            if (dy < 0) {
                mFloatLayout.setY(-mFloatLayoutHeight);
            }
            mLastBindPosition = itemAdapter.firstItemPosition;
            itemAdapter.onBindFloatViewData(mFloatViewHelper, itemAdapter.firstItemPosition, getObject(itemAdapter.firstItemPosition));
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(ItemViewHolder<Object> holder, int position, List<Object> payloads) {
        if (holder instanceof LoadMoreViewHolder) {
            return;
        }
        ItemAdapter itemAdapter = mPool.acquireFromLayoutPosition(position);
        itemAdapter.onBindViewHolder(holder, position - itemAdapter.firstItemPosition, payloads);
        if (mFloatLayout != null && position == 0 && itemAdapter.isFloatAble()) {
            setFloatLayoutVisibility(true);
            itemAdapter.onBindFloatViewData(mFloatViewHelper, itemAdapter.firstItemPosition, getObject(itemAdapter.firstItemPosition));
        }
    }

    @Override
    public int getItemType(int position) {
        return mPool.acquireFromLayoutPosition(position).getRootViewType();
    }

    @Override
    protected int getItemSpanSize(int position) {
        ItemAdapter adapter = getChildAdapterByPosition(position);

        if (adapter == null) return getTotalSpanSize();

        int itemSpanSize = adapter.getItemSpanSize(position);
        return itemSpanSize == ItemAdapter.SPAN_SIZE_FULL_SCREEN || itemSpanSize > getTotalSpanSize() ? getTotalSpanSize() : itemSpanSize;
    }

    /**
     * 根据索引获取对应的子适配器。
     *
     * @param position 当前的索引位置。
     * @return 返回对应的适配器。
     */
    ItemAdapter getChildAdapterByPosition(int position) {
        return mPool.acquireFromLayoutPosition(position);
    }

    /**
     * 通过{@link RecyclerView}的Adapter的position获取{@link ItemAdapter}中对应的position。
     *
     * @param position 当前 {@link RecyclerView} 的position。
     */
    int getItemAdapterPosition(int position) {
        return position - mPool.acquireFromLayoutPosition(position).firstItemPosition;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean areItemsTheSame(Object oldItemData, Object newItemData) {
        if (oldItemData.getClass() != newItemData.getClass()) {
            return false;
        }
        ItemViewHolder viewHolder = getViewHolder(oldItemData.getClass());
        return viewHolder == null || viewHolder.areItemsTheSame(oldItemData, newItemData);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected boolean areContentsTheSame(Object oldItemData, Object newItemData) {
        if (oldItemData.getClass() != newItemData.getClass()) {
            return false;
        }
        ItemViewHolder viewHolder = getViewHolder(oldItemData.getClass());
        return viewHolder == null || viewHolder.areContentsTheSame(oldItemData, newItemData);
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void getChangePayload(Object oldItemData, Object newItemData, Bundle bundle) {
        if (oldItemData.getClass() != newItemData.getClass()) {
            return;
        }
        ItemViewHolder viewHolder = getViewHolder(oldItemData.getClass());
        if (viewHolder != null) {
            viewHolder.getChangePayload(oldItemData, newItemData, bundle);
        }
    }

    /**
     * 根据Holder的数据模型类型获取 {@link ItemViewHolder} 对象。
     *
     * @param holderModelClazz {@link ItemViewHolder} 中泛型指定的数据模型的字节码对象。
     * @return 返回 {@link ItemViewHolder} 对象。
     */
    private ItemViewHolder getViewHolder(Class<?> holderModelClazz) {
        //因为我省略了Adapter是不需要自己创建Adapter子类的，所以只能讲所有方法都封装到ViewHolder中。这里存ViewHolder也是迫不得已
        //暂时没有想到更好的方法去比较两个模型的相同与不同。
        return mItemViewHolderMap.get(holderModelClazz);
    }

    /**
     * 根据position获取相邻的子条目的Adapter。具体是获取前一个还是后一个由next参数决定。是否只获取可悬浮的由floatAble参数决定。
     *
     * @param position  当前的position索引。
     * @param next      是否是获取后一个，true表示获取后一个，false表示获取前一个。
     * @param floatAble 是否获取可悬浮的。
     */
    @CheckResult
    private ItemAdapter getAdjacentChildAdapterByPosition(int position, boolean next, boolean floatAble) {
        ItemAdapter adapter;
        ItemAdapter lastFloatAbleAdapter = null;
        boolean isContinue = false;
        for (int i = 0; i < mPool.size(); i++) {
            adapter = mPool.acquire(i);
            if (isContinue && adapter.isFloatAble()) {
                return adapter;
            }
            if (adapter.firstItemPosition <= position && adapter.lastItemPosition >= position) {
                if (floatAble) {
                    if (next) {
                        isContinue = true;
                        continue;
                    } else {
                        return lastFloatAbleAdapter;
                    }
                }
                if (next) {
                    if (i == mPool.size() - 1) {
                        return null;
                    } else {
                        return mPool.acquire(i + 1);
                    }
                } else {
                    if (i == 0) {
                        return null;
                    } else {
                        return mPool.acquire(i - 1);
                    }
                }
            } else {
                if (adapter.isFloatAble()) {
                    lastFloatAbleAdapter = adapter;
                }
            }
        }
        return null;
    }

    /**
     * 根据布局位置判断是否是第一个可悬浮的子Adapter。
     *
     * @param position 要判断的位置。
     */
    private boolean isFirstFloatAbleChildAdapter(int position) {
        ItemAdapter itemAdapter = mPool.acquireFirstFloatAbleChild();
        return itemAdapter != null && itemAdapter.firstItemPosition <= position && itemAdapter.lastItemPosition >= position;
    }

    private class ItemAdapterDataObserver extends ItemAdapter.AdapterDataObserver {

        @Override
        protected void add(int position, Object o, ItemAdapter adapter) {
            getDataList().add(position + adapter.firstItemPosition, o);
            getOldDataList().add(position + adapter.firstItemPosition, o);
            updateFirstAndLastPosition(adapter, 1, true);
        }

        @Override
        protected void addAll(int firstPosition, Collection dataList, ItemAdapter adapter) {
            boolean addAll = getDataList().addAll(firstPosition + adapter.firstItemPosition, dataList);
            boolean oldAddAll = getOldDataList().addAll(firstPosition + adapter.firstItemPosition, dataList);
            if (addAll && oldAddAll) {
                updateFirstAndLastPosition(adapter, dataList.size(), true);
            }
        }

        @Override
        protected void remove(Object o, ItemAdapter adapter) {
            boolean remove = getDataList().remove(o);
            boolean oldRemove = getOldDataList().remove(o);
            if (remove && oldRemove) {
                updateFirstAndLastPosition(adapter, 1, false);
            }
        }

        @Override
        protected void removeAll(Collection dataList, ItemAdapter adapter) {
            boolean removeAll = getDataList().removeAll(dataList);
            boolean oldRemoveAll = getOldDataList().removeAll(dataList);
            if (removeAll && oldRemoveAll) {
                updateFirstAndLastPosition(adapter, dataList.size(), false);
            }
        }

        private void updateFirstAndLastPosition(ItemAdapter current, int updateSize, boolean isAdd) {
            if (isAdd) {
                int index = mPool.indexOf(current) + 1;
                current.lastItemPosition += updateSize;
                for (int i = index; i < mPool.size(); i++) {
                    current = mPool.acquire(i);
                    current.firstItemPosition += updateSize;
                    current.lastItemPosition += updateSize;
                }
            } else {
                int index = mPool.indexOf(current);
                current.lastItemPosition -= updateSize;
                if (current.isEmptyList()) {
                    current.unregisterAll();
                    mPool.remove(current);
                } else {
                    index += 1;
                }
                for (int i = index; i < mPool.size(); i++) {
                    current = mPool.acquire(i);
                    current.firstItemPosition -= updateSize;
                    current.lastItemPosition -= updateSize;
                }
            }
        }
    }

    /**
     * 子Adapter的对象池。
     */
    private class ChildAdapterPool implements Pool<ItemAdapter> {

        /**
         * 用来存放所有的子条目对象。
         */
        private List<ItemAdapter> adapters = new ArrayList<>();


        /**
         * 通过位置获取对象。
         *
         * @param position 要获取的对象的位置。
         */
        @Override
        public ItemAdapter acquire(int position) {
            return adapters.get(position);
        }

        /**
         * 获取第一个可悬浮的子Adapter。
         */
        ItemAdapter acquireFirstFloatAbleChild() {
            for (ItemAdapter adapter : adapters) {
                if (adapter.isFloatAble()) {
                    return adapter;
                }
            }
            return null;
        }

        /**
         * 根据布局位置获取对象。
         *
         * @param layoutPosition 要获取的对象的布局位置。
         */
        ItemAdapter acquireFromLayoutPosition(int layoutPosition) {
            for (ItemAdapter adapter : adapters) {
                if (adapter.firstItemPosition <= layoutPosition && adapter.lastItemPosition >= layoutPosition) {
                    return adapter;
                }
            }
            throw new RuntimeException("ItemAdapter not found from layout position: " + layoutPosition);
        }

        @Override
        public void release(ItemAdapter instance) {
            adapters.add(instance);
        }

        /**
         * 获取对象的个数。
         */
        @Override
        public int size() {
            return adapters.size();
        }

        /**
         * 获取一个对象的位置。
         *
         * @param instance 要获取位置的对象。
         */
        @Override
        public int indexOf(ItemAdapter instance) {
            return adapters.indexOf(instance);
        }

        /**
         * 移除一个对象。
         *
         * @param instance 要移除的对象。
         */
        @Override
        public void remove(ItemAdapter instance) {
            adapters.remove(instance);
        }

        /**
         * 获取全部对象。
         */
        @Override
        public List<ItemAdapter> acquireAll() {
            return adapters;
        }
    }
}
