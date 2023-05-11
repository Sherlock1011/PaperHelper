package com.example.paperhelper.model;

import android.content.Context;

import com.example.paperhelper.model.bean.FileItem;
import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.model.bean.Schedule;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.model.dao.DataBaseManager;
import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.model.dao.FileLibManager;
import com.example.paperhelper.model.dao.NotebookLibManager;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Sherlock
 * 数据模型层 全局类
 * 控制层与数据层之间的数据交互
 * 单例对象
 */
public class Model {
    private Context mContext;

    /**
     * 单例对象
     */
    private static Model model;

    private DataBaseManager dataBaseManager;
    private List<PageUrlModel> urlList;

    private FileLibManager fileLibManager;
    private List<FileItem> fileList;

    private NotebookLibManager notebookLibManager;
    private List<NotebookItem> notebookList;

    private List<Schedule> scheduleList;


    /**
     * 创建全局线程池
     */
    private ExecutorService executors = Executors.newCachedThreadPool();



    private Model(){

    }

    /**
     * 初始化数据模型
     * Model层的初始化全在Application中进行
     * @param context
     */
    public synchronized void init(Context context){
        mContext = context;

        //初始化数据管理者
        dataBaseManager = new DataBaseManager(mContext);
        fileList = new ArrayList<>();
        fileLibManager = new FileLibManager(fileList);
        notebookList = new ArrayList<>();
        notebookLibManager = new NotebookLibManager(notebookList);

        //初始化数据
        initData();
    }

    /**
     * 初始化程用到的所有数据
     */
    private void initData(){
        initDataBase();
        initFileLib();
        initNotebookList();
    }

    /**
     * 获取数据库数据
     */
    private void initDataBase(){
        loadUrlData();
        loadScheduleData();
    }

    /**
     * 从数据库中获取网址信息
     */
    private void loadUrlData(){
        //从数据库中获取数据
        urlList = dataBaseManager.getPageUrlModelDao().loadAll();
        //为软件插入初始数据
        if (urlList.isEmpty()){
            FinalValue.insertFinalUrl(dataBaseManager.getPageUrlModelDao());
            urlList = dataBaseManager.getPageUrlModelDao().loadAll();
        }
    }

    /**
     * 从数据库中读取日程信息
     */
    private void loadScheduleData() {
        scheduleList = new ArrayList<>();
        Schedule schedule = new Schedule("Read UNTER");
        schedule.setFinished(true);
        schedule.setImportant(true);
        scheduleList.add(schedule);
        scheduleList.add(new Schedule("Read Transformer"));
        scheduleList.add(new Schedule("Write overview"));
        scheduleList.add(new Schedule("Search image segmentation paper"));
        scheduleList.add(new Schedule("Write Software report"));
//        scheduleList.add(new Schedule("写论文4"));
//        scheduleList.add(new Schedule("写论文5"));
//        scheduleList.add(new Schedule("写论文6"));
//        scheduleList.add(new Schedule("写论文7"));
//        scheduleList.add(new Schedule("写论文8"));
//        scheduleList.add(new Schedule("写论文9"));
//        scheduleList.add(new Schedule("写论文10"));
//        scheduleList.add(new Schedule("写论文11"));
    }

    /**
     * 获取文件数据
     */
    private void initFileLib(){
        //TODO 从文件夹中读取所有文件
//        fileLibManager.addTestList();
        fileLibManager.loadLocalFile();
    }

    private void initNotebookList(){
        notebookLibManager.loadLocalNoteBook();
    }

    /**
     * 获取单例对象
     * @return Model单例对象
     */
    public static synchronized Model getInstance(){
        if (model == null){
            model = new Model();
        }
        return model;
    }

    /**
     * 获取全局线程池
     * @return
     */
    public ExecutorService getGlobalThreadPool(){
        return executors;
    }

    /**
     * 获取数据库管理者
     * @return
     */
    public DataBaseManager getDataBaseManager(){
        return dataBaseManager;
    }

    /**
     * 获取pageUrl列表
     * @return
     */
    public List<PageUrlModel> getUrlList() {
        return urlList;
    }

    public FileLibManager getFileLibManager(){
        return fileLibManager;
    }

    public NotebookLibManager getNotebookLibManager() {
        return notebookLibManager;
    }

    /**
     * 获取日程表
     * @return
     */
    public List<Schedule> getScheduleList() {
        return scheduleList;
    }
}
