package com.example.paperhelper.model.bean;

import com.example.paperhelper.R;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.utils.FileHelper;

import java.io.File;

/**
 * @author Sherlock
 * 笔记本单项
 */
public class NotebookItem {
    private int icon = R.drawable.notebook;
    private String time;
    private String file_name;
    private String path;
    private String content;

    public NotebookItem(File file){
        file_name = file.getName();
        this.path = file.getAbsolutePath();
        time = FileHelper.getInstance().getFileCreateTime(file);
        this.content = FinalValue.NOTEBOOK_CONTENT;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTime() {
        return time;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getFileName() {
        return file_name;
    }

    public void setFileName(String file_name) {
        this.file_name = file_name;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
