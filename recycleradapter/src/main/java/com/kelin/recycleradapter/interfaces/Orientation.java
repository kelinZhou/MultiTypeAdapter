package com.kelin.recycleradapter.interfaces;

import android.support.annotation.IntDef;
import android.widget.LinearLayout;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * 描述 ${TODO}
 * 创建人 kelin
 * 创建时间 2017/4/28  上午10:43
 * 版本 v 1.0.0
 */

@IntDef({LinearLayout.VERTICAL, LinearLayout.HORIZONTAL})
@Retention(RetentionPolicy.SOURCE)
public @interface Orientation {
}
