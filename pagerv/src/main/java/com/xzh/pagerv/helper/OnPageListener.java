package com.xzh.pagerv.helper;

/**
 * 分页时的监听
 * Created by xiezihao on 16/12/7.
 */
public interface OnPageListener<K> {

    /**
     * 加载页
     *
     * @param key 分页标识关键字
     */
    void loadPage(K key);
}
