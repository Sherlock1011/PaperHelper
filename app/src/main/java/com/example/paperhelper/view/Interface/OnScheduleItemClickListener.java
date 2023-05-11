package com.example.paperhelper.view.Interface;

import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

import com.example.paperhelper.model.bean.Schedule;

public interface OnScheduleItemClickListener {
    /**
     * 日程提醒列表点击事件
     * @param parent
     * @param view
     * @param position
     * @param item
     */
    void onItemClick(RecyclerView parent, View view, int position, Schedule item);
}
