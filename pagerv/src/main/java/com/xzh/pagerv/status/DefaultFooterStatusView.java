package com.xzh.pagerv.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xzh.pagerv.R;

/**
 * 分页加载尾部的控件
 * Created by xiezihao on 16/12/6.
 */
public class DefaultFooterStatusView extends BaseStatusView {

    private ViewGroup vg_progress;
    private TextView tv_progress;

    private ViewGroup vg_failed;
    private TextView tv_failed;

    private ViewGroup vg_empty;
    private TextView tv_empty;

    public DefaultFooterStatusView(Context context) {
        super(context);
    }

    public DefaultFooterStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultFooterStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.zhx_view_default_footer_status;
    }

    @Override
    protected void initViews() {
        vg_progress = findViewById(R.id.vg_progress);
        tv_progress = findViewById(R.id.tv_progress);

        vg_failed = findViewById(R.id.vg_failed);
        tv_failed = findViewById(R.id.tv_failed);

        vg_empty = findViewById(R.id.vg_empty);
        tv_empty = findViewById(R.id.tv_empty);
    }

    @Override
    public void progress(String msg) {
        show().hideAllStatus();
        vg_progress.setVisibility(VISIBLE);
        tv_progress.setText(msg);
    }

    @Override
    public void failed(int imgRes, String msg, String action, OnClickListener actionListener) {
        show().hideAllStatus();
        vg_failed.setVisibility(VISIBLE);
        tv_failed.setText(msg);
        vg_failed.setOnClickListener(actionListener);
    }

    @Override
    public void empty(String msg) {
        show().hideAllStatus();
        vg_empty.setVisibility(VISIBLE);
        tv_empty.setText(msg);
    }

    @Override
    public boolean isEmptyShow() {
        return vg_empty.getVisibility() == VISIBLE;
    }

    @Override
    public void hideAllStatus() {
        vg_progress.setVisibility(GONE);
        vg_failed.setVisibility(GONE);
        vg_empty.setVisibility(GONE);
    }
}
