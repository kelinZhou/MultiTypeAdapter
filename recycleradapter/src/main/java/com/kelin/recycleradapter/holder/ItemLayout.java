package com.kelin.recycleradapter.holder;

import android.support.annotation.LayoutRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 描述 用来获取跟布局资源文件 {@link LayoutRes} 的注解。
 * 创建人 kelin
 * 创建时间 2017/3/27  下午1:57
 * 版本 v 1.0.0
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface ItemLayout {

    /**
     * 表示没有指定Header或则Footer布局。
     */
    int NOT_HEADER_FOOTER = 0X0000_0000;

    /**
     * 条目的布局文件。
     */
    @LayoutRes int rootLayoutId();

    /**
     * 头布局文件，如果没有指定则表示没有页眉。
     */
    @LayoutRes int headerLayoutId() default NOT_HEADER_FOOTER;

    /**
     * 脚布局文件，如果没有指定则表示没有页脚。
     */
    @LayoutRes int footerLayoutId() default NOT_HEADER_FOOTER;
}
