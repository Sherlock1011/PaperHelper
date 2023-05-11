package com.example.lib_network.okhttp.response.listener;

/**
 * @author Sherlock
 * 回调处理工具包装类
 */
public class DisposeDataHandle {
    public DisposeDataListener mListener = null;
    public DisposeDownloadListener downloadListener = null;
    public Class<?> mClass = null;
    /**
     * 文件保存路径
     */
    public String mSource = null;

    public DisposeDataHandle(DisposeDataListener listener){
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> mClass){
        this.mListener = listener;
        this.mClass = mClass;
    }

    public DisposeDataHandle(DisposeDownloadListener listener, String source){
        this.downloadListener = listener;
        this.mSource = source;
    }
}
