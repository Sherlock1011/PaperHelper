package com.example.paperhelper.model.dao;

import com.example.paperhelper.model.bean.FileItem;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.utils.TestData;

import java.io.File;
import java.util.List;

/**
 * @author Sherlock
 * 文件列表管理者
 * 负责文献库的修改
 */
public class FileLibManager {
    private List<FileItem> fileList;

    public FileLibManager(List<FileItem> fileList) {
        this.fileList = fileList;
    }

    public List<FileItem> getFileList() {
        return fileList;
    }

    /**
     * 添加文件单项
     * @param fileItem
     */
    public void addFile(FileItem fileItem){
        fileList.add(fileItem);
    }

    /**
     * 加载本地的文献
     */
    public void loadLocalFile(){
        File folder = new File(FinalValue.FOLDER_SRC);
        if(folder.exists()){
            //获取文件夹下的所有文件
            File[] tempList = folder.listFiles();
            if(tempList.length == 0){
                return;
            }
            for (File file : tempList){
                fileList.add(new FileItem(file));
            }
        }
        else{
            folder.mkdir();
        }
    }
}
