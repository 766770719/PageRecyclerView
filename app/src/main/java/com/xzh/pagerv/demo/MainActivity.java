package com.xzh.pagerv.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.xzh.pagerv.status.DefaultContentStatusView;
import com.xzh.pagerv.status.DefaultFooterStatusView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @BindView(R.id.dcsv)
    DefaultContentStatusView dcsv;
    @BindView(R.id.dfsv)
    DefaultFooterStatusView dfsv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        dcsv.progress("kaishi");
        dcsv.empty("空");
        dcsv.failed("网络异常", "再次请求", v -> {
            dcsv.progress("请求中");
        });
        dcsv.hidden();

        dfsv.progress("加载中...");
        dfsv.empty("没有数据了");
        dfsv.failed("网络错误,点击重新加载", null, v -> {
            dfsv.progress("加载中1...");
        });
        dfsv.hidden();
    }

    @OnClick({R.id.v_one_row, R.id.v_two_row, R.id.v_page_one_row})
    void onClick(View v) {
        int id = v.getId();
        Intent intent = new Intent();
        if (id == R.id.v_one_row) { //单行
            intent.setClass(this, OneRowActivity.class);
        } else if (id == R.id.v_two_row) { //两行(多行)
            intent.setClass(this, TwoRowActivity.class);
        } else if (id == R.id.v_page_one_row) { //带分页单行样式
            intent.setClass(this, PageOneRowActivity.class);
        }
        startActivity(intent);
    }

}
