package com.kelin.recycleradapter;

import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.Size;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.kelin.recycleradapter.holder.ItemViewHolder;
import com.kelin.recycleradapter.interfaces.Orientation;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述 {@link android.support.v7.widget.RecyclerView.Adapter} 的基类。
 * 创建人 kelin
 * 创建时间 2017/3/28  下午12:42
 * 版本 v 1.0.0
 */

abstract class SuperAdapter<D, VH extends ItemViewHolder<D>> extends RecyclerView.Adapter<VH> {

    protected static final String HEADER_DATA_FLAG = "com.kelin.recycleradapter.header_data_flag";
    protected static final String FOOTER_DATA_FLAG = "com.kelin.recycleradapter.footer_data_flag";
    /**
     * 当前页面的数据集。
     */
    private List<D> mDataList;
    /**
     * 用来存页面数据集的副本。
     */
    private List<D> mTempList;
    /**
     * 当需要刷新列表时，用来比较两此数据不同的回调。
     */
    private DiffUtil.Callback mDiffUtilCallback;
    /**
     * 与当前适配器绑定的 {@link RecyclerView} 对象。
     */
    private RecyclerView mRecyclerView;
    /**
     * 当前 {@link RecyclerView} 的宽度被均分成的份数。
     */
    private int mTotalSpanSize;
    private LinearLayoutManager mLm;
    /**
     * 加载更多时显示的布局文件ID。
     */
    int mLoadMoreViewId;
    /**
     * 加载更多的回调。
     */
    private MultiTypeAdapter.LoadMoreCallback mLoadMoreCallback;
    /**
     * 是否正在加载更多，通过此变量做判断，防止LoadMore重复触发。
     */
    private boolean mIsInTheLoadMore;

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView 您要绑定的 {@link RecyclerView} 对象。
     */
    public SuperAdapter(@NonNull RecyclerView recyclerView) {
        this(recyclerView, 1);
    }

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView 您要绑定的 {@link RecyclerView} 对象。
     * @param totalSpanSize 总的占屏比，通俗来讲就是 {@link RecyclerView} 的宽度被均分成了多少份。该值的范围是1~100之间的数(包含)。
     *
     */
    public SuperAdapter(@NonNull RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize) {
        this(recyclerView, totalSpanSize, LinearLayout.VERTICAL);
    }

    /**
     * 构造方法。
     * <P>初始化适配器并设置布局管理器，您不许要再对 {@link RecyclerView} 设置布局管理器。
     * <p>例如：{@link RecyclerView#setLayoutManager(RecyclerView.LayoutManager)} 方法不应该在被调用，否者可能会出现您不希望看到的效果。
     *
     * @param recyclerView  您要绑定的 {@link RecyclerView} 对象。
     * @param totalSpanSize 总的占屏比，通俗来讲就是 {@link RecyclerView} 的宽度被均分成了多少份。该值的范围是1~100之间的数(包含)。
     * @param orientation 列表的方向，该参数的值只能是{@link LinearLayout#HORIZONTAL} or {@link LinearLayout#VERTICAL}的其中一个。
     */
    public SuperAdapter(RecyclerView recyclerView, @Size(min = 1, max = 100) int totalSpanSize, @Orientation int orientation) {
        if (totalSpanSize < 1 || totalSpanSize > 100) {
            throw new RuntimeException("the totalSpanSize argument must be an integer greater than zero and less than 1000");
        }
        mTotalSpanSize = totalSpanSize;
        if (recyclerView != null) {
            mRecyclerView = recyclerView;
            initLayoutManager(recyclerView, orientation, mTotalSpanSize);
        }
        mDiffUtilCallback = new DiffUtil.Callback() {
            @Override
            public int getOldListSize() {
                return mTempList.size();
            }

            @Override
            public int getNewListSize() {
                return mDataList.size();
            }

            // 判断是否是同一个 item
            @Override
            public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                return oldObject == newObject || SuperAdapter.this.areItemsTheSame(oldObject, newObject);
            }

            // 如果是同一个 item 判断内容是否相同
            @Override
            public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                return oldObject == newObject || SuperAdapter.this.areContentsTheSame(oldObject, newObject);
            }

