package com.example.recycleradapter;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.ViewGroup;

import com.example.recycleradapter.holder.HeaderFooterViewHolder;
import com.example.recycleradapter.holder.ItemLayout;
import com.example.recycleradapter.holder.ItemViewHolder;

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
    private static final int KEY_ITEM_MODEL = 0X0000_0010<<24;
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
     * 用来记录当前的条目类型。
     */
    private int mItemType;
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

    public ItemAdapter(@Size(min = 1) int itemType, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(itemType, SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(int itemType, @Size(min = 1) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(null, itemType, spanSize, holderClass);
    }

    public ItemAdapter(List<D> list, @Size(min = 0) int itemType, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        this(list, itemType, SPAN_SIZE_FULL_SCREEN, holderClass);
    }

    public ItemAdapter(List<D> list, @Size(min = 0) int itemType, @Size(min = 1) int spanSize, @NonNull Class<? extends ItemViewHolder<D>> holderClass) {
        super(list, holderClass);
        mItemType = itemType;
        mSpanSize = spanSize <= 0 ? SPAN_SIZE_FULL_SCREEN : spanSize;
        ItemLayout annotation = holderClass.getAnnotation(ItemLayout.class);
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
        return mItemType;
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

    int getHeaderItemViewType() {
        return haveHeader() ? getItemViewType() + 1 << 18 : getItemViewType();
    }

    ItemViewHolder<D> onCreateHeaderViewHolder(ViewGroup parent) {
        return createHolderForType(parent, HEADER_HOLDER);
    }

    ItemViewHolder<D> onCreateFooterViewHolder(ViewGroup parent) {
        return createHolderForType(parent, FOOTER_HOLDER);
    }

    private ItemViewHolder<D> createHolderForType(ViewGroup parent, int type) {
        ItemLayout annotation = mHolderClass.getAnnotation(ItemLayout.class);
        if (annotation != null) {
            ItemViewHolder viewHolder = null;
            switch (type) {
                case HEADER_HOLDER:
                    try {
                        Constructor<? extends ItemViewHolder> constructor = HeaderFooterViewHolder.class.getConstructor(ViewGroup.class, int.class);
                        viewHolder = constructor.newInstance(parent, annotation.headerLayoutId());
                        HeaderFooterViewHolder holder = (HeaderFooterViewHolder) viewHolder;
                        holder.setHeaderType();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                    break;
                case FOOTER_HOLDER:
                    try {
                        Constructor<? extends ItemViewHolder> constructor = HeaderFooterViewHolder.class.getConstructor(ViewGroup.class, int.class);
                        viewHolder = constructor.newInstance(parent, annotation.footerLayoutId());
                        HeaderFooterViewHolder holder = (HeaderFooterViewHolder) viewHolder;
                        holder.setFooterType();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
            }
            if (viewHolder != null) {
                bindItemClickEvent(viewHolder);
                return viewHolder;

            } else {
                throw new RuntimeException("view holder's root layout is not found! You must use \"@ItemLayout(rootLayoutId = @LayoutRes int)\" notes on your ItemViewHolder class");
            }
        }
        return null;
    }

    int getFooterItemViewType() {
        return haveFooter() ? getItemViewType() + 1 << 18 - 1 : getItemViewType();
    }

    int getHeaderCount() {
        return haveHeader() ? 1 : 0;
    }

    int getFooterCount() {
        return haveFooter() ? 1 : 0;
    }
}
