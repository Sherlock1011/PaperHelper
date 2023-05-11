package com.example.paperhelper.view.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paperhelper.model.bean.PageUrlModel;

/**
 * @author Sherlock
 * recyclerView成员点击时间监听器
 */
public interface OnWebItemClickListener {
    /**
     * 文献网址列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param item
     */
    void onItemClick(RecyclerView parent, View view, int position, PageUrlModel item);
}