            @Nullable
            @Override
            public Bundle getChangePayload(int oldItemPosition, int newItemPosition) {
                D oldObject = getOldObject(oldItemPosition);
                D newObject = getObject(newItemPosition);
                if (oldObject == newObject) return null;
                Bundle bundle = new Bundle();
                SuperAdapter.this.getChangePayload(oldObject, newObject, bundle);
                return bundle.size() == 0 ? null : bundle;
            }
        };
    }

    /**
     * 初始化布局管理器。并设置给 {@link RecyclerView}。
     */
    protected void initLayoutManager(@NonNull RecyclerView recyclerView, @Orientation int orientation, int totalSpanSize) {
        if (totalSpanSize > 1) {
            GridLayoutManager lm = new GridLayoutManager(recyclerView.getContext(), totalSpanSize, orientation, false)
            {
                @Override
                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    try {
                        super.onLayoutChildren(recycler, state);
                    } catch (Exception ignored) {}
                }
            };
            lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return getItemSpan(position);
                }
            });
            mLm = lm;
        } else {
            mLm = new LinearLayoutManager(recyclerView.getContext(), orientation, false) {
                @Override
                public void onLayoutChildren(RecyclerView.Recycler recycler, RecyclerView.State state) {
                    try {
                        super.onLayoutChildren(recycler, state);
                    } catch (Exception ignored) {}
                }
            };
        }
        recyclerView.setLayoutManager(mLm);
    }

    /**
     * 获取总的占屏比，通俗来讲就是获取 {@link RecyclerView} 的宽度被均分成了多少份。
     *
     * @return 总的份数。
     */
    int getTotalSpanSize() {
        return mTotalSpanSize;
    }

    /**
     * 根据位置获取条目的占屏值。
     *
     * @param position 当前的位置。
     * @return 返回当前条目的占屏值。
     */
    protected int getItemSpan(int position) {
        if ((mLoadMoreViewId != 0 && position == getItemCount() - 1)
                || HEADER_DATA_FLAG.equals(getObject(position))
                || FOOTER_DATA_FLAG.equals(getObject(position))) return getTotalSpanSize();
        return getItemSpanSize(position);
    }

    protected abstract int getItemSpanSize(int position);

    /**
     * 设置加载更多时显示的布局。
     * @param loadMoreViewId 加载更多时显示的布局的资源ID。
     * @param callback 加载更多的回调。
     */
    public void setLoadMoreViewId(@LayoutRes int loadMoreViewId, @NonNull MultiTypeAdapter.LoadMoreCallback callback) {
        mLoadMoreViewId = loadMoreViewId;
        mLoadMoreCallback = callback;
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (mIsInTheLoadMore || mLoadMoreViewId == 0) return;
                int lastVisibleItemPosition = mLm.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == getDataList().size()) {
                    Log.i("MultiTypeAdapter", "开始加载更多");
                    mLoadMoreCallback.OnLoadMore();
                    mIsInTheLoadMore = true;
                }
            }
        });
    }

    /**
     * 当加载更多完成后要调用此方法，否则不会触发下一次LoadMore事件。
     */
    public void setLoadMoreFinished() {
        mIsInTheLoadMore = false;
        Log.i("MultiTypeAdapter", "加载完成");
    }

    /**
     * 如果你的页面已经没有更多数据可以加载了的话，应当调用此方法。调用了此方法后就不会再触发LoadMore事件，否则还会触发。
     */
    public void setNoMoreData() {
        mLoadMoreViewId = 0;
    }

    @Override
    public int getItemCount() {
        return getDataList().size() + (mLoadMoreViewId == 0 ? 0 : 1);
    }

    @Override
    public final int getItemViewType(int position) {
        if (mLoadMoreViewId != 0 && position == getItemCount() - 1) return mLoadMoreViewId;
        return getItemType(position);
    }

    protected abstract int getItemType(int position);

    @Override
    public final void onBindViewHolder(VH holder, int position) {}

    @Override
    public void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        holder.onBindPartData(position, getObject(position), payloads);
    }

    /**
     * 判断两个位置的Item是否相同。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @return 相同返回true，不同返回false。
     */
    protected abstract boolean areItemsTheSame(D oldItemData, D newItemData);

    /**
     * 判断两个位置的Item的内容是否相同。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @return 相同返回true，不同返回false。
     */
    protected abstract boolean areContentsTheSame(D oldItemData, D newItemData);

    /**
     * 获取两个位置的Item的内容不同之处。
     * @param oldItemData 旧的Item数据。
     * @param newItemData 新的Item数据。
     * @param bundle 将不同的内容存放到该参数中。
     */
    protected abstract void getChangePayload(D oldItemData, D newItemData, Bundle bundle);
    /**
     * 刷新RecyclerView。
     */
    public void notifyRefresh() {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(mDiffUtilCallback);
        diffResult.dispatchUpdatesTo(this);
        // 通知刷新了之后，要更新副本数据到最新
        mTempList.clear();
        mTempList.addAll(mDataList);
    }

    /**
     * 设置数据。
     *
     * @param list 数据集合。
     */
    public void setDataList(List<D> list) {
        mDataList = list != null ? list : new ArrayList<D>();
        mTempList = new ArrayList<>(mDataList);
    }

    /**
     * 设置数据。
     *
     * @param list 数据集合。
     */
    void addDataList(List<D> list) {
        getDataList().addAll(list);
        mTempList.addAll(list);
    }

    /**
     * 设置数据。
     *
     * @param d 数据集合。
     */
    void addData(D d) {
        getDataList().add(d);
        mTempList.add(d);
    }

    /**
     * 获取当前的数据集合。
     */
    List<D> getDataList() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
            mTempList = new ArrayList<>();
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

    /**
     * 获取指定位置的对象。
     *
     * @param position 要获取对象对应的条目索引。
     * @return 返回 {@link D} 对象。
     */
    D getOldObject(int position) {
        if (mTempList.size() > position && position >= 0) {
            return mTempList.get(position);
        }
        return null;
    }
}
