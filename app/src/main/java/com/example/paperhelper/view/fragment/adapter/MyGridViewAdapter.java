package com.example.paperhelper.view.fragment.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import java.util.List;

/**
 * @author Sherlock
 * 自定义GridView适配器
 */
public abstract class MyGridViewAdapter <T> extends BaseAdapter {
    private List<T> fileList;
    private int layout_id;


    public MyGridViewAdapter(){

    }

    public MyGridViewAdapter(List<T> fileList, int layout_id) {
        this.fileList = fileList;
        this.layout_id = layout_id;
    }

    @Override
    public int getCount() {
        return fileList != null ? fileList.size() : 0;
    }

    @Override
    public T getItem(int position) {
        return fileList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = ViewHolder.bind(parent.getContext(), convertView, parent, layout_id, position);
        bindView(holder, getItem(position));
        return holder.getItemView();
    }

    public abstract void bindView(ViewHolder holder, T obj);

    public static class ViewHolder{
        private SparseArray<View> mViews;
        private View item;
        private int position;
        private Context context;

        private ViewHolder(Context context, ViewGroup parent, int layoutRes){
            mViews = new SparseArray<>();
            this.context = context;
            View convertView = LayoutInflater.from(context).inflate(layoutRes, parent, false);
            convertView.setTag(this);
            item = convertView;
        }

        /**
         * 绑定ViewHolder与item
         * @param context
         * @param convertView
         * @param parent
         * @param layoutRes
         * @param position
         * @return
         */
        public static ViewHolder bind(Context context, View convertView, ViewGroup parent,
                                      int layoutRes, int position){
            ViewHolder holder;
            if(convertView == null){
                holder = new ViewHolder(context, parent, layoutRes);
            }
            else{
                holder = (ViewHolder) convertView.getTag();
                holder.item = convertView;
            }
            holder.position = position;
            return holder;
        }


        @SuppressWarnings("unchecked")
        public <T extends View> T getView(int id) {
            T t = (T) mViews.get(id);
            if (t == null) {
                t = (T) item.findViewById(id);
                mViews.put(id, t);
            }
            return t;
        }

        public View getItemView(){return item;}
        public int getItemPosition(){return position;}

        /**
         * 设置文字
         * @param id
         * @param text
         * @return
         */
        public ViewHolder setText(int id, CharSequence text){
            View view = getView(id);
            if(view instanceof TextView){
                ((TextView) view).setText(text);
            }
            return this;
        }

        /**
         * 设置字体样式
         * @param id
         * @param typeface
         * @return
         */
        public ViewHolder setTypeface(int id, Typeface typeface){
            View view = getView(id);
            if(view instanceof TextView){
                ((TextView) view).setTypeface(typeface);
            }
            return this;
        }

        /**
         * 设置图片
         * @param id
         * @param drawable
         * @return
         */
        public ViewHolder setImageResource(int id, int drawable){
            View view = getView(id);
            if(view instanceof ImageView){
//                ((ImageView) view).setImageResource(drawable);

            }
            else{
                view.setBackgroundResource(drawable);
            }
            return this;
        }

        /**
         * 设置点击监听
         * @param id
         * @param listener
         * @return
         */
        public ViewHolder setOnClickListener(int id, View.OnClickListener listener){
            getView(id).setOnClickListener(listener);
            return this;
        }

        /**
         * 设置长按点击监听
         * @param id
         * @param listener
         * @return
         */
        public ViewHolder setOnLongClickListener(int id, View.OnLongClickListener listener){
            getView(id).setOnLongClickListener(listener);
            return this;
        }

        /**
         * 设置可见
         * @param id
         * @param visible
         * @return
         */
        public ViewHolder setVisibility(int id, int visible) {
            getView(id).setVisibility(visible);
            return this;
        }

        /**
         * 设置标签
         * @param id
         * @param obj
         * @return
         */
        public ViewHolder setTag(int id, Object obj){
            getView(id).setTag(obj);
            return this;
        }
    }
}
