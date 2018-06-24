package com.xzh.pagerv.demo;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xzh.pagerv.demo.module.Pet;
import com.xzh.pagerv.demo.module.User;
import com.xzh.pagerv.rv.PageRecyclerView;
import com.xzh.pagerv.rv.PageRecyclerViewAdapter;
import com.xzh.pagerv.rv.PageViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 两行样式页面
 * Created by xiezihao on 16/12/8.
 */
public class TwoRowActivity extends Activity {

    @BindView(R.id.prv)
    PageRecyclerView prv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two_row);
        ButterKnife.bind(this);

        //初始化
        prv.init(new LinearLayoutManager(this), true, mAdapter);

        //创建测试数据
        List<Object> data = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            data.add(new User("User" + i));
        for (int i = 0; i < 10; i++)
            data.add(new Pet("Pet" + i));
        for (int i = 10; i < 20; i++)
            data.add(new User("User" + i));
        for (int i = 10; i < 20; i++)
            data.add(new Pet("Pet" + i));

        //绑定数据
        mAdapter.list().addAll(data);
        mAdapter.notifyDataSetChanged();
    }

    //可使用触发onItemClick的ViewID
    private PageRecyclerViewAdapter<Object, PageViewHolder> mAdapter = new PageRecyclerViewAdapter<Object, PageViewHolder>() {

        @Override
        public int getLayoutId(Object obj, int position) {
            return isPet(obj) ? R.layout.item_two_row_2 : R.layout.item_two_row_1;
        }

        @Override
        public int getClickViewId(int layoutId) {
            if (layoutId == R.layout.item_two_row_1)
                return R.id.v_click_1;
            else if (layoutId == R.layout.item_two_row_2)
                return R.id.v_click_2;
            return super.getClickViewId(layoutId);
        }

        @Override
        protected PageViewHolder getViewHolder(View root, int layoutID) {
            if (layoutID == R.layout.item_two_row_2)  //是第二行的布局ID，创建对应的VH
                return new ViewHolder2(root);
            else  //默认第一行样式
                return new ViewHolder1(root);
        }

        @Override
        protected void bindView(PageViewHolder holder, Object obj, int position, int index) {
            if (isPet(obj)) { //是Pet，转换对象和VH为Pet相关的对象
                ViewHolder2 holder2 = (ViewHolder2) holder;
                Pet pet = (Pet) obj;
                holder2.tv_2.setText(pet.getName());
            } else { //默认的第一行样式
                ViewHolder1 holder1 = (ViewHolder1) holder;
                User user = (User) obj;
                holder1.tv_1.setText(user.getName());
            }
        }

        @Override
        protected void onItemClick(View view, Object obj, int position) {
            if (isPet(obj)) {
                Toast.makeText(TwoRowActivity.this, "数据:" + ((Pet) obj).getName(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(TwoRowActivity.this, "数据:" + ((User) obj).getName(), Toast.LENGTH_SHORT).show();
            }
        }

        /**
         * 数据类型是否是Pet
         * @param obj 数据
         * @return true 是Pet
         */
        private boolean isPet(Object obj) {
            return obj instanceof Pet;
        }
    };

    //第一种行样式
    class ViewHolder1 extends PageViewHolder {

        @BindView(R.id.tv_1)
        TextView tv_1;

        public ViewHolder1(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

    //第二种行样式
    class ViewHolder2 extends PageViewHolder {

        @BindView(R.id.tv_2)
        TextView tv_2;

        public ViewHolder2(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
