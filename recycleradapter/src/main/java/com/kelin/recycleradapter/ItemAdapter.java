package com.kelin.recycleradapter;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.ViewGroup;

import com.kelin.recycleradapter.holder.HeaderFooterViewHolder;
import com.kelin.recycleradapter.holder.ItemLayout;
import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.lang.reflect.Constructor;
import java.util.List;

/**
 * 描述 条目适配器 {@link MultiTypeAdapter} 的子适配器。
 * 创建人 kelin
 * 创建时间 2017/1/19  下午4:22
 * 版本 v 1.0.0
 */

public class ItemAdapter<D> extends SingleTypeAdapter<D, ItemViewHolder<D>> {

    /**
     * 用来从ViewHolder中获取数据模型的键。
     */
    private static final int KEY_ITEM_MODEL = 0X0000_0010 << 24;
    /**
     * 表示当前的条目是占满屏幕的。
     */
    static final int SPAN_SIZE_FULL_SCREEN = 0x0000_0010;
    /**
     * 表示创建头Holder。
     */
    private static final int HEADER_HOLDER = 0X0000_0101;
    /**
     * 表示创建脚Holder。
     */
    private static final int FOOTER_HOLDER = 0X0000_0102;
    /**
     * 用来记录当前条目的占屏比。
     */
    private int mSpanSize;
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
     * 用来记录头布局的资源文件ID。
     */
    private int mHeaderLayoutId;
    /**
     * 用来记录脚布局的资源文件ID。
     */
    private int mFooterLayoutId;
    private int mRootLayoutId;

    public ItemAdapter(@NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(@Size(min = 1) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(null, spanSize, holderClass);
    }

    public ItemAdapter(List<D> list, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(list, SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(List<D> list, @Size(min = 1) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        super(list, holderClass);
        mSpanSize = spanSize <= 0 ? SPAN_SIZE_FULL_SCREEN : spanSize;
        ItemLayout annotation = holderClass.getAnnotation(ItemLayout.class);
        mRootLayoutId = annotation.rootLayoutId();
        mHeaderLayoutId = annotation.headerLayoutId();
        mFooterLayoutId = annotation.footerLayoutId();
    }

    /**
     * 是否拥有Header。
     */
    boolean haveHeader() {
        return mHeaderLayoutId != ItemLayout.NOT_HEADER_FOOTER;
    }

    /**
     * 是否拥有Footer。
     */
    boolean haveFooter() {
        return mFooterLayoutId != ItemLayout.NOT_HEADER_FOOTER;
    }

    int getItemViewType() {
        return mRootLayoutId;
    }

    int getHeaderItemViewType() {
        return mHeaderLayoutId;
    }

    int getFooterItemViewType() {
        return mFooterLayoutId;
    }

    int getItemSpanSize() {
        return mSpanSize;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount() + getHeaderAndFooterCount();
    }

    @Override
    public void notifyRefresh() {
        //因为子Adapter不是RecyclerView的Adapter，所以这里是调用父级Adapter的刷新。
        if (mParentAdapter != null) {
            mParentAdapter.notifyRefresh();
        }
    }

    @Override
    public void onBindViewHolder(ItemViewHolder<D> holder, int position, List<Object> payloads) {
        D object = getObject(position - getHeaderCount());
        holder.itemView.setTag(KEY_ITEM_MODEL, object);
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    protected D getItemObject(ItemViewHolder<D> holder) {
        return (D) holder.itemView.getTag(KEY_ITEM_MODEL);
    }

    @Override
    protected int getAdapterPosition(ItemViewHolder<D> holder) {
        return mParentAdapter.getItemAdapterPosition(holder.getLayoutPosition());
    }

    void setParent(MultiTypeAdapter parent) {
        mParentAdapter = parent;
    }

    Class<?> getItemModelClass() {
        return isEmptyList() ? null : getObject(0).getClass();
    }

    /**
     * 获取头和脚的数量。
     */
    private int getHeaderAndFooterCount() {
        return getHeaderCount() + getFooterCount();
    }

    ItemViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        return createHolderForType(parent, viewType, HEADER_HOLDER);
    }

    ItemViewHolder onCreateFooterViewHolder(ViewGroup parent, int viewType) {
        return createHolderForType(parent, viewType, FOOTER_HOLDER);
    }

    private ItemViewHolder createHolderForType(ViewGroup parent, int viewType, int type) {
        ItemViewHolder viewHolder;
        HeaderFooterViewHolder holder;
        try {
            Constructor<? extends ItemViewHolder> constructor = HeaderFooterViewHolder.class.getConstructor(ViewGroup.class, int.class);
            viewHolder = constructor.newInstance(parent, viewType);
            holder = (HeaderFooterViewHolder) viewHolder;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        switch (type) {
            case HEADER_HOLDER:
                holder.setHeaderType();
                break;
            case FOOTER_HOLDER:
                holder.setFooterType();
        }
        if (viewHolder != null) {
            bindItemClickEvent(viewHolder);
            return viewHolder;

        } else {
            throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
        }
    }

    int getHeaderCount() {
        return haveHeader() ? 1 : 0;
    }

    int getFooterCount() {
        return haveFooter() ? 1 : 0;
    }
}
