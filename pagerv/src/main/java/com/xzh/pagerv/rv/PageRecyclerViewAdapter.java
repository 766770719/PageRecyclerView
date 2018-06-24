package com.xzh.pagerv.rv;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 基础Adapter
 * Created by Hao on 2016/1/1.
 *
 * @param <T> 数据类型
 * @param <H> ViewHolder类型
 */
public abstract class PageRecyclerViewAdapter<T, H> extends RecyclerView.Adapter<PageViewHolder> {

    @Override
    public int getItemCount() {
        return OBJECTS.size() + (mFooterHolder == null ? 0 : 1) + (mHeaderHolder == null ? 0 : 1);
    }

    /**
     * 判断应该显示的布局
     *
     * @param position 数据的位置
     * @return 当前数据对应的布局ID, 用布局ID代表类型
     */
    @Override
    public final int getItemViewType(int position) {
        if (isFooter(position)) { //Footer的位置
            return VIEW_TYPE_FOOTER;
        } else if (isHeader(position)) { //Header的位置
            return VIEW_TYPE_HEADER;
        }
        int dataPosition = getDataPosition(position);
        return getLayoutId(OBJECTS.get(dataPosition), dataPosition);
    }

    @Override
    public PageViewHolder onCreateViewHolder(ViewGroup viewGroup, int layoutId) {
        if (layoutId == VIEW_TYPE_FOOTER) { //是Footer
            return mFooterHolder;
        } else if (layoutId == VIEW_TYPE_HEADER) { //是Header
            return mHeaderHolder;
        }

        //必须传入parent来inflate布局，不然item的match_parent无效
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(layoutId, viewGroup, false);
        PageViewHolder holder = getViewHolder(root, layoutId);
        int clickViewID = getClickViewId(layoutId);
        if (clickViewID == 0) { //整行点击
            holder.clickView = root;
        } else { //其它
            holder.clickView = holder.findView(clickViewID);
        }
        if (holder.clickView != null) { //存在可点击的View
            holder.clickView.setOnClickListener(mClickListener);
        }
        return holder;
    }

    /**
     * 获取布局的点击方式,默认整行点击
     *
     * @param layoutId 布局ID
     * @return 可点击View的Id, 0整行,-1不点击
     */
    public int getClickViewId(int layoutId) {
        return 0;
    }

    @Override
    public void onBindViewHolder(PageViewHolder holder, int position) {
        if (isFooter(position)) { //Footer不绑定数据
            onFooterShow();
            return;
        } else if (isHeader(position)) { //Header不绑定数据
            return;
        }
        int dataPosition = getDataPosition(position);
        if (holder.clickView != null) {
            holder.clickView.setTag(dataPosition);
        }
        bindView((H) holder, OBJECTS.get(dataPosition), dataPosition, getItemViewType(position));
    }

    /**
     * 获取数据的位置
     *
     * @param position RV中Item的位置
     * @return 数据的位置
     */
    private int getDataPosition(int position) {
        //如果存在头部，数据位置应该减去1
        return position - (mHeaderHolder == null ? 0 : 1);
    }

    //点击监听
    private View.OnClickListener mClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int position = (int) view.getTag();
            onItemClick(view, OBJECTS.get(position), position);
        }
    };

    /**
     * 点击监听
     *
     * @param view     点击的View
     * @param obj      数据对象
     * @param position 数据位置
     */
    protected void onItemClick(View view, T obj, int position) {
    }

    //抽象方法=======================

    /**
     * 获取布局的下标
     *
     * @param obj      数据类型
     * @param position 数据位置
     * @return 对应数据的布局下标
     */
    public abstract int getLayoutId(T obj, int position);

    /**
     * 获取ViewHolder
     *
     * @param root Holder绑定的View对象
     * @return ItemHolder
     */
    protected abstract PageViewHolder getViewHolder(View root, int layoutID);

    /**
     * 绑定数据
     *
     * @param holder   ItemHolder
     * @param obj      数据对象
     * @param position 数据位置
     * @param index    布局的Index
     */
    protected abstract void bindView(H holder, T obj, int position, int index);

    //数据相关=======================

    //数据列表
    private final List<T> OBJECTS = new ArrayList<>();

    /**
     * 列表
     *
     * @return 列表
     */
    public List<T> list() {
        return OBJECTS;
    }

    //头尾相关=======================

    /**
     * Footer的ViewType类型
     */
    private final int VIEW_TYPE_FOOTER = -1000;
    /**
     * Header的ViewType类型
     */
    private final int VIEW_TYPE_HEADER = -1001;

    private PageViewHolder mFooterHolder;
    private PageViewHolder mHeaderHolder;

    /**
     * 设置FooterView,需要{@link #notifyDataSetChanged()},目前只支持单Footer主要是用于显示分页
     *
     * @param footer footer
     */
    public void setFooter(PageViewHolder footer) {
        mFooterHolder = footer;
    }

    /**
     * 设置HeaderView,需要{@link #notifyDataSetChanged()},目前只支持单Header
     *
     * @param header header
     */
    public void setHeader(PageViewHolder header) {
        mHeaderHolder = header;
    }

    /**
     * 是否是Footer
     *
     * @param position 数据位置
     */
    public boolean isFooter(int position) {
        return mFooterHolder != null && position == getItemCount() - 1; //Footer存在并且是最后的数据了
    }

    /**
     * 是否是Header
     *
     * @param position 数据位置
     */
    public boolean isHeader(int position) {
        return mHeaderHolder != null && position == 0; //Header存在并且是第一个数据
    }

    private OnFooterShowListener onFooterShowListener;

    /**
     * Footer显示，分页需要重写判断是否到底
     */
    public void setOnFooterShowListener(OnFooterShowListener onFooterShowListener) {
        this.onFooterShowListener = onFooterShowListener;
    }

    /**
     * 尾部显示
     */
    protected void onFooterShow() {
        if (onFooterShowListener != null) {
            onFooterShowListener.onFooterShow();
        }
    }

    //清除相关=======================

    public void clearAll() {
        clearAll(true, true);
    }

    public void clearAll(boolean clearHeader, boolean clearFooter) {
        if (clearHeader) {
            setHeader(null);
        }
        if (clearFooter) {
            setFooter(null);
        }
        list().clear();
        notifyDataSetChanged();
    }
}