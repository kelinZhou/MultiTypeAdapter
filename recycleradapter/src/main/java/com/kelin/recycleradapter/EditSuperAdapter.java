package com.kelin.recycleradapter;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.interfaces.AdapterEdit;
import com.kelin.recycleradapter.interfaces.Orientation;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * 描述 描述了可以编辑列表数据的适配器。
 * 创建人 kelin
 * 创建时间 2017/4/6  下午1:36
 * 版本 v 1.0.0
 */

public abstract class EditSuperAdapter<D, VH extends ItemViewHolder<D>> extends SuperAdapter<D, VH> implements AdapterEdit<D, VH> {

    /**
     * 当前适配器中的ViewHolder对象。
     */
    private VH mViewHolder;
    /**
     * 用来存放ViewHolder的字节码对象。
     */
    private Class<? extends VH> mHolderClass;
    /**
     * 用来记录头布局的资源文件ID。
     */
    private @LayoutRes int mHeaderLayoutId;
    /**
     * 用来记录脚布局的资源文件ID。
     */
    private @LayoutRes int mFooterLayoutId;
    /**
     * 用来记录当前适配器中的布局资源ID。
     */
    private @LayoutRes int mRootLayoutId;
    private int mItemSpanSize;

    public EditSuperAdapter(@NonNull RecyclerView recyclerView, List<D> list, Class<? extends VH> holderClass) {
        this(recyclerView, 1, 1, list, holderClass);
    }

    public EditSuperAdapter(@NonNull RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize, @Size(min = 1, max = 100) int spanSize, List<D> list, Class<? extends VH> holderClass) {
        this(recyclerView, totalSpanSize, spanSize, LinearLayout.VERTICAL, list, holderClass);
    }

    public EditSuperAdapter(@NonNull RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize, @Size(min = 1, max = 100) int spanSize, @Orientation int orientation, List<D> list, Class<? extends VH> holderClass) {
        super(recyclerView, totalSpanSize, orientation);
        if (holderClass == null) {
            throw new RuntimeException("you mast set holderClass and not null object");
        }
        mItemSpanSize = spanSize <= 0 ? SPAN_SIZE_FULL_SCREEN : spanSize;
        mHolderClass = holderClass;
        ItemLayout annotation = holderClass.getAnnotation(ItemLayout.class);
        if (annotation != null) {
            mRootLayoutId = annotation.rootLayoutId();
            mHeaderLayoutId = annotation.headerLayoutId();
            mFooterLayoutId = annotation.footerLayoutId();
        } else {
            throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
        }

        setDataList(list);
    }

    /**
     * 是否拥有Header。
     */
    @Override
    public boolean haveHeader() {
        return mHeaderLayoutId != ItemLayout.NOT_HEADER_FOOTER;
    }

    /**
     * 是否拥有Footer。
     */
    @Override
    public boolean haveFooter() {
        return mFooterLayoutId != ItemLayout.NOT_HEADER_FOOTER;
    }

    /**
     * 获取条目类型。
     *
     * @return 返回跟布局的资源ID。
     */
    @Override
    public int getRootViewType() {
        return mRootLayoutId;
    }

    /**
     * 获取Header条目类型。
     *
     * @return 返回Header布局的资源ID。
     */
    @Override
    public int getHeaderViewType() {
        return mHeaderLayoutId;
    }

    /**
     * 获取Footer条目类型。
     *
     * @return 返回Footer布局的资源ID。
     */
    @Override
    public int getFooterViewType() {
        return mFooterLayoutId;
    }

    /**
     * 获取Header的数量。
     *
     * @return 如果有Header则返回1，否则返回0。
     */
    @Override
    public int getHeaderCount() {
        return haveHeader() ? 1 : 0;
    }

    /**
     * 获取Footer的数量。
     *
     * @return 如果有Footer则返回1，否则返回0。
     */
    @Override
    public int getFooterCount() {
        return haveFooter() ? 1 : 0;
    }

    /**
     * 获取头和脚的数量。
     */
    @Override
    public int getHeaderAndFooterCount() {
        return getHeaderCount() + getFooterCount();
    }

    /**
     * 判断当前Position是否是头。
     * @param position 要判断的position。
     */
    boolean isHeader(int position) {
        return position == 0 && haveHeader();
    }

    /**
     * 判断当前Position是否是脚。
     * @param position 要判断的position。
     */
    boolean isFooter(int position) {
        return position == getItemCount() - (mLoadMoreLayoutInfo != null ? 1 : 2) && haveFooter();
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
        clickView.setOnLongClickListener(onGetLongClickListener(viewHolder));
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
        holder.onBindPartData(position, getObject(holder.getLayoutPosition() - getHeaderCount()), payloads);
    }

    @Override
    public int getItemType(int position) {
        if (isHeader(position)) {
            return getHeaderViewType();
        } else if (isFooter(position)) {
            return getFooterViewType();
        } else {
            return getRootViewType();
        }
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + getHeaderAndFooterCount();
    }

    @Override
    protected int getItemSpan(int position) {
        if ((mLoadMoreLayoutInfo != null && position == getItemCount() - 1)
                || isHeader(position)
                || isFooter(position)) return getTotalSpanSize();
        return getItemSpanSize(position);
    }

    @Override
    public int getItemSpanSize(int position) {
        return mItemSpanSize;
    }

    /**
     * 当需要给ViewHolder绑定事件的时候调用。
     * @param viewHolder 当前要绑定事件的ViewHolder对象。
     */
    protected abstract View.OnClickListener onGetClickListener(VH viewHolder);

    protected abstract View.OnLongClickListener onGetLongClickListener(VH viewHolder);


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
        if (refresh) {
            notifyItemInserted(position + getHeaderCount());
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
            if (refresh) {
                notifyItemRangeInserted(positionStart + getHeaderCount(), datum.size());
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
                if (refresh) {
                    notifyItemRemoved(position + getHeaderCount());
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
                    if (refresh) {
                        notifyItemRemoved(position + getHeaderCount());
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
        if (positionStart < 0 || itemCount < 0) throw new IllegalArgumentException("the positionStart Arguments or itemCount Arguments must is greater than 0 integer");
        if (!isEmptyList()) {
            List<D> dataList = getDataList();
            int positionEnd = positionStart + (itemCount = itemCount > dataList.size() ? dataList.size() : itemCount);
            List<D> temp = new ArrayList<>();
            for (int i = positionStart; i < positionEnd; i++) {
                temp.add(dataList.get(i));
            }
            boolean removeAll = dataList.removeAll(temp);
            if (removeAll) {
                if (refresh) {
                    notifyItemRangeRemoved(positionStart + getHeaderCount(), itemCount);
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
                if (refresh) {
                    notifyItemRangeRemoved(positionStart + getHeaderCount(), datum.size());
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
            if (refresh) {
                notifyItemRangeRemoved(getHeaderCount(), dataList.size());
            }
        }
    }
}
