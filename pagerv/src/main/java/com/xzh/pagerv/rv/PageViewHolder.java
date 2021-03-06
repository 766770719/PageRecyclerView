package com.xzh.pagerv.rv;

import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * 基础ViewHolder
 */
public class PageViewHolder extends RecyclerView.ViewHolder {

    //可点击的View缓存,tag用于点击位置缓存
    public View clickView;

    public PageViewHolder(View view) {
        super(view);
    }

    /**
     * FindView
     *
     * @param id view id
     * @return View
     */
    public <V> V findView(int id) {
        return (V) itemView.findViewById(id);
    }
}