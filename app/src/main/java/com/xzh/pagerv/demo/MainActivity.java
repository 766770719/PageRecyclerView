package com.xzh.pagerv.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
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
