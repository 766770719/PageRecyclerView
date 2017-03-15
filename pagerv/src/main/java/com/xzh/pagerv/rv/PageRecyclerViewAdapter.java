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

    private int[] layoutIDs;
    private int[] clickViewIDs;

    /**
     * 单样式构造函数,整行点击
     *
     * @param layoutID 布局ID
     */
    public PageRecyclerViewAdapter(int layoutID) {
        this(layoutID, 0);
    }

    /**
     * 单样式构造函数
     *
     * @param layoutID    布局ID
     * @param clickViewID 点击ViewID，0整行，-1不设置
     */
    public PageRecyclerViewAdapter(int layoutID, int clickViewID) {
        this(new int[]{layoutID}, new int[]{clickViewID});
    }

    /**
     * 多样式构造函数
     *
     * @param layoutIDs    布局IDs
     * @param clickViewIDs 点击ViewIDs，0整行，-1不设置，其它对应的View，和布局对应
     */
    public PageRecyclerViewAdapter(int[] layoutIDs, int[] clickViewIDs) {
        this.layoutIDs = layoutIDs;
        this.clickViewIDs = clickViewIDs;
    }

    @Override
    public int getItemCount() {
        return OBJECTS.size() + (mFooterHolder == null ? 0 : 1) + (mHeaderHolder == null ? 0 : 1);
    }

    /**
     * 判断应该显示的布局
     *
     * @param position 数据的位置
     * @return 当前数据对应的布局下标，和传入的布局列表对应
     */
    @Override
    public final int getItemViewType(int position) {
        if (isFooter(position)) { //Footer的位置
            return VIEW_TYPE_FOOTER;
        } else if (isHeader(position)) { //Header的位置
            return VIEW_TYPE_HEADER;
        }
        return getLayoutIndex(OBJECTS.get(getDataPosition(position)));
    }

    /**
     * 获取布局的下标
     *
     * @param obj 数据类型
     * @return 对应数据的布局下标
     */
    public int getLayoutIndex(T obj) {
        return 0;
    }

    @Override
    public PageViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == VIEW_TYPE_FOOTER) { //是Footer
            return mFooterHolder;
        } else if (viewType == VIEW_TYPE_HEADER) { //是Header
            return mHeaderHolder;
        }

        if (viewType < 0 || viewType >= layoutIDs.length) { //不合法viewType,不在布局数组长度内
            throw new ArrayIndexOutOfBoundsException("getItemViewType的返回值不在传入的布局数组长度范围内");
        }

        //必须传入parent来inflate布局，不然item的match_parent无效
        int layoutID = layoutIDs[viewType];
        View root = LayoutInflater.from(viewGroup.getContext()).inflate(layoutID, viewGroup, false);
        PageViewHolder holder = getViewHolder(root, layoutID);
        int clickViewID = clickViewIDs[viewType];
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
     * 获取ViewHolder
     *
     * @param root     Holder绑定的View对象
     * @param layoutID 布局的ID
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

    /**
     * 还原到初始显示状态
     *
     * @param needClearHeader 是否同时清空头部
     */
    public void resetUI(boolean needClearHeader) {
        if (needClearHeader) {
            setHeader(null);
        }
        setFooter(null);
        OBJECTS.clear();
        notifyDataSetChanged();
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
    private boolean isFooter(int position) {
        return mFooterHolder != null && position == getItemCount() - 1; //Footer存在并且是最后的数据了
    }

    /**
     * 是否是Header
     *
     * @param position 数据位置
     */
    private boolean isHeader(int position) {
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
        if (onFooterShowListener != null)
            onFooterShowListener.onFooterShow();
    }
}