package com.example.paperhelper.model.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.paper.greendao.DaoMaster;
import com.paper.greendao.DaoSession;
import com.paper.greendao.PageUrlModelDao;

/**
 * @author Sherlock
 * 数据库管理者
 */
public class DataBaseManager {
    /**
     * 数据库会话
     */
    private DaoSession mSession;
    private SQLiteDatabase db;
    private DaoMaster daoMaster;
    private Context context;

    private PageUrlModelDao pageUrlModelDao;

    public DataBaseManager(Context context){
        this.context = context;
        initDataBase();
    }

    /**
     * 初始化数据库
     */
    private void initDataBase(){
        //获取需要连接的数据库
        DaoMaster.DevOpenHelper devOpenHelper = new DaoMaster.DevOpenHelper(context,"paperHelper.db");
        db = devOpenHelper.getWritableDatabase();
        //创建数据库连接
        daoMaster = new DaoMaster(db);
        //创建数据库会话
        mSession = daoMaster.newSession();
    }

    /**
     * 获取pageUrl的dao
     * @return
     */
    public PageUrlModelDao getPageUrlModelDao(){
        return mSession.getPageUrlModelDao();
    }
}
