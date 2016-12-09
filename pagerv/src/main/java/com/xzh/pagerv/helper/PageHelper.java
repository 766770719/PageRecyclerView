package com.xzh.pagerv.helper;

import com.xzh.pagerv.refresh.IPageRefreshView;
import com.xzh.pagerv.refresh.OnPageRefreshListener;
import com.xzh.pagerv.rv.OnFooterShowListener;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;
import com.xzh.pagerv.status.PageStatusView;

import java.util.List;

/**
 * 基础分页操作类
 * Created by xiezihao on 16/12/7.
 */
public abstract class PageHelper<K, T, H> {

    //参数
    private PageRecyclerViewAdapter<T, H> adapter;
    private PageStatusView contentStatusView;
    private IPageRefreshView pageRefreshView;
    private PageStatusView footerStatusView;
    private OnPageListener<K> listener;

    //FooterView创建的Holder
    private PageViewHolder mFooterHolder;
    //默认页的KEY,当前的Key
    private K mDefaultKey, mCurrentKey;
    //是否加载中
    private boolean isLoading = false;

    /**
     * 初始化
     *
     * @param adapter           适配器
     * @param contentStatusView 状态View
     * @param pageRefreshView   下拉刷新
     * @param footerStatusView  Footer
     * @param listener          监听
     */
    public void init(PageRecyclerViewAdapter<T, H> adapter, PageStatusView contentStatusView, IPageRefreshView pageRefreshView,
                     PageStatusView footerStatusView, OnPageListener<K> listener) {
        this.adapter = adapter;
        this.contentStatusView = contentStatusView;
        this.pageRefreshView = pageRefreshView;
        this.footerStatusView = footerStatusView;
        this.listener = listener;

        //创建FooterHolder
        if (footerStatusView != null)
            mFooterHolder = new PageViewHolder(footerStatusView);

        //初始化下拉控件监听
        if (pageRefreshView != null) {
            pageRefreshView.setListener(new OnPageRefreshListener() {
                @Override
                public void onRefresh() {
                    refresh(true);
                }
            });
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
     * @param contentEmpty    内容空信息
     * @param footerEmpty     没有更多数据空信息
     */
    public void start(K defaultKey, String contentProgress, String footerProgress, String contentEmpty, String footerEmpty) {
        //缓存默认的Key
        this.mDefaultKey = defaultKey;
        //初始化状态View
        contentStatusView.setDefaultMsg(contentProgress, contentEmpty);
        contentStatusView.resetUI();
        if (footerStatusView != null) {
            footerStatusView.setDefaultMsg(footerProgress, footerEmpty);
            footerStatusView.resetUI();
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
        if (showRefreshView) { //需要显示效果，显示效果，不需要显示效果保持当前下拉刷新的显示效果
            if (pageRefreshView != null)
                pageRefreshView.showRefreshView(true);
        }
        //恢复状态控件进度状态，但不改变其显示属性：1.第一次加载失败，下拉刷新，此时会显示进度控件 2.加载成功过数据，下拉刷新，此时不会显示进度控件
        contentStatusView.progress();
        //加载第一页数据
        loadPage(mDefaultKey);
    }

    /**
     * 加载下一页数据
     */
    private void loadNextPage() {
        if (footerStatusView != null) {
            //判断Footer状态
            if (footerStatusView.isEmptyShow()) //没有更多数据了
                return;
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
    public void loadFailed(K key, String contentFailed, String footerFailed) {
        if (isFirstPage(key, mDefaultKey)) { //第一页数据：第一次进入加载或下拉刷新加载
            //只通知状态控件失败，不改变其隐藏属性。
            // 1.第一次加载数据或第一次加载失败后下拉刷新控件本来就是是显示的，直接通知失败即可
            // 2.加载成功一页数据后，控件被隐藏掉，下拉刷新失败，任然通知失败，此时控件是隐藏的，界面不会发生改变，依然显示现有数据
            contentStatusView.failed(contentFailed);
        } else { //其它页数据:第二页或以上
            //直接通知footer失败即可
            if (footerStatusView != null)
                footerStatusView.failed(footerFailed);
        }
        if (pageRefreshView != null)
            pageRefreshView.showRefreshView(false);
        isLoading = false;
    }

    /**
     * 加载空
     *
     * @param key 页Key
     */
    public void loadEmpty(K key) {
        if (isFirstPage(key, mDefaultKey)) { //第一页数据：第一次进入加载或下拉刷新加载
            adapter.resetUI(false);
            contentStatusView.resetUI();
            contentStatusView.empty();
        } else { //其它页数据:第二页或以上
            if (footerStatusView != null)
                footerStatusView.empty();
        }
        if (pageRefreshView != null)
            pageRefreshView.showRefreshView(false);
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
            contentStatusView.success();
            if (footerStatusView != null)
                footerStatusView.resetUI();
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
        if (pageRefreshView != null)
            pageRefreshView.showRefreshView(false);
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
