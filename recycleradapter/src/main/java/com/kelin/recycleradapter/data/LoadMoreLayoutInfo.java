package com.kelin.recycleradapter.data;

import android.support.annotation.LayoutRes;

/**
 * 描述 用来描述加载更多的布局信息。
 * 创建人 kelin
 * 创建时间 2017/5/3  下午2:39
 * 版本 v 1.0.0
 */
public class LoadMoreLayoutInfo {

    private static final int STATE_FAILED = 0X0000_00F0;
    private static final int STATE_NO_MORE = 0X0000_00F1;
    private static final int STATE_LOAD = 0X0000_00F2;

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

    public LoadMoreLayoutInfo(@LayoutRes int loadMoreLayoutId, @LayoutRes int retryLayoutId, @LayoutRes int noMoreDataLayoutId) {
        mLoadMoreLayoutId = loadMoreLayoutId;
        mRetryLayoutId = retryLayoutId;
        mNoMoreDataLayoutId = noMoreDataLayoutId;
    }

    public void setRetryState() {
        mCurState = STATE_FAILED;
    }

    public boolean isRetryState() {
        return mCurState == STATE_FAILED;
    }

    public void setNoMoreState() {
        mCurState = STATE_NO_MORE;
    }

    public void setLoadState() {
        mCurState = STATE_LOAD;
    }

    public @LayoutRes int getCurStateLayoutId() {
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

    public boolean noCurStateLayoutId() {
        return getCurStateLayoutId() == 0;
    }
}
