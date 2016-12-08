package com.xzh.pagerv.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xzh.pagerv.demo.module.User;
import com.xzh.pagerv.demo.view.ContentStatusView;
import com.xzh.pagerv.demo.view.FooterStatusView;
import com.xzh.pagerv.helper.IndexPageHelper;
import com.xzh.pagerv.refresh.PageSwipeRefreshLayout;
import com.xzh.pagerv.rv.PageRecyclerView;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 带分页的单行页
 * Created by xiezihao on 16/12/8.
 */
public class PageOneRowActivity extends Activity {

    @BindView(R.id.psrl)
    PageSwipeRefreshLayout psrl;
    @BindView(R.id.prv)
    PageRecyclerView prv;
    @BindView(R.id.csv)
    ContentStatusView csv;

    private IndexPageHelper<User, ViewHolder> mHelper = new IndexPageHelper<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_one_row);
        ButterKnife.bind(this);

        //初始化RV
        prv.init(new LinearLayoutManager(this), true, mAdapter);

        //初始化分页Helper
        mHelper.init(mAdapter, csv, psrl, new FooterStatusView(this), this::loadPage);
        //开始加载，并设置默认的页标
        mHelper.start(1, "数据加载中...", "正在获取下一页数据", "暂无数据信息", "没有更多数据了");
    }

    /**
     * 加载页
     *
     * @param page 页标
     */
    private void loadPage(int page) {
        //测试数据
        prv.postDelayed(() -> {
            if (page > 3) { //超过3页没有数据了
                mHelper.loadEmpty(page);
            } else { //有数据
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 10; i++)
                    users.add(new User(page + "数据" + i + ":" + System.currentTimeMillis()));
                mHelper.loadSuccess(page, users);
            }
        }, 1500);
    }

    //Adapter:第二个参数表示如何触发onItemClick,-1:不可点击不触发,0或不填整行触发，控件ID点击这个控件才会触发
    private PageRecyclerViewAdapter<User, ViewHolder> mAdapter
            = new PageRecyclerViewAdapter<User, ViewHolder>(R.layout.item_one_row) {
        @Override
        protected PageViewHolder getViewHolder(View root, int index) {
            return new ViewHolder(root);
        }

        @Override
        protected void bindView(ViewHolder holder, User obj, int position, int index) {
            holder.tv.setText(obj.getName());
        }

        @Override
        protected void onItemClick(View view, User obj, int position) {
            Toast.makeText(PageOneRowActivity.this, "点击：" + obj.getName(), Toast.LENGTH_SHORT).show();
        }
    };

    //ViewHolder
    class ViewHolder extends PageViewHolder {

        @BindView(R.id.tv)
        TextView tv;

        public ViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
