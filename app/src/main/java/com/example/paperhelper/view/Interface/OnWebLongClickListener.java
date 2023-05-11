package com.example.paperhelper.view.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paperhelper.model.bean.PageUrlModel;

/**
 * @author Sherlock
 * 长按点击事件监听器
 */
public interface OnWebLongClickListener {
    /**
     * 网址列表长按点击事件监听
     * @param parent
     * @param view
     * @param position
     * @param item
     */
    void onLongClick(RecyclerView parent, View view, int position, PageUrlModel item);
}
