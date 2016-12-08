package com.xzh.pagerv.demo.view;

import android.content.Context;

import com.xzh.pagerv.refresh.IPageRefreshView;
import com.xzh.pagerv.refresh.OnPageRefreshListener;

/**
 * 下拉刷新控件扩展,这个DEMO使用SwipeRefreshLayout实现IPageRefreshView来创建一个供分页使用的下拉刷新控件,
 * 这个类的功能和{@link com.xzh.pagerv.refresh.PageSwipeRefreshLayout}一样，如果使用SwipeRefreshLayout不用写这个类，
 * 直接用{@link com.xzh.pagerv.refresh.PageSwipeRefreshLayout}即可，其它下拉刷新控件需要按这个类写的这样自己去扩展
 * Created by xiezihao on 16/12/8.
 */
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout implements IPageRefreshView {

    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public void init(OnPageRefreshListener listener) {
        setOnRefreshListener(listener::onRefresh);
    }

    @Override
    public void showRefreshView(boolean show) {
        setRefreshing(show);
    }
}
