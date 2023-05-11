package com.example.paperhelper.view.activity.dialog;

import android.app.Activity;
import android.os.Handler;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.model.bean.Schedule;
import com.example.paperhelper.view.fragment.adapter.PaperPageRecyclerAdapter;
import com.example.paperhelper.view.fragment.adapter.ScheduleListBaseAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

/**
 * @author Sherlock
 * 对话框显示管理类
 */
public class DialogManager {
    private static DialogManager instance;
    private MyDialog myDialog;

    private DialogManager(){
        myDialog = new MyDialog();
    }

    /**
     * 添加网址dialog
     * @param activity
     * @param urlItemList
     */
    public void addPageUrlDialogShow(Activity activity, List<PageUrlModel> urlItemList){
        myDialog.addPageUrlDialogShow(activity, urlItemList);
    }

    /**
     * 删除网址dialog
     * @param activity
     * @param urlModelList
     * @param position
     */
    public void deletePageUrlDialogShow(Activity activity, List<PageUrlModel> urlModelList, int position, PaperPageRecyclerAdapter adapter){
        myDialog.deletePageUrlDialog(activity, urlModelList, position, adapter);
    }

    /**
     * 编辑网址信息dialog
     * @param activity
     * @param urlList
     * @param position
     * @param adapter
     */
    public void editPageUrlDialogShow(Activity activity, List<PageUrlModel> urlList, int position, PaperPageRecyclerAdapter adapter){
        myDialog.editPageUrlDialog(activity, urlList, position, adapter);
    }

    /**
     * 下载文件确认dialog
     * @param activity
     * @param file_name
     */
    public QMUIDialog.MessageDialogBuilder fileDownloadConfirmDialogShow(Activity activity, String file_name){
        return myDialog.fileDownloadConfirmDialog(activity, file_name);
    }

    /**
     * 操作成功dialog
     * @param activity
     * @param msg
     */
    public QMUITipDialog successOperationDialogShow(Activity activity, String msg){
        return myDialog.successOperationDialog(activity, msg);
    }

    /**
     * 操作失败dialog
     * @param activity
     * @param msg
     */
    public QMUITipDialog failureOperationDialogShow(Activity activity, String msg){
        return myDialog.failureOperationDialog(activity, msg);
    }

    /**
     * 正在下载dialog
     * @param activity
     * @param msg
     * @return
     */
    public QMUITipDialog downloadingDialogShow(Activity activity, String msg){
        return myDialog.downloadingDialog(activity, msg);
    }

    /**
     * 重命名item dialog
     * @param activity 依赖的activity
     * @param title 对话框标题
     * @param content 编辑框内容显示
     * @param inputType 编辑框内容类型
     * @param item 编辑对象
     */
    public void renameItemDialogShow(Activity activity, String title, String content, int inputType, Object item){
        myDialog.renameItemDialog(activity, title, content, inputType, item);
    }

    /**
     * 删除notebook对话框
     * @param activity 依赖的activity
     * @param deleteItem 要删除的notebook item
     */
    public void deleteNotebookItemDialogShow(Activity activity, NotebookItem deleteItem, BaseAdapter adapter){
        myDialog.deleteNotebookItemDialog(activity, deleteItem, adapter);
    }

    /**
     * 添加schedule对话框
     * 有输入框
     * @param activity 依赖的activity
     * @param handler 消息传输机制，用于进度条刷新
     */
    public void addScheduleDialogShow(Activity activity, Handler handler){
        myDialog.addScheduleDialog(activity, handler);
    }

    /**
     * 编辑schedule对话框
     * 有输入框
     * @param activity 依赖的activity
     * @param scheduleList 日程列表
     * @param position schedule在列表中的位置
     * @param adapter 日程列表适配器，用于刷新日程列表
     */
    public void editScheduleDialogShow(Activity activity, List<Schedule> scheduleList, int position, ScheduleListBaseAdapter adapter){

    }

    /**
     *
     * @param activity 依赖的activity
     * @param handler 消息传输机制，用于进度条刷新
     */
    public void deleteScheduleItemDialogShow(Activity activity, Handler handler){
        myDialog.deleteScheduleDialog(activity, handler);
    }

    /**
     * 有输入框的对话框
     *
     * @param activity  界面activity
     * @param title     对话框标题
     * @param content   输入框提示内容
     * @param inputTv  要设置的textView
     * @param inPutType 输入框类型，如text、number、phone
     *                  使用示例：
     *                  MyDialogUtil.editDialogShow(this,"子弹数量","请输入子弹数量", bullet_count_tv,InputType.TYPE_CLASS_TEXT);
     */
    public void editTextViewDialogShow(Activity activity, String title, String content, TextView inputTv, int inPutType){
        myDialog.editTextViewDialogShow(activity, title, content, inputTv, inPutType);
    }

    public static synchronized DialogManager getInstance(){
        if(instance == null){
            instance = new DialogManager();
        }
        return instance;
    }

}
