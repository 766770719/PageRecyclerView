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
    private BaseStatusView contentStatusView;
    private IPageRefreshView pageRefreshView;
    private BaseStatusView footerStatusView;
    private OnPageListener<K> listener;

    //FooterView创建的Holder
    private PageViewHolder mFooterHolder;
    //默认页的KEY,当前的Key
    private K mDefaultKey, mCurrentKey;
    //是否加载中
    private boolean isLoading = false;

    /**
     * 初始化
     */
    public void init(PageRecyclerViewAdapter<T, H> adapter, BaseStatusView contentStatusView, IPageRefreshView pageRefreshView,
                     BaseStatusView footerStatusView, OnPageListener<K> listener) {
        this.adapter = adapter;
        this.contentStatusView = contentStatusView;
        this.pageRefreshView = pageRefreshView;
        this.footerStatusView = footerStatusView;
        this.listener = listener;

        //初始化下拉控件监听
        if (pageRefreshView != null) {
            pageRefreshView.setPageRefreshListener(new OnPageRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(true);
                }
            });
        }
        if (footerStatusView != null) {
            mFooterHolder = new PageViewHolder(footerStatusView);
        }
        //初始化下一页加载监听
        adapter.setOnFooterShowListener(new OnFooterShowListener() {
            @Override
            public void onFooterShow() {
                loadNextPage();
            }
        });
    }

    /**
     * 开始加载
     *
     * @param defaultKey      默认页的Key
     * @param contentProgress 内容进度信息
     * @param footerProgress  尾部进度信息
     */
    public void start(K defaultKey, String contentProgress, String footerProgress) {
        //缓存默认的Key
        this.mDefaultKey = defaultKey;
        //初始化状态View
        if (contentStatusView != null) {
            contentStatusView.progress(contentProgress);
        }
        //初始化FooterView
        if (footerStatusView != null) {
            footerStatusView.progress(footerProgress);
        }
        //第一次加载类似下拉刷新，只不过不显示下拉刷新的效果
        refresh(false);
    }

    /**
     * 刷新
     *
     * @param showRefreshView 是否显示下拉刷新控件
     */
    public void refresh(boolean showRefreshView) {
        if (showRefreshView && pageRefreshView != null) { //需要显示效果，显示效果，不需要显示效果保持当前下拉刷新的显示效果
            pageRefreshView.showPageRefreshView(true);
        }
        //加载第一页数据
        loadPage(mDefaultKey);
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
            footerStatusView.progress();
        }
        //加载下一页
        loadPage(getNextPageKey(mCurrentKey, adapter.list()));
    }

    /**
     * 加载页
     *
     * @param key 页Key
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
     *
     * @param key           页Key
     * @param contentFailed 内容失败信息
     * @param footerFailed  footer失败信息
     */
    public void loadFailed(K key, String contentFailed, String contentAction, String footerFailed, String footerFailedAction) {
        if (isFirstPage(key, mDefaultKey)) { //第一页数据：第一次进入加载或下拉刷新加载
            //只通知状态控件失败，不改变其隐藏属性。
            // 1.第一次加载数据或第一次加载失败后下拉刷新控件本来就是是显示的，直接通知失败即可
            // 2.加载成功一页数据后，控件被隐藏掉，下拉刷新失败，任然通知失败，此时控件是隐藏的，界面不会发生改变，依然显示现有数据
            if (contentStatusView != null) {
                contentStatusView.failed(contentFailed, contentAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        refresh(false);
                    }
                });
            }
        } else { //其它页数据:第二页或以上
            //直接通知footer失败即可
            if (footerStatusView != null) {
                footerStatusView.failed(footerFailed, footerFailedAction, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        loadNextPage();
                    }
                });
            }
        }
        if (pageRefreshView != null) {
            pageRefreshView.showPageRefreshView(false);
        }
        isLoading = false;
    }

    /**
     * 加载空
     *
     * @param key 页Key
     */
    public void loadEmpty(K key, String contentEmpty, String footerEmpty) {
        if (isFirstPage(key, mDefaultKey)) { //第一页数据：第一次进入加载或下拉刷新加载
            adapter.clearAll(false, true);
            if (contentStatusView != null) {
                contentStatusView.show().empty(contentEmpty);
            }
        } else { //其它页数据:第二页或以上
            if (footerStatusView != null) {
                footerStatusView.empty(footerEmpty);
            }
        }
        if (pageRefreshView != null) {
            pageRefreshView.showPageRefreshView(false);
        }
        isLoading = false;
    }

    /**
     * 加载成功
     *
     * @param key  页Key
     * @param data 数据列表
     */
    public void loadSuccess(K key, List<T> data) {
        List<T> list = adapter.list();
        if (isFirstPage(key, mDefaultKey)) { //第一页数据：第一次进入加载或下拉刷新加载
            if (contentStatusView != null) {
                contentStatusView.hidden();
            }
            if (footerStatusView != null) {
                footerStatusView.progress("sdfasdf");
            }
            adapter.setFooter(mFooterHolder);
            list.clear();
            list.addAll(data);
            adapter.notifyDataSetChanged();
        } else { //其它页数据:第二页或以上
            int curSize = list.size();
            list.addAll(data);
            //通知插入,Footer改变
            adapter.notifyItemRangeInserted(curSize, data.size());
            adapter.notifyItemChanged(adapter.getItemCount() - 1);
        }
        //缓存Key
        mCurrentKey = key;
        if (pageRefreshView != null) {
            pageRefreshView.showPageRefreshView(false);
        }
        isLoading = false;
    }

    /**
     * 是否是第一页
     *
     * @param key        当前key
     * @param defaultKey 第一页Key
     * @return true 是第一页
     */
    protected abstract boolean isFirstPage(K key, K defaultKey);

    /**
     * 获取下一页
     *
     * @param key  当前key
     * @param data 当前数据列表
     * @return 下一页的key
     */
    protected abstract K getNextPageKey(K key, List<T> data);
}
