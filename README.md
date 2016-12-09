# PageRecyclerView
####用于分页操作的RecyclerView封装

使用这个分页工具的Activity大致如下,即可实现自动控制各种状态UI显示,分页页脚效果,下拉刷新控制(使用了lambda和butterknife):
```java
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

        //初始化分页Helper:1.可以不要下拉控件，设置为null即可  2.可以不要footer，即为单页数据效果，footer设置为null即可
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
                //如果失败情况可以使用
                //mHelper.loadFailed(page,"网络错误！","网络错误，点击重新加载下一页");
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
```
布局XML如下：
```xml
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <com.xzh.pagerv.refresh.PageSwipeRefreshLayout
        android:id="@+id/psrl"
        android:layout_width="match_parent"
        android:layout_height="match_parent">

        <com.xzh.pagerv.rv.PageRecyclerView
            android:id="@+id/prv"
            android:layout_width="match_parent"
            android:layout_height="match_parent" />
    </com.xzh.pagerv.refresh.PageSwipeRefreshLayout>

    <com.xzh.pagerv.demo.view.ContentStatusView
        android:id="@+id/csv"
        android:layout_width="match_parent"
        android:layout_height="match_parent" />

</FrameLayout>
```

其中上述页面中的下拉刷新控件可以自己扩展，如果使用SwipeRefreshLayout可以直接用封装好的PageSwipeRefreshLayout即可，其它自己按如下类似封装：
```java
public class SwipeRefreshLayout extends android.support.v4.widget.SwipeRefreshLayout implements IPageRefreshView {

    public SwipeRefreshLayout(Context context) {
        super(context);
    }

    @Override
    public void setListener(OnPageRefreshListener listener) {
        setOnRefreshListener(listener::onRefresh);
    }

    @Override
    public void showRefreshView(boolean show) {
        setRefreshing(show);
    }
}
```
上述页面的ContentStatusView和FooterStatusView(Demo中这两个View除了布局不一样，其它都一样，所以扩展都一样)可以自己扩展，类似写法如下：
```java
public class ContentStatusView/**FooterStatusView*/ extends PageStatusView {

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
```
其中最核心的分页方式扩展需要自己定义，已经封好一个通过int页标分页方式的扩展，其它具体按情况可以自己定义，大概扩展如下:
```java
public class IndexPageHelper<T, H> extends PageHelper<Integer, T, H> {

    @Override
    protected boolean isFirstPage(Integer key, Integer defaultKey) {
        return key.equals(defaultKey);
    }

    @Override
    protected Integer getNextPageKey(Integer key, List<T> data) {
        return key + 1;
    }
}
```