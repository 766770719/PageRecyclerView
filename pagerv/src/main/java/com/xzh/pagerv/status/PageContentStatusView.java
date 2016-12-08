package com.xzh.pagerv.status;

import android.content.Context;
import android.util.AttributeSet;

/**
 * 内容状态View
 * Created by xiezihao on 16/12/8.
 */
public abstract class PageContentStatusView extends PageStatusView{

    public PageContentStatusView(Context context) {
        super(context);
    }

    public PageContentStatusView(Context context, String progressMsg) {
        super(context, progressMsg);
    }

    public PageContentStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public PageContentStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
