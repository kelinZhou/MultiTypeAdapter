package com.kelin.recycleradapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * 描述 用来描述加载更多的布局信息。
 * 创建人 kelin
 * 创建时间 2017/5/3  下午2:39
 * 版本 v 1.0.0
 */
final class LoadMoreLayoutManager {

    private static final int STATE_FAILED = 0x0000_00F0;
    private static final int STATE_NO_MORE = 0x0000_00F1;
    private static final int STATE_LOAD = 0x0000_00F2;
    private final int mLoadMoreOffset;

    private int mCurState = STATE_LOAD;
    /**
     * 加载更多时显示的布局文件ID。
     */
    private int mLoadMoreLayoutId;
    /**
     * 加载更多时显示的View。
     */
    private View mLoadMoreLayout;
    /**
     * 加载更多失败时显示的布局文件ID。
     */
    private int mRetryLayoutId;
    /**
     * 加载更多失败时显示的View。
     */
    private View mRetryLayout;
    /**
     * 没有更多数据时显示的布局文件ID。
     */
    private int mNoMoreDataLayoutId;
    /**
     * 没有更多数据时显示的View。
     */
    private View mNoMoreDataLayout;
    /**
     * 是否正在加载更多，通过此变量做判断，防止LoadMore重复触发。
     */
    private boolean mIsInTheLoadMore;
    /**
     * 加载更多是否可用。
     */
    private boolean mIsUsable = true;

    /**
     * 构建一个加载更多的布局信息对象。
     *
     * @param loadMoreLayoutId   加载更多时显示的布局的资源ID。
     * @param retryLayoutId      加载更多失败时显示的布局。
     * @param noMoreDataLayoutId 没有更多数据时显示的布局。
     * @param offset             加载更多触发位置的偏移值。偏移范围只能是1-10之间的数值。正常情况下是loadMoreLayout显示的时候就开始触发，
     *                           但如果设置了该值，例如：2，那么就是在loadMoreLayout之前的两个位置的时候开始触发。
     */
    LoadMoreLayoutManager(@LayoutRes int loadMoreLayoutId, @LayoutRes int retryLayoutId, @LayoutRes int noMoreDataLayoutId, @Size(min = 1, max = 10) int offset) {
        mLoadMoreLayoutId = loadMoreLayoutId;
        mRetryLayoutId = retryLayoutId;
        mNoMoreDataLayoutId = noMoreDataLayoutId;
        mLoadMoreOffset = offset < 0 ? 0 : offset > 10 ? 10 : offset;
    }

    void setInTheLoadMore(boolean isInTheLoadMore) {
        mIsInTheLoadMore = isInTheLoadMore;
    }

    /**
     * 判断是否正在加载中。
     */
    boolean isInTheLoadMore() {
        return mIsInTheLoadMore;
    }

    /**
     * 设置状态为加载失败，点击重试。
     */
    void setRetryState() {
        mCurState = STATE_FAILED;
        setVisible(mRetryLayout, mNoMoreDataLayout, mLoadMoreLayout);
    }

    /**
     * 判断是否是加载失败点击重试状态。
     *
     * @return 返回true表示是，false表示不是。
     */
    boolean isRetryState() {
        return mCurState == STATE_FAILED;
    }

    /**
     * 设置状态为没有更多数据。
     */
    void setNoMoreState() {
        setInTheLoadMore(false);
        mCurState = STATE_NO_MORE;
        setVisible(mNoMoreDataLayout, mRetryLayout, mLoadMoreLayout);
    }

    /**
     * 判断是否是没有更多数据状态。
     *
     * @return 返回true表示是，false表示不是。
     */
    boolean isNoMoreState() {
        return mCurState == STATE_NO_MORE;
    }

    /**
     * 设置状态为加载更多。
     */
    void setLoadState() {
        mCurState = STATE_LOAD;
        setVisible(mLoadMoreLayout, mRetryLayout, mNoMoreDataLayout);
    }

    private void setVisible(View visible, View... goneViews) {
        visible.setVisibility(View.VISIBLE);
        for (View view : goneViews) {
            view.setVisibility(View.GONE);
        }
    }

    /**
     * 判断是否为加载更多状态。
     *
     * @return 返回true表示是，false表示不是。
     */
    boolean isLoadState() {
        return mCurState == STATE_LOAD;
    }

    /**
     * 获取LoadMore条目布局ID。
     */
    @LayoutRes
    int getItemLayoutId() {
        return R.layout.com_kelin_zhou_item_load_more_layout;
    }

    /**
     * 获取某个状态下应当显示的View。
     *
     * @param layoutId 当前状态对应的LayoutId。如果当前的LayoutId不是当前状态下的Layout的ID则返回null。
     * @param parent   当前的RecyclerView对象。
     * @return 如果条件满足返回对应的View。否则返回null。
     */
    View getLayoutView(@LayoutRes int layoutId, @NonNull ViewGroup parent, View.OnClickListener retryClickListener) {
        if (layoutId == getItemLayoutId()) {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            ViewGroup contentView = (ViewGroup) inflater.inflate(layoutId, parent, false);
            mLoadMoreLayout = addView(mLoadMoreLayoutId, contentView, inflater, true, View.VISIBLE);
            mRetryLayout = addView(mRetryLayoutId, contentView, inflater, false, View.GONE);
            if (mRetryLayout != null) {
                mRetryLayout.setOnClickListener(retryClickListener);
            }
            mNoMoreDataLayout = addView(mNoMoreDataLayoutId, contentView, inflater, false, View.GONE);
            return contentView;
        } else {
            return null;
        }
    }

    /**
     * 添加一个布局视图到指定的父容器中。
     *
     * @param layoutId        要添加的布局视图的LayoutId。
     * @param parent          要指定的父容器。
     * @param inflater        填充器对象。
     * @param resetParentSize 是否根据被添加的视图的宽高重置父容器的宽高。
     * @param visibility      被添加的视图的显示状态。
     */
    private View addView(int layoutId, ViewGroup parent, LayoutInflater inflater, boolean resetParentSize, int visibility) {
        View contentView = inflater.inflate(layoutId, parent, false);
        if (resetParentSize) {
            ViewGroup.LayoutParams lp = contentView.getLayoutParams();
            ViewGroup.LayoutParams plp = parent.getLayoutParams();
            if (plp != null && lp != null) {
                plp.width = lp.width;
                plp.height = lp.height;
                parent.setLayoutParams(plp);
            }
        }
        parent.addView(contentView);
        contentView.setVisibility(visibility);
        return contentView;
    }

    /**
     * 判断是否没有正确的状态。
     *
     * @return 如果是返回true，否者返回false。
     */
    boolean noCurStateLayoutId() {
        int layoutId = 0;
        switch (mCurState) {
            case STATE_LOAD:
                layoutId = mLoadMoreLayoutId;
                break;
            case STATE_FAILED:
                layoutId = mRetryLayoutId;
                break;
            case STATE_NO_MORE:
                layoutId = mNoMoreDataLayoutId;
                break;
        }
        return layoutId == 0;
    }

    /**
     * 获取加载更多触发时机的偏移值。
     */
    int getLoadMoreOffset() {
        return mLoadMoreOffset;
    }

    /**
     * 设置加载更多是否可用。
     *
     * @param usable true表示可用，false表示不可用。默认为true。
     */
    void setLoadMoreUsable(boolean usable) {
        mIsUsable = usable;
    }

    /**
     * 判断加载更多是否可用。
     *
     * @return 返回true表示可用，false表示不可用。
     */
    boolean isUsable() {
        return mIsUsable;
    }
}
