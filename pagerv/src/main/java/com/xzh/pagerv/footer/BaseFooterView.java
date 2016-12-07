package com.xzh.pagerv.footer;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;

import com.xzh.pagerv.rv.BaseViewHolder;

/**
 * 基础FooterView
 * Created by xiezihao on 16/12/6.
 */
public abstract class BaseFooterView extends RelativeLayout {

    //Holder对象
    private BaseViewHolder holder = new BaseViewHolder(this);

    public BaseFooterView(Context context) {
        super(context);
        init();
    }

    public BaseFooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseFooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 获取ViewHolder
     *
     * @return ViewHolder
     */
    public BaseViewHolder getHolder() {
        return holder;
    }

    /**
     * 初始化
     */
    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(getContentViewID(), this, false));
        initViews();
        progress();
    }

    /**
     * 获取内容布局ID
     *
     * @return 布局ID
     */
    protected abstract int getContentViewID();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 显示进度
     */
    public void progress() {
        showStatus(true, false, false);
    }

    /**
     * 失败
     */
    public void failed() {
        showStatus(false, true, false);
    }

    /**
     * 空
     */
    public void empty() {
        showStatus(false, false, true);
    }

    /**
     * 成功，隐藏
     */
    public void success() {
        setVisibility(GONE);
    }

    /**
     * 重置UI
     */
    public void resetUI() {
        progress();
        setVisibility(VISIBLE);
    }

    /**
     * 显示状态
     *
     * @param progress true 是当前状态
     * @param failed   true 是当前状态
     * @param empty    true 是当前状态
     */
    protected abstract void showStatus(boolean progress, boolean failed, boolean empty);

    /**
     * 是否是空了
     *
     * @return true 空 false不是空
     */
    public abstract boolean isEmptyShow();

    /**
     * 设置失败点击监听
     *
     * @param listener 失败点击监听
     */
    public abstract void setOnFailedClickListener(OnClickListener listener);
}
