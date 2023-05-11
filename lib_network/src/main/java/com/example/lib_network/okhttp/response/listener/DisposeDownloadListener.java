package com.example.lib_network.okhttp.response.listener;

/**
 * @author Sherlock
 * 文件类型响应监听
 */
public interface DisposeDownloadListener {
    /**
     * 请求成功回调事件处理
     * @param responseObj
     */
    void onSuccess(Object responseObj);

    /**
     * 请求失败回调事件处理
     * @param reasonObj
     */
    void onFailure(Object reasonObj);

    /**
     * 进度更新回调处理
     * @param progress
     */
    void onProgress(int progress);
}
