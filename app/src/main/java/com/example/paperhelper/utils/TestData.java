package com.example.paperhelper.utils;

import com.example.paperhelper.model.bean.FileItem;
import com.example.paperhelper.model.bean.PageUrlModel;

/**
 * @author Sherlock
 * 测试数据
 */
public class TestData {
    public static PageUrlModel getTestPageUrlItem(){
        PageUrlModel item = new PageUrlModel();
        item.setPageName("MUST Library");
        item.setUrl("https://lib.must.edu.mo/");
        return item;
    }

    public static FileItem getTestFileItem(){
        FileItem fileItem = new FileItem("Image Segmentation");
        return fileItem;
    }
}
