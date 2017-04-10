package com.kelin.recycleradapter;

import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
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

public class ItemAdapter<D> extends EditableSupperAdapter<D, ItemViewHolder<D>> {

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
    private OnItemEventListener<D> mItemEventListener;

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
     * 设置条目的事件监听。
     *
     * @param listener {@link SingleTypeAdapter.OnItemEventListener} 对象。
     */
    public void setItemEventListener(@NonNull OnItemEventListener<D> listener) {
        mItemEventListener = listener;
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
    protected View.OnClickListener onGetClickListener(final ItemViewHolder<D> viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemEventListener == null) return;
                mItemEventListener.adapter = mParentAdapter.getChildAdapterByPosition(viewHolder.getLayoutPosition());
                int position = viewHolder.getLayoutPosition();
                int adapterPosition = getAdapterPosition(viewHolder);
                if (viewHolder instanceof HeaderFooterViewHolder) {
                    HeaderFooterViewHolder holder = (HeaderFooterViewHolder) viewHolder;
                    if (holder.isHeader()){
                        mItemEventListener.onItemHeaderClick(position);
                    } else if (holder.isFooter()) {
                        mItemEventListener.onItemFooterClick(position, adapterPosition);
                    }
                    return;
                }

                D object = getItemObject(viewHolder);
                if (v.getId() == viewHolder.itemView.getId() || v.getId() == viewHolder.getItemClickViewId()) {
                    mItemEventListener.onItemClick(position, object, adapterPosition);
                } else {
                    mItemEventListener.onItemChildClick(position, object, v, adapterPosition);
                }
            }
        };
    }

    protected int getAdapterPosition(ItemViewHolder<D> holder) {
        return mParentAdapter.getItemAdapterPosition(holder.getLayoutPosition()) - getHeaderCount();
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
            Constructor<? extends ItemViewHolder> constructor = HeaderFooterViewHolder.class.getDeclaredConstructor(ViewGroup.class, int.class);
            constructor.setAccessible(true);
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

    private int getFooterCount() {
        return haveFooter() ? 1 : 0;
    }

    @Override
    protected void parentNotifyItemInserted(int position) {
        Log.i("ItemAdapter", "parentNotifyItemInserted/position=" + position);
        if (mParentAdapter != null)
            mParentAdapter.notifyItemInserted(position + firstItemPosition + getHeaderCount());
    }

    @Override
    protected void parentNotifyItemRangeInserted(int positionStart, int itemCount) {
        Log.i("ItemAdapter", "parentNotifyItemRangeInserted/positionStart=" + positionStart + " | itemCount=" + itemCount);
        if (mParentAdapter != null)
            mParentAdapter.notifyItemRangeInserted(positionStart + firstItemPosition + getHeaderCount(), itemCount);
    }

    @Override
    protected void parentNotifyItemRemoved(int position) {
        Log.i("ItemAdapter", "parentNotifyItemRemoved/position=" + position);
        if (mParentAdapter != null) {
            if (!isEmptyList()) {
                mParentAdapter.notifyItemRemoved(position + firstItemPosition + getHeaderCount());
            } else {
                if (haveHeader() || haveFooter()) {
                   mParentAdapter.notifyItemRangeRemoved(firstItemPosition, getHeaderCount() + getFooterCount() + 1);
                } else {
                    mParentAdapter.notifyItemRemoved(position + firstItemPosition);
                }
            }
        }
    }

    @Override
    protected void parentNotifyItemRangeRemoved(int positionStart, int itemCount) {
        Log.i("ItemAdapter", "parentNotifyItemRangeRemoved/positionStart=" + positionStart + " | itemCount=" + itemCount);
        if (isEmptyList()) {
            positionStart = firstItemPosition;
            if (haveHeader()) {
                itemCount++;
            }
            if (haveFooter()) {
                itemCount++;
            }
        } else {
            positionStart += firstItemPosition + getHeaderCount();
        }
        if (mParentAdapter != null)
            mParentAdapter.notifyItemRangeRemoved(positionStart, itemCount);
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

        /**
         * 当头ViewHolder被点击的时候调用。
         * <P>如果当前Item事件监听是设置给 {@link SingleTypeAdapter} 的，
         * 那么重写该方法是没有意义的，因为永远不会被调用。
         *
         * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
         */
        public void onItemHeaderClick(int position) {
        }

        /**
         * 当脚ViewHolder被点击的时候调用。
         * <P>如果当前Item事件监听是设置给 {@link SingleTypeAdapter} 的，
         * 那么重写该方法是没有意义的，因为永远不会被调用。
         *
         * @param position        当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param adapterPosition 当前点击的条目在当前子 Adapter 中的位置。
         */
        public void onItemFooterClick(int position, int adapterPosition) {
        }
    }
}
