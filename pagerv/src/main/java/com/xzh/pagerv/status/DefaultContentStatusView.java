package com.xzh.pagerv.status;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.xzh.pagerv.R;

/**
 * 默认状态View
 * Created by abc on 2018/5/24.
 */
public class DefaultContentStatusView extends BaseStatusView {

    private ViewGroup vg_progress;

    private ViewGroup vg_failed;
    private TextView tv_failed;
    private Button btn_failed_retry;

    private ViewGroup vg_empty;
    private TextView tv_empty;

    public DefaultContentStatusView(Context context) {
        super(context);
    }

    public DefaultContentStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public DefaultContentStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.zhx_view_default_content_status;
    }

    @Override
    protected void initViews() {
        vg_progress = findViewById(R.id.vg_progress);

        vg_failed = findViewById(R.id.vg_failed);
        tv_failed = findViewById(R.id.tv_failed);
        btn_failed_retry = findViewById(R.id.btn_failed_retry);

        vg_empty = findViewById(R.id.vg_empty);
        tv_empty = findViewById(R.id.tv_empty);
    }

    @Override
    public void progress(String msg) {
        show().hideAllStatus();
        vg_progress.setVisibility(VISIBLE);
    }

    @Override
    public void failed(int imgRes, String msg, String action, OnClickListener actionListener) {
        show().hideAllStatus();
        vg_failed.setVisibility(VISIBLE);
        tv_failed.setText(msg);
        btn_failed_retry.setText(action);
        btn_failed_retry.setOnClickListener(actionListener);
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
