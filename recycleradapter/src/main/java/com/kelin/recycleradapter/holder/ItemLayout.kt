package com.kelin.recycleradapter.holder

import android.support.annotation.LayoutRes

/**
 * 描述 用来获取跟布局资源文件 [LayoutRes] 的注解。
 * 创建人 kelin
 * 创建时间 2017/3/27  下午1:57
 * 版本 v 1.0.0
 */
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.CLASS, AnnotationTarget.FILE)
annotation class ItemLayout(
        /**
         * 条目的布局文件。
         */
        @LayoutRes val value: Int)
