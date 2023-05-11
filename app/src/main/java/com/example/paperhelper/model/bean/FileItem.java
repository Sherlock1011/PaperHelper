package com.example.paperhelper.model.bean;

import com.example.paperhelper.R;

import java.io.File;

/**
 * @author Sherlock
 * 文件数据模型
 * 用于存储文件图标 文件名 文件信息
 */
public class FileItem {
    private int image = R.drawable.pdf;
    private String fileName;
    private File file;

//    public FileItem(String path){
//        this.file = new File(path);
//        fileName = file.getName().trim();
//    }

    public FileItem(File file){
        this.file = file;
        this.fileName = file.getName().trim();
    }

    public FileItem(String path, int drawable){
        this.file = new File(path);
        this.image = drawable;
        this.fileName = file.getName().trim();
    }

    public FileItem(String fileName){
        this.fileName = fileName;
    }

    public int getImage() {
        return image;
    }

    public String getFileName() {
        return fileName;
    }

    public File getFile() {
        return file;
    }
}
