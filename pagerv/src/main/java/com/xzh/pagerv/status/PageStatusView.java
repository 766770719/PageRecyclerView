package com.xzh.pagerv.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;

/**
 * 基础分页状态View
 * Created by xiezihao on 16/12/6.
 */
public abstract class PageStatusView extends RelativeLayout {

    public PageStatusView(Context context) {
        super(context);
        init();
    }

    public PageStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PageStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * 初始化
     */
    private void init() {
        addView(getContentView());
        initViews();
    }

    /**
     * 获取布局View，可使用{@link #inflate(int)}加载LayoutID成View
     *
     * @return 布局View
     */
    protected abstract View getContentView();

    /**
     * 加载布局
     *
     * @param layoutID 布局ID
     * @return View
     */
    protected View inflate(int layoutID) {
        return LayoutInflater.from(getContext()).inflate(layoutID, this, false);
    }

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
