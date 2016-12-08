package com.xzh.pagerv.status;

import android.content.Context;
import android.util.AttributeSet;

import com.xzh.pagerv.rv.PageViewHolder;

/**
 * 分页页脚View
 * Created by xiezihao on 16/12/8.
 */
public abstract class PageFooterStatusView extends PageStatusView {

    private final PageViewHolder HOLDER = new PageViewHolder(this);

    /**
     * 获取Footer的Holder对象
     *
     * @return Holder对象
     */
    public PageViewHolder getHolder() {
        return HOLDER;
    }

    public PageFooterStatusView(Context context) {
        super(context);
    }

    public PageFooterStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageFooterStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
