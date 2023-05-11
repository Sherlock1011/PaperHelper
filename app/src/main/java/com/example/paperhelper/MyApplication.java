package com.example.paperhelper;

import android.app.Application;
import android.content.Context;

import com.example.paperhelper.model.Model;

/**
 * @author Sherlock
 * 全局Application，用于项目整体初始化
 */
public class MyApplication extends Application {
    /**
     * 全局上下文
     */
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        //初始化模型层
        Model.getInstance().init(mContext);
    }

    /**
     * 获取全局上下文
     * @return
     */
    public static Context getGlobalApplication(){
        return mContext;
    }
}
