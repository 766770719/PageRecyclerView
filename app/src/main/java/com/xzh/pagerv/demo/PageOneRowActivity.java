package com.xzh.pagerv.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xzh.pagerv.demo.module.User;
import com.xzh.pagerv.helper.IndexPageHelper;
import com.xzh.pagerv.refresh.PageSwipeRefreshLayout;
import com.xzh.pagerv.rv.PageRecyclerView;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;
import com.xzh.pagerv.status.DefaultContentStatusView;
import com.xzh.pagerv.status.DefaultFooterStatusView;

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
    DefaultContentStatusView csv;

    private IndexPageHelper<User, ViewHolder> mHelper = new IndexPageHelper<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_page_one_row);
        ButterKnife.bind(this);

        //初始化RV
        prv.init(new LinearLayoutManager(this), true, mAdapter);

        //初始化分页Helper:1.可以不要下拉控件，设置为null即可  2.可以不要footer，即为单页数据效果，footer设置为null即可
        mHelper.init(mAdapter, psrl, csv, new DefaultFooterStatusView(this),
                "数据加载中...", "正在获取下一页数据");
        //开始加载，并设置默认的页标
        mHelper.start(1, this::loadPage, false);
    }

    /**
     * 加载页
     *
     * @param page 页标
     */
    private void loadPage(int page) {
        //测试数据
        prv.postDelayed(() -> {
            if (page == 1) {
                mAdapter.clearAll();
            }
            if (page == 1 && !prv.isSelected()) { //第一页失败
                mHelper.loadFailed(page, "网络错误！1", "第1次失败", "网络错误，点击重新加载下一页", null);
                prv.setSelected(true);
            } else if (page == 2 && prv.isSelected()) { //第二页失败
                mHelper.loadFailed(page, "网络错误！2", "第2次失败", "网络错误，点击重新加载下一页2", null);
                prv.setSelected(false);
            } else if (page > 3) { //空
                mHelper.loadSuccess(page, null, "第三页空", "尾部第三页空");
            } else {
                List<User> users = new ArrayList<>();
                for (int i = 0; i < 10; i++)
                    users.add(new User(page + "数据" + i + ":" + System.currentTimeMillis()));
                mHelper.loadSuccess(page, users, "空else", "尾部空else");
            }
        }, 1500);
    }

    //Adapter
    private PageRecyclerViewAdapter<User, ViewHolder> mAdapter = new PageRecyclerViewAdapter<User, ViewHolder>() {
        @Override
        public int getLayoutId(User obj) {
            return R.layout.item_one_row;
        }

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
