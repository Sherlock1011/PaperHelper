package com.example.lib_common_ui;

import static com.qmuiteam.qmui.util.QMUIStatusBarHelper.setStatusBarLightMode;
import static com.qmuiteam.qmui.util.QMUIStatusBarHelper.translucent;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.view.ViewCompat;

import java.lang.reflect.Field;

/**
 * @author Sherlock
 * 导航栏工具类
 */
public class StatusBarManager {
    /**
     * 设置状态栏
     * 使用QMUI内部Android UI
     * */
    public static void setStatusBarFull(Activity activity, Window window, Resources resources){
        //设置沉浸式状态栏
        translucent(activity, Color.WHITE);
//        window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        addMoreView(activity,window,resources);
        setStatusBarLightMode(activity);//设置状态栏字体颜色
    }

    /**
     * 设置状态栏字体为高亮显示
     * */
    public static void setStatusBarTextFountLight(Activity activity){
        translucent(activity,Color.WHITE);
        setStatusBarLightMode(activity);
    }

    /**
     * 添加一个和状态栏高度一样的View
     * 把标题栏顶下去
     * */
    public static void addMoreView(Context context, Window window, Resources resources){
        //获取windowPhone下的decorView
        ViewGroup decorView = (ViewGroup)window.getDecorView();
        int count = decorView.getChildCount();
        //判断是否已经添加了statusBarView
        if (count > 0 && decorView.getChildAt(count - 1) instanceof TextView) {
            decorView.getChildAt(count - 1).setBackgroundColor(Color.WHITE);
        } else {
            //新建一个和状态栏高宽的view
            View statusView = createStatusBarView(context,resources);
            decorView.addView(statusView);
        }
        ViewGroup rootView = (ViewGroup) ((ViewGroup) window.findViewById(android.R.id.content)).getChildAt(0);
        //rootview不会为状态栏留出状态栏空间
        ViewCompat.setFitsSystemWindows(rootView, true);
        rootView.setClipToPadding(true);
    }
    /**
     * 创建一个与状态看一样高度的view
     *
     * @return 返回view
     */
    private static View createStatusBarView(Context context,Resources resources) {
        // 绘制一个和状态栏一样高的矩形
        TextView statusBarView = new TextView(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, getStatusBarHeight(resources));
        statusBarView.setLayoutParams(params);
        statusBarView.setBackgroundColor(Color.WHITE);
        return statusBarView;
    }

    /**
     * 拿到状态栏的高度
     *
     * @return 返回高度
     */
    private static int getStatusBarHeight(Resources resources) {
        Class c;
        try {
            c = Class.forName("com.android.internal.R$dimen");
            Object obj = c.newInstance();
            Field field = c.getField("status_bar_height");
            int x = Integer.parseInt(field.get(obj).toString());
//            Log.e("TTT","打印高度：" + resources.getDimensionPixelSize(x));
            return resources.getDimensionPixelSize(x);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }
}
