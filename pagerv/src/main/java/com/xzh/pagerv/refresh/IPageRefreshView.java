package com.xzh.pagerv.refresh;

/**
 * 下拉刷新控件功能接口
 * Created by xiezihao on 16/12/7.
 */
public interface IPageRefreshView {

    /**
     * 设置监听
     *
     * @param listener 刷新监听
     */
    void setPageRefreshListener(OnPageRefreshListener listener);

    /**
     * 显示刷新控件
     *
     * @param show true 显示 false 隐藏
     */
    void showPageRefreshView(boolean show);
}
