package com.xzh.pagerv.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.xzh.pagerv.footer.BaseFooterView;
import com.xzh.pagerv.demo.R;

/**
 * 分页加载尾部的控件
 * Created by xiezihao on 16/12/6.
 */
public class FooterView extends BaseFooterView {

    private View v_progress, v_failed, v_empty;

    public FooterView(Context context) {
        super(context);
    }

    public FooterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public FooterView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getContentViewID() {
        return R.layout.view_footer;
    }

    @Override
    protected void initViews() {
        v_progress = findViewById(R.id.v_progress);
        v_failed = findViewById(R.id.v_failed);
        v_empty = findViewById(R.id.v_empty);
    }

    @Override
    protected void showStatus(boolean progress, boolean failed, boolean empty) {
        v_progress.setVisibility(progress ? VISIBLE : GONE);
        v_failed.setVisibility(failed ? VISIBLE : GONE);
        v_empty.setVisibility(empty ? VISIBLE : GONE);
    }

    @Override
    public boolean isEmptyShow() {
        return v_empty.getVisibility() == VISIBLE;
    }

    @Override
    public void setOnFailedClickListener(OnClickListener listener) {
        v_failed.setOnClickListener(listener);
    }
}
