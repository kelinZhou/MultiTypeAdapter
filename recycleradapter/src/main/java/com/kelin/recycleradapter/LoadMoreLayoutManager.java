package com.kelin.recycleradapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Size;
import android.util.SparseArray;
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

    private static final int STATE_FAILED = 0X0000_00F0;
    private static final int STATE_NO_MORE = 0X0000_00F1;
    private static final int STATE_LOAD = 0X0000_00F2;
    private final int mLoadMoreOffset;

    private int mCurState = STATE_LOAD;
    /**
     * 加载更多时显示的布局文件ID。
     */
    private int mLoadMoreLayoutId;
    /**
     * 加载更多失败时显示的布局文件ID。
     */
    private int mRetryLayoutId;
    /**
     * 没有更多数据时显示的布局文件ID。
     */
    private int mNoMoreDataLayoutId;
    /**
     * 是否正在加载更多，通过此变量做判断，防止LoadMore重复触发。
     */
    private boolean mIsInTheLoadMore;
    /**
     * 加载更多是否可用。
     */
    private boolean mIsUsable = true;
    private SparseArray<View> mLayoutViews = new SparseArray<>(3);

    /**
     * 构建一个加载更多的布局信息对象。
     * @param loadMoreLayoutId 加载更多时显示的布局的资源ID。
     * @param retryLayoutId 加载更多失败时显示的布局。
     * @param noMoreDataLayoutId 没有更多数据时显示的布局。
     * @param offset 加载更多触发位置的偏移值。偏移范围只能是1-10之间的数值。正常情况下是loadMoreLayout显示的时候就开始触发，
     *                       但如果设置了该值，例如：2，那么就是在loadMoreLayout之前的两个位置的时候开始触发。
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

    void setRetryState() {
        mCurState = STATE_FAILED;
    }

    boolean isRetryState() {
        return mCurState == STATE_FAILED;
    }

    void setNoMoreState() {
        setInTheLoadMore(false);
        mCurState = STATE_NO_MORE;
    }

    boolean isNoMoreState() {
        return mCurState == STATE_NO_MORE;
    }

    void setLoadState() {
        mCurState = STATE_LOAD;
    }

    boolean isLoadState() {
        return mCurState == STATE_LOAD;
    }

    @LayoutRes int getCurStateLayoutId() {
        switch (mCurState) {
            case STATE_LOAD:
                return mLoadMoreLayoutId;
            case STATE_FAILED:
                return mRetryLayoutId;
            case STATE_NO_MORE:
                return mNoMoreDataLayoutId;
            default:
                throw new RuntimeException("the current state is unknown!");
        }
    }

    View getLayoutView(@LayoutRes int layoutId, @NonNull ViewGroup parent) {
        if (layoutId == getCurStateLayoutId()) {
            View view = mLayoutViews.get(layoutId);
            if (view == null) {
                view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
                if (view == null) {
                    return null;
                }
                mLayoutViews.put(layoutId, view);
            }
            return view;
        } else {
            return null;
        }
    }

    boolean noCurStateLayoutId() {
        return getCurStateLayoutId() == 0;
    }

    int getLoadMoreOffset() {
        return mLoadMoreOffset;
    }

    void setLoadMoreUsable(boolean usable) {
        mIsUsable = usable;
    }

    boolean isUsable() {
        return mIsUsable;
    }
}
