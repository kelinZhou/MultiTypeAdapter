package com.kelin.recycleradapter.holder;

import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.DrawableRes;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.annotation.StringRes;
import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kelin.recycleradapter.callback.NotifyCallback;
import com.kelin.recycleradapter.interfaces.ViewOperation;

import java.util.List;

/**
 * 描述 {@link RecyclerView} 中的条目的 {@link RecyclerView.ViewHolder} 对象。
 * 创建人 kelin
 * 创建时间 2017/1/19  下午12:15
 * 版本 v 1.0.0
 */
public abstract class ItemViewHolder<D> extends RecyclerView.ViewHolder implements NotifyCallback<D>, ViewOperation {

    private final SparseArray<View> mViews;

    protected ItemViewHolder(ViewGroup parent, @LayoutRes int itemRootViewId) {
        super(LayoutInflater.from(parent.getContext()).inflate(itemRootViewId, parent, false));

        mViews = new SparseArray<>();
        initHolder(itemView);
    }

    public @IdRes int[] onGetNeedListenerChildViewIds(){
        return null;
    }

    /**
     * 初始化ViewHolder。
     *
     * @param itemView 当前Holder的布局View。
     */
    protected abstract void initHolder(View itemView);

    /**
     * 绑定数据的时候调用。
     *
     * @param position 当前的Item索引。
     * @param d        当前索引对应的数据对象。
     */
    public abstract void onBindData(int position, D d);

    /**
     * 绑定数据的时候调用。与 {@link #onBindData(int, Object)} 方法不用的是，这个方法可以用来局部绑定数据。
     * 就是说当一个条目中的数据模型发生变化以后但不是展示在UI上的每个字段都发生了变化的情况下就可以通过该方法只针对部分改动过的字段进行数据绑定。
     * <p>但是该方法也不是必然就会被执行的，而是在 {@link NotifyCallback#getChangePayload(Object, Object, Bundle)} 方法中获得了不同之处后才会被执行。
     * <p>该方法的默认实现是调用 {@link #onBindData(int, Object)} 方法，如果你希望通过局部刷新来提高效率则重写该方法。
     * @param position 当前的Item索引。
     * @param d        当前索引对应的数据对象。
     * @param payloads 数据模型被改变的内容。
     */
    public void onBindPartData(int position, D d, List<Object> payloads){
        onBindData(position, d);
    }

    /**
     * 如果你不希望通过点击{@link #itemView}触发条目点击事件，而是希望通过点击自己自定的控件触发条目点击事件，则需要重写该方法。
     * <p>如果你重写了该方法并返回的ViewId包含在 {@link #onGetNeedListenerChildViewIds()} 返回的数组里，则条目点击事件会被触发，子控件点击不会被触发。
     * @return 返回你希望触发条目点击事件的ViewId。
     */
    public @IdRes int getItemClickViewId() {
        return 0;
    }

    /**
     * 判断两个数据模型是否为同一个数据模型。如果指定模型有唯一标识应当以唯一标识去判断。这里的默认实现是通过 {@link #equals(Object)} 方法去判断，
     * 你可以通过重写 {@link D#equals(Object)} 方法进行处理，也可以重写该方法进行处理。
     * <P>如果你没有重写该方法则最好重写 {@link D#equals(Object)} 方法进行唯一标识字段的比较，否则有可能会造成不必要刷新的item刷新。
     * @param oldItemData 旧的数据模型。
     * @param newItemDate 新的数据模型。
     * @return 如果相同则应当返回 <code color="blue">true</code>,否则应当返回 <code color="blue">false</code>。
     */
    @Override
    public boolean areItemsTheSame(D oldItemData, D newItemDate) {
        return oldItemData != null && oldItemData.equals(newItemDate);
    }

    /**
     * 获取两个对象不同的部分。
     * @param oldItemData 旧的数据模型。
     * @param newItemDate 新的数据模型。
     * @param bundle 比较两个对象不同的部分，将两个对象不同的部分存入该参数中。
     */
    @Override
    public void getChangePayload(D oldItemData, D newItemDate, Bundle bundle) {}

    @Override
    public <T extends View> T getView(int id) {
        View view = mViews.get(id);
        if (view != null) {
            return (T) view;
        }
        view = itemView.findViewById(id);
        mViews.put(id, view);
        return (T) view;
    }

    @Override
    public void setBackGround(Drawable drawable, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setBackground(drawable);
            }
        }
    }

    @Override
    public void setBackgroundColor(@ColorInt int color, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setBackgroundColor(color);
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void setBackgroundTintList(@Nullable ColorStateList tint, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setBackgroundTintList(tint);
            }
        }
    }

    @Override
    public void setBackgroundResource(@DrawableRes int drawable, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setBackgroundResource(drawable);
            }
        }
    }

    @Override
    public void setText(@IdRes int textViewId, CharSequence text) {
        TextView view = getView(textViewId);
        view.setText(text);
    }

    @Override
    public void setText(@IdRes int textViewId, @StringRes int textRes) {
        TextView view = getView(textViewId);
        String s = view.getContext().getString(textRes);
        view.setText(s);
    }

    @Override
    public void setHint(@IdRes int textViewId, CharSequence text) {
        TextView view = getView(textViewId);
        view.setHint(text);
    }

    @Override
    public void setHint(@IdRes int textViewId, @StringRes int textRes) {
        TextView view = getView(textViewId);
        String s = view.getContext().getString(textRes);
        view.setHint(s);
    }

    @Override
    public void setGone(@IdRes int...viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void setInvisible(@IdRes int...viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setVisibility(View.INVISIBLE);
            }
        }
    }

    @Override
    public void setVisible(@IdRes int...viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                getView(viewId).setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void setTextColor(@NonNull ColorStateList color, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                ((TextView)getView(viewId)).setTextColor(color);
            }
        }
    }

    @Override
    public void setTextColor(@ColorInt int color, @IdRes int... viewIds) {
        if (viewIds != null && viewIds.length > 0) {
            for (int viewId : viewIds) {
                ((TextView)getView(viewId)).setTextColor(color);
            }
        }
    }
}
