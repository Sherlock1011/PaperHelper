package com.example.paperhelper.view.fragment.adapter;

import static com.example.paperhelper.model.constant.FinalValue.ADD_FINISH_COUNT;
import static com.example.paperhelper.model.constant.FinalValue.SUB_FINISH_COUNT;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paperhelper.R;
import com.example.paperhelper.model.bean.Schedule;
import com.example.paperhelper.view.Interface.OnScheduleItemClickListener;
import com.qmuiteam.qmui.layout.QMUILinearLayout;
import com.qmuiteam.qmui.nestedScroll.QMUIContinuousNestedBottomRecyclerView;

import java.util.List;


public class ScheduleListBaseAdapter extends QMUIContinuousNestedBottomRecyclerView.Adapter<ScheduleListBaseAdapter.MyViewHolder> implements View.OnClickListener {
    private List<Schedule> scheduleList;
    private Context context;
    private int selectedPosition = 0;
    private View inflater;
    private QMUIContinuousNestedBottomRecyclerView recyclerView;
    private OnScheduleItemClickListener onItemClickListener;
    private Handler handler;

    /**
     * 注册点击事件监听器
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnScheduleItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    public  ScheduleListBaseAdapter(Context context, QMUIContinuousNestedBottomRecyclerView rv, List<Schedule> scheduleList, Handler handler){
        this.context = context;
        this.recyclerView = rv;
        this.scheduleList = scheduleList;
        this.handler = handler;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_schedule, parent, false);
        inflater.setOnClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(inflater, context);
        return myViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        holder.schedule_checkbox.setText(scheduleList.get(position).getContent());
        holder.schedule_checkbox.setChecked(scheduleList.get(position).isFinished());
        holder.schedule_checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if(scheduleList.get(position).isFinished() != isChecked){
                    scheduleList.get(position).setFinished(isChecked);
                    if (isChecked){handler.sendEmptyMessage(ADD_FINISH_COUNT);}
                    else {handler.sendEmptyMessage(SUB_FINISH_COUNT);}
                }
            }
        });


        if(selectedPosition == position){
            holder.item_layout.setBackgroundColor(Color.argb(50,29,131,241));
        }
        else {
            holder.item_layout.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return scheduleList.size();
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(recyclerView, view, position, scheduleList.get(position));
            selectedPosition = position;
//            notifyDataSetChanged();
        }
    }


    class MyViewHolder extends RecyclerView.ViewHolder {
        QMUILinearLayout item_layout;
        CheckBox schedule_checkbox;
        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            initView(itemView, context);
        }

        /**
         * 初始化控件
         * @param view
         * @param context
         */
        private void initView(View view, Context context){
            item_layout = (QMUILinearLayout) view.findViewById(R.id.schedule_item_layout);
            schedule_checkbox = (CheckBox) view.findViewById(R.id.schedule_content);
            initFontStyle(context);
            item_layout.setRadius(20);
        }

        private void initFontStyle(Context context){
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text.ttf");
            schedule_checkbox.setTypeface(typeface);
        }
    }
}
