package com.xzh.pagerv.helper;

import android.view.View;

import com.xzh.pagerv.refresh.IPageRefreshView;
import com.xzh.pagerv.refresh.OnPageRefreshListener;
import com.xzh.pagerv.rv.OnFooterShowListener;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;
import com.xzh.pagerv.status.BaseStatusView;

import java.util.List;

/**
 * 基础分页操作类
 * Created by xiezihao on 16/12/7.
 */
public abstract class PageHelper<K, T, H> {

    //参数
    private PageRecyclerViewAdapter<T, H> adapter;
    private IPageRefreshView pageRefreshView;
    private BaseStatusView contentStatusView;
    private BaseStatusView footerStatusView;
    //FooterView创建的Holder
    private PageViewHolder mFooterHolder;

    //默认页的KEY,当前的Key
    private K defaultKey, mCurrentKey;
    //进度消息
    private String contentProgressMsg, footerProgressMsg;
    //加载监听
    private OnPageListener<K> listener;

    //是否加载中
    private boolean isLoading = false;

    /**
     * 初始化
     */
    public void init(PageRecyclerViewAdapter<T, H> adapter, IPageRefreshView pageRefreshView, BaseStatusView contentStatusView, BaseStatusView footerStatusView,
                     String contentProgressMsg, String footerProgressMsg) {
        //缓存控件
        this.adapter = adapter;
        this.pageRefreshView = pageRefreshView;
        this.contentStatusView = contentStatusView;
        this.footerStatusView = footerStatusView;
        //缓存进度消息
        this.contentProgressMsg = contentProgressMsg;
        this.footerProgressMsg = footerProgressMsg;

        //初始化默认清空列表数据
        adapter.clearAll(false, true);
        //初始化下一页加载监听
        adapter.setOnFooterShowListener(new OnFooterShowListener() {
            @Override
            public void onFooterShow() {
                loadNextPage();
            }
        });

        //初始化下拉控件监听
        if (pageRefreshView != null) {
            pageRefreshView.setPageRefreshListener(new OnPageRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(true);
                }
            });
        }

        //初始化状态控件部分
        if (contentStatusView != null) {
            contentStatusView.progress(contentProgressMsg);
        }
        if (footerStatusView != null) {
            mFooterHolder = new PageViewHolder(footerStatusView);
            footerStatusView.progress(footerProgressMsg);
        }
    }

    /**
     * 开始加载
     */
    public void start(K defaultKey, OnPageListener<K> listener, boolean showRefreshView) {
        //缓存默认的Key
        this.defaultKey = defaultKey;
        //加载监听
        this.listener = listener;

        //第一次加载类似下拉刷新
        refresh(showRefreshView);
    }

    /**
     * 刷新
     */
    public void refresh(boolean showRefreshView) {
        boolean isShowContentStatus = contentStatusView != null && contentStatusView.isShow() && adapter.list().isEmpty();
        if (isShowContentStatus) {
            contentStatusView.progress(contentProgressMsg); //状态View需要显示，显示进度
        } else {
            if (contentStatusView != null) contentStatusView.hidden(); //隐藏状态View
        }
        if (pageRefreshView != null) { //状态进度显示,下拉就不显示不管如何触发的刷新
            pageRefreshView.showPageRefreshView(!isShowContentStatus && showRefreshView);
        }
        //加载第一页数据
        loadPage(defaultKey);
    }

    /**
     * 加载下一页数据
     */
    private void loadNextPage() {
        if (footerStatusView != null) {
            //判断Footer状态
            if (footerStatusView.isEmptyShow()) { //没有更多数据了
                return;
            }
            //设置footer的显示
            footerStatusView.progress(footerProgressMsg);
        }
        //加载下一页
        loadPage(getNextPageKey(mCurrentKey, adapter.list()));
    }

    /**
     * 加载页
     */
    private void loadPage(K key) {
        //判断加载状态
        if (isLoading)
            return;
        isLoading = true;
        listener.loadPage(key);
    }

    /**
     * 加载失败
     */
    public void loadFailed(K key, String contentFailed, String contentFailedAction, String footerFailed, String footerFailedAction) {
        //隐藏下拉
        if (pageRefreshView != null) {
            pageRefreshView.showPageRefreshView(false);
        }

        if (isFirstPage(key, defaultKey)) { //第一页
            if (contentStatusView != null && adapter.list().isEmpty()) { //存在状态View并且数据为空,显示失败
                contentStatusView.failed(contentFailed, contentFailedAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh(false);
                    }
                });
            }
        } else { //第二页和以后
            if (footerStatusView != null) { //尾部失败
                footerStatusView.failed(footerFailed, footerFailedAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadNextPage();
                    }
                });
            }
        }

        //缓存数据
        isLoading = false;
    }

    /**
     * 加载成功
     */
    public void loadSuccess(K key, List<T> data, String contentEmpty, String footerEmpty) {
        //隐藏下拉
        if (pageRefreshView != null) {
            pageRefreshView.showPageRefreshView(false);
        }

        //数据处理
        boolean isEmpty = data == null || data.isEmpty();
        if (isFirstPage(key, defaultKey)) { //第一页
            adapter.clearAll(false, true); //先清空所有数据
            if (contentStatusView != null) { //处理内容状态控件
                if (isEmpty) contentStatusView.empty(contentEmpty);
                else contentStatusView.hidden();
            }
            if (footerStatusView != null) { //改变尾部状态控件
                footerStatusView.progress(footerProgressMsg);
            }
            if (!isEmpty) { //数据不为空添加尾部和数据
                adapter.setFooter(mFooterHolder);
                adapter.list().addAll(data);
                adapter.notifyDataSetChanged();
            }
        } else { //第二页和以后
            if (footerStatusView != null) { //改变尾部状态
                if (isEmpty) footerStatusView.empty(footerEmpty);
                else footerStatusView.progress(footerProgressMsg);
            }
            if (!isEmpty) { //不为空,添加数据
                int curSize = adapter.list().size();
                adapter.list().addAll(data);
                adapter.notifyItemRangeInserted(curSize, data.size()); //通知插入
                adapter.notifyItemChanged(adapter.getItemCount() - 1); //通知尾部改变
            }
        }

        //缓存数据
        mCurrentKey = key;
        isLoading = false;
    }

    /**
     * 获取尾部状态View
     */
    public BaseStatusView getFooterStatusView() {
        return footerStatusView;
    }

    public void setFooterStatusView(BaseStatusView footerStatusView) {
        this.footerStatusView = footerStatusView;
    }

    /**
     * 获取尾部的Holder
     */
    public PageViewHolder getFooterHolder() {
        return mFooterHolder;
    }

    /**
     * 获取内容状态消息
     */
    public String getContentProgressMsg() {
        return contentProgressMsg;
    }

    /**
     * 获取尾部状态消息
     */
    public String getFooterProgressMsg() {
        return footerProgressMsg;
    }

    /**
     * 设置默认Key
     */
    public void setDefaultKey(K defaultKey) {
        this.defaultKey = defaultKey;
    }

    /**
     * 获取默认页Key
     */
    public K getDefaultKey() {
        return defaultKey;
    }

    /**
     * 设置当前Key
     */
    public void setCurrentKey(K currentKey) {
        this.mCurrentKey = currentKey;
    }

    /**
     * 获取当前页Key
     */
    public K getCurrentKey() {
        return mCurrentKey;
    }

    /**
     * 是否是第一页
     *
     * @param key        当前key
     * @param defaultKey 第一页Key
     * @return true 是第一页
     */
    public abstract boolean isFirstPage(K key, K defaultKey);

    /**
     * 获取下一页
     *
     * @param key  当前key
     * @param data 当前数据列表
     * @return 下一页的key
     */
    public abstract K getNextPageKey(K key, List<T> data);
}
