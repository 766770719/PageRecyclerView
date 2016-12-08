package com.xzh.pagerv.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xzh.pagerv.demo.module.User;
import com.xzh.pagerv.rv.PageRecyclerView;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 单行RV页面
 * Created by xiezihao on 16/12/8.
 */
public class OneRowActivity extends Activity {

    @BindView(R.id.prv)
    PageRecyclerView prv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one_row);
        ButterKnife.bind(this);

        //初始化RV
        prv.init(new LinearLayoutManager(this), true, mAdapter);

        //创建测试数据
        List<User> users = new ArrayList<>();
        for (int i = 0; i < 30; i++)
            users.add(new User("用户" + i));

        //绑定数据
        mAdapter.list().addAll(users);
        mAdapter.notifyDataSetChanged();
    }

    //Adapter:第二个参数表示如何触发onItemClick,-1:不可点击不触发,0或不填整行触发，控件ID点击这个控件才会触发
    private PageRecyclerViewAdapter<User, ViewHolder> mAdapter
            = new PageRecyclerViewAdapter<User, ViewHolder>(R.layout.item_one_row, 0) {
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
            Toast.makeText(OneRowActivity.this, "点击：" + obj.getName(), Toast.LENGTH_SHORT).show();
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
