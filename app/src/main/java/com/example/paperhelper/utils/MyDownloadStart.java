package com.example.paperhelper.utils;

import android.app.Activity;
import android.webkit.DownloadListener;
import android.webkit.WebView;

import com.example.lib_network.okhttp.exception.OkHttpException;
import com.example.lib_network.okhttp.response.listener.DisposeDownloadListener;
import com.example.paperhelper.model.Model;
import com.example.paperhelper.model.bean.FileItem;
import com.example.paperhelper.model.constant.FinalValue;
import com.example.paperhelper.view.activity.dialog.DialogManager;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

import java.io.File;

/**
 * @author Sherlock
 * 网络文件下载类
 */
public class MyDownloadStart implements DownloadListener {
    private Activity activity;
    private QMUIDialog.MessageDialogBuilder dialogBuilder;
    QMUITipDialog tipDialog;
    private WebView webView;

    public MyDownloadStart(Activity activity, WebView webView) {
        this.activity = activity;
        this.webView = webView;
    }

    @Override
    public void onDownloadStart(String url, String userAgent,
                                String contentDisposition, String mimetype, long contentLength) {

        String[] urls = url.split("/");
        String fileName = urls[urls.length-1];

        //弹出提示框
        dialogBuilder = DialogManager.getInstance().fileDownloadConfirmDialogShow(activity, fileName);
        dialogBuilder.addAction("Confirm", new QMUIDialogAction.ActionListener() {
            @Override
            public void onClick(QMUIDialog dialog, int index) {
                //下载文件
                downloadFile(url, fileName);
                dialog.dismiss();
            }
        }).show();
    }

    /**
     * 自定义的下载方式
     * @param url
     * @param fileName
     */
    private void downloadFile(String url, String fileName){
        tipDialog = DialogManager.getInstance().downloadingDialogShow(activity,"Downloading...");
        tipDialog.show();
        //调用自己的下载方式
        RequestCenter.downloadFileRequest(url, FinalValue.FOLDER_SRC +fileName, new DisposeDownloadListener() {
            @Override
            public void onSuccess(Object responseObj) {
                File file = new File(FinalValue.FOLDER_SRC +fileName);
                Model.getInstance().getFileLibManager().addFile(new FileItem(file));
                downloadSuccessShow();
            }

            @Override
            public void onFailure(Object reasonObj) {
                downloadFailureShow();
                OkHttpException e = (OkHttpException) reasonObj;
                e.printStackTrace();
            }

            @Override
            public void onProgress(int progress) {
                if(progress == 100){
                    tipDialog.dismiss();
                }
            }
        });
    }


    private void downloadSuccessShow(){
        tipDialog = DialogManager.getInstance().successOperationDialogShow(activity,"Download Success！");
        tipDialog.show();
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }

    private void downloadFailureShow(){
        tipDialog = DialogManager.getInstance().failureOperationDialogShow(activity,"Download Failure！");
        tipDialog.show();
        webView.postDelayed(new Runnable() {
            @Override
            public void run() {
                tipDialog.dismiss();
            }
        }, 1500);
    }
}
