package com.kelin.recycleradapter.interfaces;

import android.view.View;

/**
 * <strong>描述: </strong> 用来描述条目的共性信息。
 * 列表中的每个条目都是LayoutItem的子类对象，无论是适配器中的ViewHolder还是悬浮吸顶条目列表中的悬浮条目。它们都有以下两个特征：
 * <p> 1.在RecyclerView中有索引。
 * <p> 2.都承载这一块View视图，所以该接口一共有两个抽象方法：{@link #getLayoutPosition()} 和 {@link #getItemView()}。
 * @see #getLayoutPosition()
 * @see #getItemView()
 *
 * <p><strong>创建人: </strong>kelin
 * <p><strong>创建时间: </strong>2017/6/27  下午5:05
 * <p><strong>版本: </strong>v 1.0.0
 */

public interface LayoutItem {

    /**
     * 获取当前布局条目所在的索引位置。
     *
     * @return 返回索引位置。
     */
    int getLayoutPosition();

    /**
     * 获取当前布局条目中的View视图。
     *
     * @return 返回当前条目所承载的View。
     */
    View getItemView();
}
