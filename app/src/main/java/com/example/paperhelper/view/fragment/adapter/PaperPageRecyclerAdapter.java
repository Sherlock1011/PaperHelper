package com.example.paperhelper.view.fragment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.paperhelper.R;
import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.view.Interface.OnWebItemClickListener;
import com.example.paperhelper.view.Interface.OnWebLongClickListener;
import com.qmuiteam.qmui.layout.QMUILinearLayout;

import java.util.List;

/**
 * @author Sherlock
 * Scholar website recycler view adapter
 */
public class PaperPageRecyclerAdapter extends RecyclerView.Adapter<PaperPageRecyclerAdapter.MyViewHolder> implements View.OnClickListener, View.OnLongClickListener{
    private List<PageUrlModel> urlItemList;
    private Context context;
    private View inflater;
    private RecyclerView recyclerView;
    private OnWebItemClickListener onItemClickListener;
    private OnWebLongClickListener onLongClickListener;


    /**
     * register onItemClickListener
     * @param onItemClickListener
     */
    public void setOnItemClickListener(OnWebItemClickListener onItemClickListener){
        this.onItemClickListener = onItemClickListener;
    }

    /**
     * register onLongItemClickListener
     * @param onLongClickListener
     */
    public void setOnLongClickListener(OnWebLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }

    public PaperPageRecyclerAdapter(Context context, RecyclerView rv, List<PageUrlModel> itemList) {
        this.context = context;
        this.recyclerView = rv;
        this.urlItemList = itemList;
    }

    @Override
    public void onClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if(onItemClickListener != null){
            onItemClickListener.onItemClick(recyclerView, view, position, urlItemList.get(position));
        }
    }

    @Override
    public boolean onLongClick(View view) {
        int position = recyclerView.getChildAdapterPosition(view);
        if(onLongClickListener != null){
            onLongClickListener.onLongClick(recyclerView, view, position, urlItemList.get(position));
        }
        return false;
    }

    /**
     * 创建viewHolder，返回每一项的布局
     * @param parent The ViewGroup into which the new View will be added after it is bound to
     *               an adapter position.
     * @param viewType The view type of the new View.
     *
     * @return
     */
    @NonNull
    @Override
    public PaperPageRecyclerAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        inflater = LayoutInflater.from(context).inflate(R.layout.item_paper_page_url, parent, false);
        //设置事件监听
        inflater.setOnClickListener(this);
        inflater.setOnLongClickListener(this);
        MyViewHolder myViewHolder = new MyViewHolder(inflater, context);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull PaperPageRecyclerAdapter.MyViewHolder holder, int position) {
        holder.pageNameText.setText(urlItemList.get(position).getPageName());
        holder.pageUrlText.setText(urlItemList.get(position).getUrl());
    }

    @Override
    public int getItemCount() {
        return urlItemList.size();
    }



    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView pageNameText;
        TextView pageUrlText;

        QMUILinearLayout pageItem;

        public MyViewHolder(@NonNull View itemView, Context context) {
            super(itemView);
            initView(itemView, context);
        }

        private void initView(View view, Context context){
            pageNameText = (TextView)view.findViewById(R.id.page_name_tv);
            pageUrlText = (TextView)view.findViewById(R.id.page_url_tv);
            pageItem = (QMUILinearLayout)view.findViewById(R.id.page_url_list_item);
            initFontStyle(context);
            pageItem.setRadius(20);
        }

        /**
         * 设置item控件字体
         * @param context
         */
        private void initFontStyle(Context context){
            Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text.ttf");
            pageUrlText.setTypeface(typeface);
            pageNameText.setTypeface(typeface);
        }
    }
}
