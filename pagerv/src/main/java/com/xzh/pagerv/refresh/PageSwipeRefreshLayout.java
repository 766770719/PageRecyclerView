package com.xzh.pagerv.refresh;

import android.content.Context;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

/**
 * 用于分页的SwipeRefreshLayout扩展
 * Created by xiezihao on 16/12/7.
 */
public class PageSwipeRefreshLayout extends SwipeRefreshLayout implements IPageRefreshView {

    public PageSwipeRefreshLayout(Context context) {
        super(context);
    }

    public PageSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(final OnPageRefreshListener listener) {
        setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh();
            }
        });
    }

    @Override
    public void showRefreshView(boolean show) {
        setRefreshing(show);
    }
}
