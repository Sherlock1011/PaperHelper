package com.example.paperhelper.view.activity.dialog;


import static com.example.paperhelper.model.constant.FinalValue.ADD_SCHEDULE;
import static com.example.paperhelper.model.constant.FinalValue.DEL_SCHEDULE;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.paperhelper.R;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.NotebookItem;
import com.example.paperhelper.model.bean.PageUrlModel;
import com.example.paperhelper.model.bean.Schedule;
import com.example.paperhelper.view.fragment.adapter.PaperPageRecyclerAdapter;
import com.example.paperhelper.view.fragment.adapter.ScheduleListBaseAdapter;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.util.List;

/**
 * @author Sherlock
 * 自定义对话框
 * 包含多种类型的对话框
 */
public class MyDialog {
    public MyDialog() {

    }

    /**
     * 自定义对话框
     * 用于交互式的添加网址
     * @param activity
     * @param urlList
     */
    public void addPageUrlDialogShow(Activity activity, List<PageUrlModel> urlList){
        QMUIDialog.CustomDialogBuilder customDialogBuilder = new QMUIDialog.CustomDialogBuilder(activity);

        customDialogBuilder.setLayout(com.example.lib_common_ui.R.layout.add_page_url_dialog)
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Addiction", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        //获取EditText控件
                        EditText nameInput = dialog.findViewById(com.example.lib_common_ui.R.id.name_input);
                        EditText urlInput = dialog.findViewById(com.example.lib_common_ui.R.id.url_input);
                        //获取用户输入的name和url
                        String name = nameInput.getText().toString().trim();
                        String url = urlInput.getText().toString().trim();
                        addUrl(activity, name, url, urlList);
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 向列表和数据库中添加PageUrlItem
     * @param name
     * @param url
     * @param urlList
     */
    private void addUrl(Activity activity, String name, String url, List<PageUrlModel> urlList){
        PageUrlModel urlItem = new PageUrlModel(name, url);
        //判断输入是否为空
        if(name.equals("") || url.equals("")){
            warningDialogShow(activity, "Name or URL cannot be empty");
            return;
        }

        //判断输入网址的格式是否正确
        if(!url.contains("http://") || !url.contains("https://")){
            url = "https://" + url;
            urlItem.setUrl(url);
        }

        //判断输入的名称或网址是否存在
        if(urlList.contains(urlItem)){
            warningDialogShow(activity,"Website already exists!");
            return;
        }
        urlList.add(urlItem);
        //将数据添加到数据库中
        Model.getInstance().getDataBaseManager().getPageUrlModelDao().insert(urlItem);
    }

    /**
     * 删除网址Dialog
     * @param activity
     * @param urlList
     * @param position
     */
    public void deletePageUrlDialog(Activity activity, List<PageUrlModel> urlList, int position, PaperPageRecyclerAdapter adapter){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(activity);
        messageDialogBuilder.setTitle("Delete")
                .setMessage("Could you delete " + urlList.get(position).getPageName() + " ？")
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Model.getInstance().getDataBaseManager().getPageUrlModelDao().delete(urlList.get(position));
                        urlList.remove(position);
                        adapter.notifyItemChanged(position);
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 编辑网址列表dialog
     * @param activity
     * @param urlList
     * @param position
     * @param adapter
     */
    public void editPageUrlDialog(Activity activity, List<PageUrlModel> urlList, int position, PaperPageRecyclerAdapter adapter){
        QMUIDialog.CustomDialogBuilder customDialogBuilder = new QMUIDialog.CustomDialogBuilder(activity);
        customDialogBuilder.setLayout(R.layout.edit_page_url_dialog)
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Edit", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        //获取EditText控件
                        EditText nameInput = dialog.findViewById(R.id.name_edit);
                        EditText urlInput = dialog.findViewById(R.id.url_edit);
                        //获取用户输入的name和url
                        String name = nameInput.getText().toString().trim();
                        String url = urlInput.getText().toString().trim();
                        editUrl(activity, name, url, urlList, position, adapter);
                        dialog.dismiss();
                    }
                });
        QMUIDialog qmuiDialog = customDialogBuilder.create();
        EditText edit_name = (EditText) qmuiDialog.findViewById(R.id.name_edit);
        edit_name.setText(urlList.get(position).getPageName());
        EditText edit_url = (EditText) qmuiDialog.findViewById(R.id.url_edit);
        edit_url.setText(urlList.get(position).getUrl());
        qmuiDialog.show();
    }

    /**
     * 修改url信息
     * @param activity
     * @param name
     * @param url
     * @param position
     * @param urlList
     */
    private void editUrl(Activity activity, String name, String url, List<PageUrlModel> urlList, int position, PaperPageRecyclerAdapter adapter){
        PageUrlModel urlItem = new PageUrlModel(name, url);
        //判断输入是否为空
        if(name.equals("") || url.equals("")){
            warningDialogShow(activity, "Name or URL cannot be empty");
            return;
        }

        //判断输入网址的格式是否正确
        if(!url.contains("http://") || !url.contains("https://")){
            url = "https://" + url;
            urlItem.setUrl(url);
        }

        //判断输入的名称或网址是否存在
        if(urlList.contains(urlItem)){
            warningDialogShow(activity,"Website already exists!");
            return;
        }
        urlList.set(position, urlItem);
//        Model.getInstance().getDataBaseManager().getPageUrlModelDao().update(urlItem);
        adapter.notifyItemChanged(position);
    }

    /**
     * 提示对话框
     * 用于提示错误的输入
     * @param msg
     * @param activity
     */
    public void warningDialogShow(Activity activity, String msg){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(activity);
        messageDialogBuilder.setTitle("Reminder")
                .setMessage(msg)
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 确定下载文件提示框
     * @param activity
     * @param file_name
     */
    public QMUIDialog.MessageDialogBuilder fileDownloadConfirmDialog(Activity activity, String file_name){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(activity);
        messageDialogBuilder.setTitle("Download")
                .setMessage("Could you download"+file_name+"?")
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                });
        return messageDialogBuilder;
    }

    /**
     * 操作成功提示框
     * @param activity
     * @param msg
     */
    public QMUITipDialog successOperationDialog(Activity activity, String msg){
        QMUITipDialog.Builder tipDialogBuilder = new QMUITipDialog.Builder(activity);
        tipDialogBuilder.setIconType(QMUITipDialog.Builder.ICON_TYPE_SUCCESS)
                .setTipWord(msg);
        return tipDialogBuilder.create();
    }

    /**
     * 操作失败提示框
     * @param activity
     * @param msg
     */
    public QMUITipDialog failureOperationDialog(Activity activity, String msg){
        QMUITipDialog.Builder tipDialogBuilder = new QMUITipDialog.Builder(activity);
        tipDialogBuilder.setIconType(QMUITipDialog.Builder.ICON_TYPE_FAIL)
                .setTipWord(msg);
        return tipDialogBuilder.create();
    }

    /**
     * 正在下载Dialog
     * @param activity
     * @param msg
     * @return
     */
    public QMUITipDialog downloadingDialog(Activity activity, String msg){
        QMUITipDialog.Builder tipDialogBuilder = new QMUITipDialog.Builder(activity);
        tipDialogBuilder.setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord(msg);
        return tipDialogBuilder.create();
    }

    /**
     * 对所选item进行重命名的对话框
     * @param activity
     * @param title
     * @param content
     * @param inputType
     * @param item
     */
    public void renameItemDialog(Activity activity, String title, String content, int inputType, Object item){
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(activity);
        editTextDialogBuilder.setTitle(title)
                .setPlaceholder(content)
                .setInputType(inputType)
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String inputString = editTextDialogBuilder.getEditText().getText().toString();
                        if(item instanceof NotebookItem){
                            ((NotebookItem) item).setFileName(inputString);
                        }
                        dialog.dismiss();
                    }
                })
                .show();
    }

    /**
     * 删除notebook提示框
     * @param activity 依赖的activity
     * @param deleteItem 要删除的notebook item
     */
    public void deleteNotebookItemDialog(Activity activity, NotebookItem deleteItem, BaseAdapter adapter){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(activity);
        messageDialogBuilder.setTitle("Delete")
                .setMessage("Could you delete " + deleteItem.getFileName() + "?")
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        Model.getInstance().getNotebookLibManager().removeNotebook(deleteItem);
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 添加日程提示框
     * @param activity 依赖的Activity
     * @param handler 消息传输机制，用于进度条刷新
     */
    public void addScheduleDialog(Activity activity,  Handler handler){
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(activity);
        editTextDialogBuilder.setTitle("Add Schedule")
                .setPlaceholder("Please enter the schedule name")
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String scheduleContent = editTextDialogBuilder.getEditText().getText().toString();
                        if(scheduleContent.equals("")){
                            warningDialogShow(activity, "Schedule cannot be empty!");
                        }
                        else {
                            Message msg = Message.obtain();
                            msg.what = ADD_SCHEDULE;
                            msg.obj = scheduleContent;
                            handler.sendMessage(msg);
                            dialog.dismiss();
                        }
                    }
                }).show();
    }

    /**
     * 编辑日程提示框
     * @param activity 依赖的activity
     * @param scheduleList 日程列表
     * @param position schedule item在列表中所在的位置
     * @param adapter 日程列表适配器，用于刷新日程列表
     */
    public void editScheduleDialog(Activity activity, List<Schedule> scheduleList, int position, ScheduleListBaseAdapter adapter){
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(activity);
        editTextDialogBuilder.setTitle("Edit Schedule")
                .setPlaceholder(scheduleList.get(position).getContent())
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String newContent = editTextDialogBuilder.getEditText().getText().toString();
                        if(newContent.equals("")){
                            warningDialogShow(activity, "Schedule cannot be empty!");
                        }
                        else {
                            scheduleList.get(position).setContent(newContent);
                        }
                        adapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 删除指定schedule提示框
     * @param activity 依赖的activity
     * @param handler 消息传输机制，用于进度条刷新
     */
    public void deleteScheduleDialog(Activity activity, Handler handler){
        QMUIDialog.MessageDialogBuilder messageDialogBuilder = new QMUIDialog.MessageDialogBuilder(activity);
        messageDialogBuilder.setTitle("Delete")
                .setMessage("Could you delete schedule")
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        handler.sendEmptyMessage(DEL_SCHEDULE);
                        dialog.dismiss();
                    }
                }).show();
    }

    /**
     * 有输入框的对话框,用于修改TextView
     *
     * @param activity  界面activity
     * @param title     对话框标题
     * @param content   输入框提示内容
     * @param inputTv  要设置的textView
     * @param inPutType 输入框类型，如text、number、phone
     */
    public void editTextViewDialogShow(Activity activity, String title, String content, TextView inputTv, int inPutType) {
        QMUIDialog.EditTextDialogBuilder editTextDialogBuilder = new QMUIDialog.EditTextDialogBuilder(activity);
        editTextDialogBuilder.setTitle(title)
                .setPlaceholder(content)
                .setInputType(inPutType)
                .addAction("Cancel", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("Confirm", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        String inputString = editTextDialogBuilder.getEditText().getText().toString();
                        inputTv.setText(inputString);
                        dialog.dismiss();
                    }
                }).show();
    }
}
