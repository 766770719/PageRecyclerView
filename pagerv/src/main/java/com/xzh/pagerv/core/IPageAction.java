package com.xzh.pagerv.core;

import java.util.List;

/**
 * 分页行为接口
 * Created by xiezihao on 16/12/5.
 */

public interface IPageAction<K,T> {

    void refresh(K key);

    void loadData(K key);

    void onLoadSuccess(K key, List<T> data);

    void onLoadFailed(K key);

}
