package com.xzh.pagerv.helper;

import java.util.List;

/**
 * 页标PageHelper
 * Created by xiezihao on 16/12/7.
 */
public class IndexPageHelper<T, H> extends PageHelper<Integer, T, H> {

    @Override
    public boolean isFirstPage(Integer key, Integer defaultKey) {
        return key.equals(defaultKey);
    }

    @Override
    public Integer getNextPageKey(Integer key, List<T> data) {
        return key + 1;
    }
}
