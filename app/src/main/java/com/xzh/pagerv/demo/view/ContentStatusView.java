package com.xzh.pagerv.demo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import com.xzh.pagerv.demo.R;
import com.xzh.pagerv.status.PageStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内容状态View
 * Created by xiezihao on 16/12/8.
 */
public class ContentStatusView extends PageStatusView {

    @BindView(R.id.v_progress)
    View v_progress;
    @BindView(R.id.tv_failed)
    TextView tv_failed;
    @BindView(R.id.tv_empty)
    TextView tv_empty;

    public ContentStatusView(Context context) {
        super(context);
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
    public void setDefaultMsg(String progressMsg, String emptyMsg) {
        //设置默认进度文本和空文本
        tv_empty.setText(emptyMsg);
    }

    @Override
    protected void setFailedMsg(String msg) {
        tv_failed.setText(msg);
    }

    @Override
    protected void showStatus(boolean progress, boolean failed, boolean empty) {
        v_progress.setVisibility(progress ? VISIBLE : GONE);
        tv_failed.setVisibility(failed ? VISIBLE : GONE);
        tv_empty.setVisibility(empty ? VISIBLE : GONE);
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
