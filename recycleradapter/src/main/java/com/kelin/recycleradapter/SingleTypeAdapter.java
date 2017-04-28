package com.kelin.recycleradapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.kelin.recycleradapter.holder.ItemViewHolder;

import java.util.List;

/**
 * 描述 {@link RecyclerView} 的适配器的基类。
 * 创建人 kelin
 * 创建时间 2016/11/28  上午10:09
 * 版本 v 1.0.0
 */

public class SingleTypeAdapter<D, H extends ItemViewHolder<D>> extends EditSuperAdapter<D, H> {

    /**
     * 当前的条目点击监听对象。
     */
    private OnItemEventListener<D, SingleTypeAdapter<D, H>> mItemEventListener;

    /**
     * 设置条目的事件监听。
     *
     * @param listener {@link OnItemEventListener} 对象。
     */
    public void setItemEventListener(@NonNull OnItemEventListener<D, SingleTypeAdapter<D, H>> listener) {
        mItemEventListener = listener;
        mItemEventListener.adapter = SingleTypeAdapter.this;
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
        super(list, holderClass);
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

    /**
     * 获取条目在Adapter中的位置。
     *
     * @param holder 当前的ViewHolder对象。
     */
    protected int getAdapterPosition(H holder) {
        return holder.getLayoutPosition();
    }

    @Override
    protected View.OnClickListener onGetClickListener(final H viewHolder) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemEventListener == null) return;
                int position = viewHolder.getLayoutPosition();
                D object = getItemObject(viewHolder);
                if (isHeader(position)) {
                    mItemEventListener.onItemHeaderClick();
                } else if (isFooter(position)) {
                    mItemEventListener.onItemFooterClick();
                }else if (v.getId() == viewHolder.itemView.getId() || v.getId() == viewHolder.getItemClickViewId()) {
                    mItemEventListener.onItemClick(position - getHeaderCount(), object);
                } else {
                    mItemEventListener.onItemChildClick(position - getHeaderCount(), object, v);
                }
            }
        };
    }

    @Override
    protected View.OnLongClickListener onGetLongClickListener(final H viewHolder) {
        return new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (mItemEventListener != null) {
                    mItemEventListener.onItemLongClick(viewHolder.getLayoutPosition(), getItemObject(viewHolder));
                }
                return true;
            }
        };
    }

    public static abstract class OnItemEventListener<D, A extends SingleTypeAdapter> {

        private A adapter;

        protected @NonNull A getAdapter() {
            return adapter;
        }

        /**
         * 当条目被点击的时候调用。
         *
         * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param d        被点击的条目的条目信息对象。
         */
        public abstract void onItemClick(int position, D d);

        /**
         * 当条目被长时点击的时候调用。
         *
         * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param d        被点击的条目的条目信息对象。
         */
        public void onItemLongClick(int position, D d){}

        /**
         * 当条目中的子控件被点击的时候调用。
         *
         * @param position 当前被点击的条目在 {@link RecyclerView} 中的索引。
         * @param d        被点击的条目的条目信息对象。
         * @param view 被点击的{@link View}。
         */
        public abstract void onItemChildClick(int position, D d, View view);

        /**
         * 当头ViewHolder被点击的时候调用。
         */
        public void onItemHeaderClick() {}

        /**
         * 当脚ViewHolder被点击的时候调用。
         */
        public void onItemFooterClick() {}
    }
}