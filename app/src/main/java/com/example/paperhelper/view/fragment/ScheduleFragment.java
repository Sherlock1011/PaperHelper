package com.example.paperhelper.view.fragment;

import static com.example.paperhelper.model.constant.FinalValue.ADD_FINISH_COUNT;
import static com.example.paperhelper.model.constant.FinalValue.ADD_SCHEDULE;
import static com.example.paperhelper.model.constant.FinalValue.DEL_SCHEDULE;
import static com.example.paperhelper.model.constant.FinalValue.FLUSH_PROGRESS;
import static com.example.paperhelper.model.constant.FinalValue.SUB_FINISH_COUNT;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.paperhelper.R;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.Schedule;
import com.example.paperhelper.utils.DateUtil;
import com.example.paperhelper.view.activity.dialog.DialogManager;
import com.example.paperhelper.view.Interface.OnScheduleItemClickListener;
import com.example.paperhelper.view.fragment.adapter.ScheduleListBaseAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView;
import com.qmuiteam.qmui.widget.QMUIProgressBar;

import java.util.Date;
import java.util.List;

public class ScheduleFragment extends Fragment implements View.OnClickListener{
    private Context context;
    private int schedulePosition = 0;
    private ImageButton menuButton;
    private TextView title_tv;
    private TextView scheduleCount_tv;
    private TextView non_schedule_tv;
    private TextView time_tv;
    private ConstraintLayout non_schedule_layout;

    private QMUIContinuousNestedBottomRecyclerView unFinishRecyclerView;
    private List<Schedule> scheduleList;
    private ScheduleListBaseAdapter adapter;
    private QMUIProgressBar progressBar;
    private TextView degree_tv;
    private int finishCount = 0;
    private int scheduledCount = 0;
    private LinearLayout schedule_content_layout;
    private QMUILinearLayout duration_layout;
    private QMUILinearLayout difficulty_layout;
    private QMUILinearLayout important_layout;
    private QMUILinearLayout note_layout;

    private TextView duration_title_tv;
    private TextView duration_tv;

    private TextView difficulty_title_tv;
    private TextView difficulty_tv;

    private TextView important_title_tv;
    private Switch importance_switch;

    private TextView note_title_tv;

    private FloatingActionButton add_schedule_btn;
    private ImageButton edit_schedule_btn;
    private ImageButton remove_schedule_btn;

