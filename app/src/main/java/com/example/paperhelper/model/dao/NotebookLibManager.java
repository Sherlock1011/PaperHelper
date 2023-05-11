package com.example.paperhelper.model.dao;

import com.example.lib_common_util.JsonModuleUtil;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.utils.FileHelper;

import java.io.File;
import java.util.List;

/**
 * @author Sherlock
 * 笔记本列表管理者
 */
public class NotebookLibManager {
    private List<NotebookItem> notebookList;

    public NotebookLibManager(List<NotebookItem> noteBookList) {
        this.notebookList = noteBookList;
    }

    /**
     * 新建笔记
     * @param item
     */
    public void addNotebook(NotebookItem item){
        notebookList.add(item);
        String notebook_json = JsonModuleUtil.parseModuleToJson(item);
//        FileHelper.getInstance().writeFile(item.getFile().getAbsolutePath(), notebook_json, false);
    }

    public void removeNotebook(NotebookItem item){
        notebookList.remove(item);
    }

    /**
     * 更新笔记内容
     * @param item
     * @param content
     */
    public void updateNotebook(NotebookItem item, String content) {
        int position = Model.getInstance().getNotebookLibManager().getNotebookList().indexOf(item);
//        FileHelper.getInstance().writeFile(item.getFile().getAbsolutePath(), content, false);
    }

    public List<NotebookItem> getNotebookList() {
        return notebookList;
    }

    /**
     * 读取本地存储的笔记文件
     */
    public void loadLocalNoteBook(){
        File folder = new File(FinalValue.NOTEBOOK_SRC);
        if(folder.exists()){
            //获取文件夹下的所有文件
            File[] tempList = folder.listFiles();
            if(tempList.length == 0){
                return;
            }
            for (File file : tempList){
                String notebook_json = FileHelper.getInstance().readFile(file.getAbsolutePath());
                NotebookItem notebookItem = (NotebookItem) JsonModuleUtil.parseJsonToModule(notebook_json, NotebookItem.class);
                notebookList.add(notebookItem);
            }
        }
        else{
            folder.mkdir();
        }
    }
}
