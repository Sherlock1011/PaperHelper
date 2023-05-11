package com.example.paperhelper.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.example.paperhelper.view.activity.PdfAndNoteActivity;
import com.example.paperhelper.R;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.FileItem;
import com.example.paperhelper.view.fragment.adapter.MyGridViewAdapter;

import java.io.File;
import java.util.List;

/**
 * @author Sherlock
 * 文献库fragment
 */
public class PaperLibFragment extends Fragment {
    private Context context;
    private Activity activity;
    private List<FileItem> fileList;

    private TextView emptyText;

    private GridView folderGridView;
    private MyGridViewAdapter gridViewAdapter;

    public PaperLibFragment(Context context, Activity activity){
        this.context = context;
        this.activity = activity;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view;
        fileList = Model.getInstance().getFileLibManager().getFileList();
        if(fileList.isEmpty()){
            //显示空文件页面
            view = inflater.inflate(R.layout.fragment_paper_lib_empty, container, false);
            initEmptyView(view);
        }
        else {
            view = inflater.inflate(R.layout.fragment_paper_lib, container, false);
            initView(view);
        }
        return view;
    }

    /**
     * 初始化空页面
     * @param view
     */
    private void initEmptyView(View view){
        //设置字体样式
        Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text.ttf");
        emptyText = (TextView) view.findViewById(R.id.empty_tv);
        emptyText.setTypeface(typeface);
    }

    /**
     * 初始化非空界面
     * @param view
     */
    private void initView(View view){
        folderGridView = (GridView) view.findViewById(R.id.folder_gv);

        gridViewAdapter = new MyGridViewAdapter<FileItem>(Model.getInstance().getFileLibManager().getFileList(), R.layout.item_folder) {
            @Override
            public void bindView(MyGridViewAdapter.ViewHolder holder, FileItem fileItem) {
                holder.setImageResource(R.id.file_img, fileItem.getImage());
                holder.setText(R.id.file_name_tv, fileItem.getFileName());

                Typeface typeface = Typeface.createFromAsset(context.getAssets(), "fonts/text.ttf");
                holder.setTypeface(R.id.file_name_tv, typeface);
            }
        };

        folderGridView.setAdapter(gridViewAdapter);
        folderGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                File file = fileList.get(position).getFile();
                String file_path = file.getPath();
                Intent intent = new Intent(activity, PdfAndNoteActivity.class);
                //将文件地址传给下一个Activity
                intent.putExtra("file_path", file_path);
                startActivity(intent);
            }
        });

    }

    private void openFileFromOutApp(File file){
        if(!file.exists()){
            return;
        }
        Uri path;
        //选择打开方式
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            //7.0及更高版本，需要通过FileProvider生成Uri，前缀为content://
            path = FileProvider.getUriForFile(context, "com.example.paperhelper.fileprovider", file);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        else{
            //低版本可以直接通过文件生成uri，前缀为file://
            path = Uri.fromFile(file);
        }
        //设置类型
        intent.setDataAndType(path, "*/*");
        Log.e("PATH",path.getPath());
        startActivity(intent);
    }
}