    private Handler handler = new Handler(Looper.getMainLooper()){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch(msg.what) {
                case ADD_FINISH_COUNT:
                    finishCount++;
                    additionProgressing();
                    Log.e("processing", "check finish part:" + "finish count = " + finishCount + " " + "schedule count = " + scheduledCount);
                    break;

                case SUB_FINISH_COUNT:
                    finishCount--;
                    decreaseProgressing();
                    Log.e("processing", "check unfinish part:" + "finish count = " + finishCount + " " + "schedule count = " + scheduledCount);
                    break;

                case FLUSH_PROGRESS:
                    progressBar.setProgress((int) msg.obj);
                    break;

                case ADD_SCHEDULE:
                    String content = (String) msg.obj;
                    Schedule schedule = new Schedule(content);
                    //添加schedule到列表中，并作一系列的显示操纵
                    addSchedule(schedule);
                    break;

                case DEL_SCHEDULE:
                    Schedule delSchedule = scheduleList.get(schedulePosition);
                    deleteSchedule(delSchedule);

                    break;
            }
        }
    };

    public ScheduleFragment(Context context) {
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_schedule, container, false);
        initList();
        initView(view);
        return view;
    }

    private void initList(){
        scheduleList = Model.getInstance().getScheduleList();
        scheduledCount = scheduleList.size();
        finishCount = 0;
        //初始化已完成的日程个数
        for (Schedule s : scheduleList){
            if (s.isFinished()){
                finishCount++;
            }
        }
        Log.e("processing", "initial part:" + "finish count = " + finishCount + " " + "schedule count = " + scheduledCount);
    }

    /**
     * 初始化控件
     * @param view
     */
    private void initView(View view) {
        initLeftView(view);
        initRightView(view);
        //设置字体样式
        setFontStyle();
        setScheduleTime();
        //设置日程列表不空的显示效果
        if(!scheduleList.isEmpty()){
            non_schedule_layout.setVisibility(View.GONE);
        }
        else {
            schedule_content_layout.setVisibility(View.GONE);
        }
    }

    /**
     * 初始化左布局
     * @param view
     */
    private void initLeftView(View view){
        menuButton = (ImageButton) view.findViewById(R.id.schedule_menu);
        title_tv = (TextView) view.findViewById(R.id.todo_title);
        scheduleCount_tv = (TextView) view.findViewById(R.id.schedule_count);
        non_schedule_tv = (TextView) view.findViewById(R.id.non_schedule_tv);
        time_tv = (TextView) view.findViewById(R.id.time_tv);
        unFinishRecyclerView = (QMUIContinuousNestedBottomRecyclerView) view.findViewById(R.id.unfinish_rv);
        non_schedule_layout = (ConstraintLayout) view.findViewById(R.id.non_schedule_layout);

        add_schedule_btn = (FloatingActionButton) view.findViewById(R.id.add_schedule_btn);
        add_schedule_btn.setOnClickListener(this);


        if (scheduledCount == 0){
            scheduleCount_tv.setVisibility(View.GONE);
        }
        else {
            scheduleCount_tv.setText(scheduleList.size() + "TO DO");
        }

        //初始化recyclerView
        initRecyclerView();
    }

    /**
     * 初始化右布局
     */
    private void initRightView(View view){
        schedule_content_layout = (LinearLayout) view.findViewById(R.id.schedule_content_layout);
        progressBar = (QMUIProgressBar) view.findViewById(R.id.progress_bar);
        degree_tv = (TextView) view.findViewById(R.id.degree_tv);
        duration_layout = (QMUILinearLayout) view.findViewById(R.id.schedule_duration_layout);
        duration_layout.setRadius(20);
        difficulty_layout = (QMUILinearLayout) view.findViewById(R.id.schedule_difficulty_layout);
        difficulty_layout.setRadius(20);
        important_layout = (QMUILinearLayout) view.findViewById(R.id.schedule_important_layout);
        important_layout.setRadius(20);
        note_layout = (QMUILinearLayout) view.findViewById(R.id.schedule_note_layout);
        note_layout.setRadius(20);
        //设置progress bar样式
        setProgressBar();

        duration_title_tv = (TextView) view.findViewById(R.id.timer_title_tv);
        duration_tv = (TextView) view.findViewById(R.id.duration_tv);

        difficulty_title_tv = (TextView) view.findViewById(R.id.difficulty_title_tv);
        difficulty_tv = (TextView) view.findViewById(R.id.difficulty_tv);

        important_title_tv = (TextView) view.findViewById(R.id.importance_title_tv);
        importance_switch = (Switch) view.findViewById(R.id.important_switch);

        note_title_tv = (TextView) view.findViewById(R.id.schedule_note_title_tv);

        edit_schedule_btn = (ImageButton) view.findViewById(R.id.schedule_edit_btn);
        edit_schedule_btn.setOnClickListener(this);

        remove_schedule_btn = (ImageButton) view.findViewById(R.id.schedule_delete_btn);
        remove_schedule_btn.setOnClickListener(this);

        //如果日程列表不为空，始终显示列表中第一个日程信息
        if(scheduledCount != 0){
            showScheduleItemInfo(0);
        }

        //设置switch监听事件
        importance_switch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                scheduleList.get(schedulePosition).setImportant(b);
            }
        });
    }

    /**
     * 显示所选位置的日程单项
     * @param position 指定的位置
     */
    private void showScheduleItemInfo(int position){
        Schedule schedule_item = scheduleList.get(position);
        duration_tv.setText(schedule_item.getDuration()+"h");
        difficulty_tv.setText(schedule_item.getDifficulty());
        importance_switch.setChecked(schedule_item.isImportant());
    }

    /**
     * 设置控件字体样式
     */
    private void setFontStyle(){
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text.ttf");
        title_tv.setTypeface(typeface);
        scheduleCount_tv.setTypeface(typeface);
        non_schedule_tv.setTypeface(typeface);
        time_tv.setTypeface(typeface);
        degree_tv.setTypeface(typeface);
        duration_title_tv.setTypeface(typeface);
        duration_tv.setTypeface(typeface);
        difficulty_title_tv.setTypeface(typeface);
        difficulty_tv.setTypeface(typeface);
        important_title_tv.setTypeface(typeface);
        note_title_tv.setTypeface(typeface);
    }

    /**
     * 设置日期
     */
    private void setScheduleTime(){
        Date date = new Date();
        String time = DateUtil.getDateString(date, "yyyy/MM/dd");
        String[] dates = time.split("/");
        time = dates[2] + "/" + dates[1] + "/" + dates[0];
        time_tv.setText(time);
    }

    /**
     * 初始化Schedule List
     */
    private void initRecyclerView() {
        adapter = new ScheduleListBaseAdapter(context, unFinishRecyclerView, scheduleList, handler);

        adapter.setOnItemClickListener(new OnScheduleItemClickListener() {
            @Override
            public void onItemClick(RecyclerView parent, View view, int position, Schedule item) {
                schedulePosition = position;
                //显示所选日程的详细信息
                showScheduleItemInfo(position);
            }
        });

        LinearLayoutManager manager = new LinearLayoutManager(context);
        manager.setOrientation(LinearLayoutManager.VERTICAL);

        unFinishRecyclerView.setLayoutManager(manager);
        unFinishRecyclerView.setAdapter(adapter);
    }


    /**
     * 设置progress bar样式
     */
    private void setProgressBar() {
        progressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                if(maxValue == 0){
                    return "0%";
                }
                else {return 100 * value / maxValue + "%";}
            }
        });
        progressBar.setMaxValue(100);
        if (scheduledCount != 0){
            progressBar.setProgress(100*finishCount/scheduledCount);
        }
    }

    /**
     * 增加的progress进程
     */
    private void additionProgressing(){
        //开启动态刷新进度条线程
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 100*(finishCount-1)/scheduledCount; i <= 100*finishCount/scheduledCount; i++){
                    flushProgressing(i);
                }
            }
        });
    }

    /**
     * 降低的progress进程
     */
    private void decreaseProgressing(){
        //开启动态刷新进度条线程
        Model.getInstance().getGlobalThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                for(int i = 100*(finishCount+1)/scheduledCount; i >= 100*finishCount/scheduledCount; i--){
                    flushProgressing(i);
                }
            }
        });
    }

    /**
     * 刷新progress bar
     * @param progress
     */
    private void flushProgressing(int progress) {
        Message msg = Message.obtain();
        msg.what = FLUSH_PROGRESS;
        msg.obj = progress;
        handler.sendMessage(msg);
    }

    @Override
    public void onClick(View view) {
        switch(view.getId()) {
            case R.id.add_schedule_btn:
                // 添加日程
                DialogManager.getInstance().addScheduleDialogShow(getActivity(), handler);
                break;

            case R.id.schedule_edit_btn:
                // 编辑日程
                editSchedule(schedulePosition);
                break;

            case R.id.schedule_delete_btn:
                // 删除日程
                DialogManager.getInstance().deleteScheduleItemDialogShow(getActivity(), handler);
                break;
        }
    }

    /**
     * 添加日程
     */
    private void addSchedule(Schedule schedule){
        scheduleList.add(schedule);
        scheduledCount++;
        progressBar.setProgress(100*finishCount/scheduledCount);
//        Log.e("COUNT", "scheduledCount = " + scheduledCount + " finishCount = " + finishCount);
        if (scheduledCount == 1){
            scheduleCount_tv.setVisibility(View.VISIBLE);
            non_schedule_layout.setVisibility(View.GONE);
            schedule_content_layout.setVisibility(View.VISIBLE);
            showScheduleItemInfo(0);
        }
        adapter.notifyDataSetChanged();
        Log.e("processing", "add part:" + "finish count = " + finishCount + " " + "schedule count = " + scheduledCount);
    }

    /**
     * 编辑日程
     */
    private void editSchedule(int position){
        DialogManager.getInstance().editScheduleDialogShow(getActivity(), scheduleList, position, adapter);
    }

    /**
     * 删除日程
     */
    private void deleteSchedule(Schedule schedule){
        Toast.makeText(context, "schedule position:" + scheduleList.indexOf(schedule), Toast.LENGTH_SHORT).show();
        scheduleList.remove(schedule);
        scheduledCount--;
        if (schedule.isFinished()){
            finishCount--;
        }

        if(scheduledCount == 0){
            scheduleCount_tv.setVisibility(View.GONE);
            non_schedule_layout.setVisibility(View.VISIBLE);
            schedule_content_layout.setVisibility(View.GONE);
        }
        else {
            progressBar.setProgress(100*finishCount/scheduledCount);
        }
        adapter.notifyDataSetChanged();
        Log.e("processing", "delete part:" + "finish count = " + finishCount + " " + "schedule count = " + scheduledCount);
    }
}