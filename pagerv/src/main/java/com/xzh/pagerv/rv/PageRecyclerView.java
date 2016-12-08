package com.xzh.pagerv.rv;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * 基础RecyclerView
 * Created by xiezihao on 16/12/5.
 */
public class PageRecyclerView extends RecyclerView {

    public PageRecyclerView(Context context) {
        super(context);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public PageRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /**
     * 初始化RV
     *
     * @param layoutManager 布局
     * @param hasFixedSize  是否是固定高度
     * @param adapter       重写后的适配器
     */
    public void init(LayoutManager layoutManager, boolean hasFixedSize, PageRecyclerViewAdapter adapter) {
        //RV配置
        setLayoutManager(layoutManager);
        setHasFixedSize(hasFixedSize);
        setAdapter(adapter);
    }

    /**
     * 设置RV的某一类型的缓存View的数量，应该大于一屏可显示的最大数量，
     * 不然下拉刷新时会重新生成新的缓存View数组导致闪烁，无法复用控件
     *
     * @param viewType 布局类型
     * @param size     缓存控件个数
     */
    public void setRecycledViewPoolSize(int viewType, int size) {
        getRecycledViewPool().setMaxRecycledViews(viewType, size);
    }
}
