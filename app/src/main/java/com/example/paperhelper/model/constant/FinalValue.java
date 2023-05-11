package com.example.paperhelper.model.constant;

import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.utils.TestData;
import com.paper.greendao.PageUrlModelDao;

/**
 * @author Sherlock
 * 默认值
 */
public class FinalValue {
    /**
     * 向数据库中插入初始url列表值
     * @param dao
     */
    public static void insertFinalUrl(PageUrlModelDao dao){
        dao.insert(TestData.getTestPageUrlItem());
        dao.insert(new PageUrlModel("IEEE", "https://ieeexplore.ieee.org/Xplore/home.jsp"));
        dao.insert(new PageUrlModel("Paper with code", "https://paperswithcode.com/"));
        dao.insert(new PageUrlModel("CNKI","https://www.cnki.net/"));
        dao.insert(new PageUrlModel("Nature", "https://www.nature.com/"));
        dao.insert(new PageUrlModel("Google Scholar","https://scholar.google.com.hk/?hl=zh-CN"));
        dao.insert(new PageUrlModel("ACM", "https://dl.acm.org/journal/tog/"));
    }

    /**
     * 文献保存路径
     */
    public static final String FOLDER_SRC = "/data/data/com.example.paperhelper/files/file/";
    public static final String NOTEBOOK_SRC = "/data/data/com.example.paperhelper/files/notebooks/";
    public static final String NOTEBOOK_CONTENT = "<html><body></body></html>";

    public static final int ADD_FINISH_COUNT = 1;
    public static final int SUB_FINISH_COUNT = 0;
    public static final int FLUSH_PROGRESS = 2;
    public static final int ADD_SCHEDULE = 3;
    public static final int DEL_SCHEDULE = 4;
}
