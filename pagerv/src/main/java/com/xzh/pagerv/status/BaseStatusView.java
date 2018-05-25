package com.xzh.pagerv.status;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.FrameLayout;

/**
 * 基础分页状态View
 * Created by xiezihao on 16/12/6.
 */
public abstract class BaseStatusView extends FrameLayout {

    public BaseStatusView(Context context) {
        super(context);
        init();
    }

    public BaseStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BaseStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public BaseStatusView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        addView(LayoutInflater.from(getContext()).inflate(getLayoutRes(), this, false));
        initViews();
    }

    /**
     * 获取布局id
     */
    protected abstract int getLayoutRes();

    /**
     * 初始化控件
     */
    protected abstract void initViews();

    /**
     * 显示进度控件
     */
    public BaseStatusView show() {
        setVisibility(VISIBLE);
        return this;
    }

    /**
     * 隐藏进度控件
     */
    public BaseStatusView hidden() {
        setVisibility(GONE);
        return this;
    }

    /**
     * 显示进度
     */
    public void progress() {
        progress(null);
    }

    /**
     * 显示进度
     */
    public abstract void progress(String msg);

    /**
     * 显示失败
     */
    public void failed(String msg, String action, OnClickListener actionListener) {
        failed(-1, msg, action, actionListener);
    }

    /**
     * 显示失败
     */
    public abstract void failed(int imgRes, String msg, String action, OnClickListener actionListener);

    /**
     * 显示空
     */
    public abstract void empty(String msg);

    /**
     * 是否是空了
     *
     * @return true 空 false不是空
     */
    public abstract boolean isEmptyShow();

    /**
     * 隐藏所有状态
     */
    public abstract void hideAllStatus();
}
