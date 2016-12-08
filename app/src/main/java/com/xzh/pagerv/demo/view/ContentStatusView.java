package com.xzh.pagerv.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xzh.pagerv.demo.R;
import com.xzh.pagerv.status.PageContentStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内容状态View
 * Created by xiezihao on 16/12/8.
 */
public class ContentStatusView extends PageContentStatusView {

    @BindView(R.id.v_progress)
    View v_progress;
    @BindView(R.id.tv_failed)
    TextView tv_failed;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    public ContentStatusView(Context context) {
        super(context);
    }

    public ContentStatusView(Context context, String progressMsg) {
        super(context, progressMsg);
    }

    public ContentStatusView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ContentStatusView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected View getContentView() {
        return inflate(R.layout.view_content_status);
    }

    @Override
    protected void initViews() {
        ButterKnife.bind(this);
    }

    @Override
    protected void showStatus(boolean progress, boolean failed, boolean empty, String msg) {
        v_progress.setVisibility(progress ? VISIBLE : GONE);
        tv_failed.setVisibility(failed ? VISIBLE : GONE);
        tv_failed.setText(failed ? msg : null);
        tv_empty.setVisibility(empty ? VISIBLE : GONE);
        tv_empty.setText(failed ? msg : null);
    }

    @Override
    public boolean isEmptyShow() {
        return tv_empty.getVisibility() == VISIBLE;
    }

    @Override
    public void setOnFailedClickListener(OnClickListener listener) {
        tv_failed.setOnClickListener(listener);
    }
}
